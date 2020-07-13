import javax.swing.*;
import java.awt.*;
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
    JPanel panel = new JPanel();
    Android_Helper android_helper = new Android_Helper();
    JLabel name = new JLabel();
    JLabel energy_count = new JLabel("15 энергии сейчас");

    public static void main(String args[]) {
        new GUI();
    }


    public GUI() {
        JFrame frame = new JFrame("Android Girls");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        panel.setLayout(null);
        frame.add(panel);

        JOptionPane.showMessageDialog(GUI.this, "Поздравляю, хозяин!" +
                " Теперь, у тебя есть личная робо-девочка, заботься о состоянии ее" +
                " механического тела, собирайте вместе новых робо-девочек и стремитесь" +
                " к робораю. ALL HAIL TECHNOCRACY!!");

        start();
        timerCheck();

        JLabel flower_count = new JLabel("0 цветов");
        panel.add(flower_count);
        Dimension flower_count_size = flower_count.getPreferredSize();
        flower_count.setBounds(800, 125, 200, flower_count_size.height);

        JLabel garbage_count = new JLabel("0 мусора");
        panel.add(garbage_count);
        Dimension garbage_count_size = garbage_count.getPreferredSize();
        garbage_count.setBounds(800, 200, 200, garbage_count_size.height);

        JLabel fuel_count = new JLabel("0 топлива");
        panel.add(fuel_count);
        Dimension fuel_count_size = fuel_count.getPreferredSize();
        fuel_count.setBounds(800, 275, 200, fuel_count_size.height);

        JLabel details_count = new JLabel("0 деталек");
        panel.add(details_count);
        Dimension details_count_size = details_count.getPreferredSize();
        details_count.setBounds(800, 350, 200, details_count_size.height);

        ImageIcon icon1 = new ImageIcon("C:/Users/User/Downloads/Android_girls/src/button.jpg");
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
        pick_garbage.addActionListener(k -> pick_old_staff.run(() -> {
            try {
                return g.garbagePicker();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, " собрано мусора (*¯︶¯*)"));

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

        TimerWrapper making_details = new TimerWrapper(fuel_count, 15_000);
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

        frame.setVisible(true);
    }

    public void start() {
        try {
            fileManage.fileCreate("girls_count");
            fileManage.fileSave("girls_count", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        ImageIcon image = new ImageIcon("C:/Users/User/Downloads/Android_girls/src/image" +
                android_helper.giveMeicon() + ".png");
        JLabel icon = new JLabel(image);
        panel.add(icon);
        Dimension icon_size = icon.getPreferredSize();
        icon.setBounds(30, 50, icon_size.width, icon_size.height);

        name.setText(android_helper.giveMeName());
        panel.add(name);
        Dimension name_size = name.getPreferredSize();
        name.setBounds(350, 90, name_size.width, name_size.height);
        try {
            fileManage.fileCreate("all_girls");
            fileManage.girlSave("all_girls", name.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JButton job = new JButton("Менеджер");
        panel.add(job);
        Dimension job_size = job.getPreferredSize();
        job.setBounds(350, 110, job_size.width, job_size.height);

        job.addActionListener(n -> {
            JList displayList = new JList<>(fileManage.girlsLoad("all_girls").toArray(new String[0]));
            JScrollPane scrollPane = new JScrollPane(displayList);
            scrollPane.createVerticalScrollBar();
            getContentPane().add(scrollPane);
            pack();
            setVisible(true);
        });


        panel.add(energy_count);
        Dimension energy_count_size = energy_count.getPreferredSize();
        energy_count.setBounds(930, 125, 200, energy_count_size.height);

        fileManage.fileCreate("energy_count");
        try {
            fileManage.fileSave("energy_count", 15);
        } catch (IOException e) {
            e.printStackTrace();
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

    public void timerCheck() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println("Проверяю, что не ноль.");
                    if (fileManage.fileLoad("energy_count") < 1) {
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
        if (fileManage.fileLoad("energy_count") < 1) {
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
                start();
                timerCheck();
            }
            if (result == JOptionPane.NO_OPTION) {
                System.exit(0);
            }

        }
    }
}
