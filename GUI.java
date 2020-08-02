import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GUI extends JFrame {
    private static final long serialVersionUID = -1267352938435573633L;
    Pick_Flowers p = Pick_Flowers.getInstance();
    Pick_Garbage g = Pick_Garbage.getInstance();
    Alchemy a = Alchemy.getInstance();
    Mechanic m = Mechanic.getInstance();
    GirlsLists girlsLists = GirlsLists.getInstance();
    FileManage fileManage = new FileManage();
    EnergyGenerator energyGenerator = EnergyGenerator.getInstance();
    Android_Helper android_helper = new Android_Helper();
    JLabel name = new JLabel();
    JLabel energy_count = new JLabel("x энергии сейчас");
    JFrame frame = new JFrame("Android Girls");
    ImageIcon icon1 = new ImageIcon(getClass().getResource("/button.jpg"));
    Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/background.jpg"));
    Image image1 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/button.jpg"));
    JLabel details_left = new JLabel("30 деталек");
    Thread garbageThread, flowerThread, fuelThread, detailsThread;
    String android_path = "C:/Users/User/Documents/NyanData/Androids";
    JLabel free = new JLabel("Free: 0");

    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), this);
            g.drawImage(image1, 600, 80, 300, 600, this);
        }
    };


    public static void main(String args[]) throws IOException {
        new GUI();
    }


    public GUI() throws IOException {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                UIManager.put("OptionPane.yesButtonText", "Угу..");
                UIManager.put("OptionPane.noButtonText", "Нет, я скоро вернусь..");
                if (JOptionPane.showConfirmDialog(frame,
                        "Удалить текущий мир перед возвращением?", "Возвращение в свою реальность.",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    fileManage.deleteDirectory(new File("C:/Users/User/Documents/NyanData"));
                }
                System.exit(0);
            }
        });
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.add(panel);
        panel.setLayout(null);
        panel.setVisible(true);

        JOptionPane.showMessageDialog(GUI.this, "Поздравляю, хозяин!" +
                " Теперь, у тебя есть личная робо-девочка, заботься о состоянии ее" +
                " механического тела, собирайте вместе новых робо-девочек и стремитесь" +
                " к робораю. ALL HAIL TECHNOCRACY!!");


        ImageIcon brokenGeneratorIcon = new ImageIcon(getClass().getResource("/broken_generator.png"));
        JLabel brokenGeneratorLabel = new JLabel(brokenGeneratorIcon);
        panel.add(brokenGeneratorLabel);
        Dimension icon_size = brokenGeneratorLabel.getPreferredSize();
        brokenGeneratorLabel.setBounds(1100, 130, icon_size.width, icon_size.height);

        JLabel flower_count = new JLabel("0 цветов");
        panel.add(flower_count);
        flower_count.setFont(new Font("Serif", Font.BOLD, 18));
        flower_count.setBounds(800, 110, 260, 60);

        JLabel garbage_count = new JLabel("0 мусора");
        panel.add(garbage_count);
        garbage_count.setFont(new Font("Serif", Font.BOLD, 18));
        garbage_count.setBounds(800, 180, 260, 60);

        JLabel fuel_count = new JLabel("0 топлива");
        panel.add(fuel_count);
        fuel_count.setFont(new Font("Serif", Font.BOLD, 18));
        fuel_count.setBounds(800, 250, 260, 60);

        JLabel details_count = new JLabel("0 деталек");
        details_count.setFont(new Font("Serif", Font.BOLD, 18));
        panel.add(details_count);
        details_count.setBounds(800, 320, 600, 60);

        details_left.setFont(new Font("Serif", Font.BOLD, 18));
        panel.add(details_left);
        details_left.setBounds(1170, 360, 600, 60);

        JLabel flower_pickers = new JLabel("Flower pickers: 0");
        flower_pickers.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        panel.add(flower_pickers);
        flower_pickers.setBounds(600, 110, 145, 60);

        flowerThread = new Thread(() -> {
            TimerWrapper pick_flower = new TimerWrapper(flower_count, 5_000);
            pick_flower.run(() -> p.flowerPicker(girlsLists.getGirlsList("flower_picker")), " цветочков");
        });


        free.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        panel.add(free);
        free.setBounds(600, 180, 145, 60);

        JLabel garbagers = new JLabel("Garbagers: 0");
        garbagers.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        panel.add(garbagers);
        garbagers.setBounds(600, 250, 145, 60);

        garbageThread = new Thread(() -> {
            TimerWrapper pick_old_staff = new TimerWrapper(garbage_count, 5_000);
            pick_old_staff.run(() -> g.garbagePicker(girlsLists.getGirlsList("garbage_picker")), " мусора");
        });

        JLabel alchemists = new JLabel("Alchemists: 0");
        alchemists.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        alchemists.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(alchemists);
        alchemists.setBounds(600, 320, 145, 60);

        fuelThread = new Thread(() -> {
            TimerWrapper making_fuel = new TimerWrapper(fuel_count, 15_000);
            making_fuel.run(() -> a.alchemyFuel(girlsLists.getGirlsList("alchemy_gurlz")), " топлива");
        });

        JLabel mechanics = new JLabel("Mechanics: ");
        mechanics.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        panel.add(mechanics);
        mechanics.setBounds(600, 390, 145, 60);

        detailsThread = new Thread(() -> {
            TimerWrapper make_details = new TimerWrapper(details_count, 10_000);
            make_details.run(() -> m.mechanicDetails(girlsLists.getGirlsList("mechanic_gurlz")), " деталек");
        });


        JButton manage = new JButton("Job manage");
        manage.setFont(new Font("Century Schoolbook", Font.BOLD, 15));
        manage.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(manage);
        manage.setBounds(600, 80, 140, 30);

        manage.addActionListener(hi -> {
            ManagementMenu managementMenu = new ManagementMenu();
            managementMenu.createPopUpMenu(manage, free, garbagers, garbageThread, mechanics, detailsThread);
        });

        JOptionPane.showMessageDialog(GUI.this, "Девочки-андроиды выключатся, " +
                "если запас энергии будет на нуле. Соберите достаточно деталек, чтобы починить ветряной генератор. " +
                "Пока что энергии есть некоторое количество, но поторопитесь, из-за поломки она сейчас " +
                "не пополняется.");

        startGame();
    }

    public void startGame() throws IOException {
        new File("C:/Users/User/Documents/NyanData").mkdirs();
        String id_path = "C:/Users/User/Documents/NyanData/helper";
        int helper_icon_id = Integer.parseInt(fileManage.fileLoad(id_path,
                "Id иконки: ", 1, true));
        if (helper_icon_id == 0) {
            NewAndroid n = new NewAndroid();
            girlsLists.setGirlsList("free_girls", n);
            helper_icon_id = n.getIconId();
            System.out.println("Имя девочки: " + n.getName());
            System.out.println("Ее описание: " + n.getInfo());
            System.out.println("Id ее иконки: " + n.getIconId());
            System.out.println("Версия девочки: " + n.getVersion());
            free.setText("Free: 1");
        }

        ImageIcon image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/image" +
                helper_icon_id + ".png")));
        JLabel icon = new JLabel(image);
        panel.add(icon);
        Dimension icon_size = icon.getPreferredSize();
        icon.setBounds(30, 50, icon_size.width, icon_size.height);

        String helper_name = fileManage.fileLoad(id_path, "Имя помощницы: ", 2, false);
        if (helper_name == null) {
            helper_name = girlsLists.free_girls.get(0).getName();
        }
        name.setText(helper_name);
        name.setFont(new Font("Serif", Font.BOLD, 18));
        panel.add(name);
        Dimension name_size = name.getPreferredSize();
        name.setBounds(350, 90, name_size.width, name_size.height);

        fileManage.fileCreate(android_path);
        int all_girls = Integer.parseInt(fileManage.fileLoad(android_path,
                "Всего: ", 1, true));
        if (all_girls == 0) {
            girlsLists.setAll_girls(1);
        }
        fileManage.fileLoad(android_path, "Безработные: ", 2, true);
        threadStarter();

        JButton job = new JButton("Менеджер", icon1);
        job.setFont(new Font("Century Schoolbook", Font.BOLD, 15));
        job.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(job);
        job.setBounds(350, 110, 140, 30);

        job.addActionListener(n -> {
            ManagementMenu managementMenu = new ManagementMenu();
            managementMenu.createManageMenu();
        });

        panel.add(energy_count);
        energy_count.setFont(new Font("Serif", Font.BOLD, 18));
        Dimension energy_count_size = energy_count.getPreferredSize();
        energy_count.setBounds(1170, 100, 400, energy_count_size.height);

        int energy_saved = Integer.parseInt(fileManage.fileLoad("C:/Users/User/Documents/NyanData/EnergyGenerators",
                "Ветряной генератор №1: ", 2, true));
        if (energy_saved == 0) {
            energyGenerator.setEnergy(400);
        }

        TimerWrapper energy = new TimerWrapper(energy_count, 10000);
        energy.runLimited(() -> {
            try {
                int energy_now = energyGenerator.eatEnergy();
                int details = m.getDetails();
                details_left.setText((30 - details) + " деталек осталось собрать");
                if (details == 30) {
                    details_left.setText("");
                    win();
                    energyGenerator.giveEnergy();
                }
                if (energy_now < 1) {
                    energy_count.setText("Энергии 0. Андроиды уснули.");
                    badEndmessage();
                }
                return energy_now;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, " энергии сейчас (´-ω-`)");
    }

    public void restart() {
        fileManage.deleteDirectory(new File("C:/Users/User/Documents/NyanData"));
        name.setText("");
        energy_count.setText("x энергии сейчас");
        //обнулить переменные у процессов и array-листы
    }

    public void win() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/goodbye.png"));
        UIManager.put("OptionPane.okButtonText", "All hail technocracy~");
        JOptionPane.showConfirmDialog(frame, "Теперь девочка обязательно выживет!", "Спасибо, хозяин",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
    }

    public void badEndmessage() throws IOException {
        System.out.println("Конец игры.");
        UIManager.put("OptionPane.yesButtonText", "Помочь мечте андроидов сбыться в новой реальности");
        UIManager.put("OptionPane.noButtonText", "Оставить этот умирающий мир");
        int result = JOptionPane.showConfirmDialog(null,
                "Няша, твоя рабыня навеки застыла без энергии." +
                        " Сейчас ее программное обеспечение связи с параллельным миром отключится.",
                "Конец реальности", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            restart();
            startGame();
        }
        if (result == JOptionPane.NO_OPTION) {
            fileManage.deleteDirectory(new File("C:/Users/User/Documents/NyanData"));
            System.exit(0);
        }
    }

    public void threadStarter() throws IOException {
        int garbargers = Integer.parseInt(fileManage.fileLoad(android_path,
                "Старьевщицы: ", 3, true));
        int mechanics = Integer.parseInt(fileManage.fileLoad(android_path,
                "Механики: ", 4, true));
        int flower_girls = Integer.parseInt(fileManage.fileLoad(android_path,
                "Цветочницы: ", 5, true));
        int alchemists = Integer.parseInt(fileManage.fileLoad(android_path,
                "Алхимики: ", 6, true));
        Map<Integer, Thread> map = new HashMap();
        map.put(garbargers, garbageThread);
        map.put(mechanics, detailsThread);
        map.put(flower_girls, flowerThread);
        map.put(alchemists, fuelThread);

        for (Map.Entry<Integer, Thread> pair : map.entrySet()) {
            if (pair.getKey() > 0) {
                pair.getValue().start();
            }
        }
        map.clear();
    }

    public void saveAll() {
        /*сохранить все текущие данные в файлы по категориям:
        1.Ресурсы
        2.Энерго-генераторы
        3.(разные файлы, разбитые по профессиям)
        4. Техно-объекты
         */
        //fileManage.fileSave(id_path, "Id иконки: ", String.valueOf(helper_icon_id), 1, true);
        //fileManage.fileSave(id_path, "Имя помощницы: ", helper_name, 2, false);
    }

}
