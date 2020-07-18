import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends JFrame {
    private static final long serialVersionUID = -1267352938435573633L;
    Pick_Flowers p = new Pick_Flowers();
    Pick_Garbage g = new Pick_Garbage();
    Alchemy a = new Alchemy();
    Mechanic m = new Mechanic();
    FileManage fileManage = new FileManage();
    EnergyGenerator energyGenerator = new EnergyGenerator();
    Android_Helper android_helper = new Android_Helper();
    JLabel name = new JLabel();
    JLabel energy_count = new JLabel("15 энергии сейчас");
    JFrame frame = new JFrame("Android Girls");
    ImageIcon icon1 = new ImageIcon("C:/Users/User/Downloads/Android_girls/src/button.jpg");
    Image image = ImageIO.read(new File("C:/Users/User/Downloads/Android_girls/src/background.jpg"));
    JPanel panel = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, frame.getWidth(), frame.getHeight(), this);
        }
    };

    public static void main(String args[]) throws IOException {
        new GUI();
    }


    public GUI() throws IOException {

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
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

        JOptionPane.showMessageDialog(GUI.this, "Поздравляю, хозяин!" +
                " Теперь, у тебя есть личная робо-девочка, заботься о состоянии ее" +
                " механического тела, собирайте вместе новых робо-девочек и стремитесь" +
                " к робораю. ALL HAIL TECHNOCRACY!!");

        start();
        timerCheck();

        JLabel flower_count = new JLabel("0 цветов");
        panel.add(flower_count);
        flower_count.setBounds(800, 110, 200, 60);

        JLabel garbage_count = new JLabel("0 мусора");
        panel.add(garbage_count);
        garbage_count.setBounds(800, 180, 200, 60);

        JLabel fuel_count = new JLabel("0 топлива");
        panel.add(fuel_count);
        fuel_count.setBounds(800, 250, 200, 60);

        JLabel details_count = new JLabel("0 деталек");
        panel.add(details_count);
        details_count.setBounds(800, 330, 200, 60);

        JButton pick_flower = new JButton("Pick flowers", icon1);
        pick_flower.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        pick_flower.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(pick_flower);
        pick_flower.setBounds(600, 110, 145, 60);

        TimerWrapper pick_flowers = new TimerWrapper(flower_count, 5_000);
        pick_flower.addActionListener(v -> pick_flowers.run(() -> {
            try {
                return p.flowerPicker();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, " собрано цветочков (´｡• ω •｡`)"));

        JButton pick_garbage = new JButton("Pick garbage", icon1);
        pick_garbage.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        pick_garbage.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(pick_garbage);
        pick_garbage.setBounds(600, 180, 145, 60);

        TimerWrapper pick_old_staff = new TimerWrapper(garbage_count, 5_000);
        //в TimeWrapper
        //if(button.getText().equals("Stop")) => обычная работа
        //else => остановка процесса
        //в слушателе кнопки по нажатию менять Pick и Stop

        pick_garbage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (pick_garbage.getText().equals("Pick garbage")) {
                    pick_garbage.setText("Stop");
                } else {
                    pick_garbage.setText("Pick garbage");
                }
                pick_old_staff.run(() -> {
                    try {
                        return g.garbagePicker();
                    } catch (IOException er) {
                        er.printStackTrace();
                    }
                    return null;
                }, " собрано мусора (*¯︶¯*)");
            }
        });


        JButton make_fuel = new JButton("Extract fuel", icon1);
        make_fuel.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        make_fuel.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(make_fuel);
        make_fuel.setBounds(600, 250, 145, 60);

        TimerWrapper making_fuel = new TimerWrapper(fuel_count, 15_000);
        make_fuel.addActionListener(v -> making_fuel.run(() -> {
            try {
                return a.alchemyFuel();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, " топлива выделено (≧◡≦)"));

        JButton make_details = new JButton("Make details", icon1);
        make_details.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        make_details.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(make_details);
        make_details.setBounds(600, 320, 145, 60);

        TimerWrapper making_details = new TimerWrapper(details_count, 15_000);
        make_details.addActionListener(v -> making_details.run(() -> {
            try {
                return m.mechanicDetails();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, " деталек изготовлено <(￣︶￣)>"));

        JOptionPane.showMessageDialog(GUI.this, "Девочки-андроиды выключатся, " +
                "если запас энергии будет на нуле. Соберите достаточно деталек, чтобы починить ветряной генератор. " +
                "Пока что энергии есть некоторое количество, но поторопитесь, из-за поломки она сейчас " +
                "не пополняется.");

        panel.setVisible(true);
    }

    public void start() throws IOException {
        new File("C:/Users/User/Documents/NyanData").mkdirs();
        int all_girls = fileManage.fileLoad("C:/Users/User/Documents/NyanData/all_girls_count");
        if (all_girls < 1) {
            fileManage.fileSave("C:/Users/User/Documents/NyanData/all_girls_count", 1);
        }
        int free_girls = fileManage.fileLoad("C:/Users/User/Documents/NyanData/free_girls_count");
        if (free_girls == all_girls) {
            fileManage.fileSave("C:/Users/User/Documents/NyanData/free_girls_count", 1);
        }
        fileManage.fileLoad("C:/Users/User/Documents/NyanData/garbagers_girls_count");
        fileManage.fileLoad("C:/Users/User/Documents/NyanData/mechanic_girls_count");


        String id_path = "C:/Users/User/Documents/NyanData/helper_icon_id";
        File icon_id = new File(id_path);
        int helper_icon_id;
        if (icon_id.length() == 0) {
            helper_icon_id = android_helper.giveMeicon();
            fileManage.fileCreate(id_path);
            fileManage.fileSave(id_path, helper_icon_id);
        } else {
            helper_icon_id = fileManage.fileLoad(id_path);
        }

        ImageIcon image = new ImageIcon("C:/Users/User/Downloads/Android_girls/src/image" +
                helper_icon_id + ".png");
        JLabel icon = new JLabel(image);
        panel.add(icon);
        Dimension icon_size = icon.getPreferredSize();
        icon.setBounds(30, 50, icon_size.width, icon_size.height);

        String helper_name = android_helper.giveMeName();
        try {
            fileManage.girlsLoad("C:/Users/User/Documents/NyanData/mechanic_girls_names");
            fileManage.girlsLoad("C:/Users/User/Documents/NyanData/garbagers_girls_names");
            fileManage.girlsLoad("C:/Users/User/Documents/NyanData/free_girls_names");
            File helper = new File("C:/Users/User/Documents/NyanData/helper_name");
            if (helper.length() == 0) {
                name.setText(helper_name);
                fileManage.girlSave("C:/Users/User/Documents/NyanData/helper_name", helper_name);
                fileManage.girlSave("C:/Users/User/Documents/NyanData/free_girls_names", helper_name);
            } else {
                name.setText(fileManage.helperNameLoad("C:/Users/User/Documents/NyanData/helper_name"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        panel.add(name);
        Dimension name_size = name.getPreferredSize();
        name.setBounds(350, 90, name_size.width, name_size.height);

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
        Dimension energy_count_size = energy_count.getPreferredSize();
        energy_count.setBounds(930, 125, 200, energy_count_size.height);

        String energy_path = "C:/Users/User/Documents/NyanData/energy_count";
        int energy_saved = fileManage.fileLoad(energy_path);
        if(energy_saved == 0){
            fileManage.fileSave(energy_path, 100);
        }

        TimerWrapper energy = new TimerWrapper(energy_count, 10000);
        energy.runLimited(() -> {
            try {
                return energyGenerator.eatEnergy();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, " энергии сейчас (´-ω-`)");

    }

    public void restart() {
        fileManage.deleteDirectory(new File("C:/Users/User/Documents/NyanData"));
    }

    public void timerCheck() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Проверяю, что не ноль.");
                    if (fileManage.fileLoad("C:/Users/User/Documents/NyanData/energy_count") < 1) {
                        timer.cancel();
                        timer.purge();
                        System.out.println("Больше не проверяю.");
                        badEndmessage();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(timerTask, 0, 5000);
    }

    public void badEndmessage() throws IOException {
        if (fileManage.fileLoad("C:/Users/User/Documents/NyanData/energy_count") < 1) {
            System.out.println("Конец игры.");
            UIManager.put("OptionPane.yesButtonText", "Помочь мечте андроидов сбыться в новой реальности");
            UIManager.put("OptionPane.noButtonText", "Оставить этот умирающий мир");
            int result = JOptionPane.showConfirmDialog(GUI.this,
                    "Няша, твоя рабыня навеки застыла без энергии." +
                            " Сейчас ее программное обеспечение связи с параллельным миром отключится.",
                    "Конец реальности", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                name.setText("");
                energy_count.setText("15 энергии сейчас");
                restart();
                start();
                timerCheck();
            }
            if (result == JOptionPane.NO_OPTION) {
                fileManage.deleteDirectory(new File("C:/Users/User/Documents/NyanData"));
                System.exit(0);
            }

        }
    }

}