import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class GUI extends JFrame {
    GirlsList girlsList = new GirlsList();


    public static void main(String args[]) {
        GUI g = new GUI();
    }

    public GUI() {
        JFrame frame = new JFrame("Android Girls");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        panel.setLayout(null);
        frame.add(panel);

        JOptionPane.showMessageDialog(GUI.this, "Поздравляю, хозяин!" +
                " Теперь, у тебя есть личная робо-девочка, заботься о состоянии ее" +
                " механического тела, собирайте вместе новых робо-девочек и стремитесь" +
                " к робораю. ALL HAIL TECHNOCRACY!!");


        Android_Helper android_helper = new Android_Helper();
        ImageIcon image = new ImageIcon("C:/Users/User/Downloads/Android_girls/src/image" +
                android_helper.giveMeicon() + ".png");
        JLabel icon = new JLabel(image);
        panel.add(icon);
        Dimension icon_size = icon.getPreferredSize();
        icon.setBounds(30, 50, icon_size.width, icon_size.height);

        JLabel name = new JLabel(android_helper.giveMeName());
        panel.add(name);
        Dimension name_size = name.getPreferredSize();
        name.setBounds(350, 90, name_size.width, name_size.height);

        JButton job = new JButton("Менеджер");
        panel.add(job);
        Dimension job_size = job.getPreferredSize();
        job.setBounds(350, 110, job_size.width, job_size.height);

        job.addActionListener(n -> {
            JList<String> displayList = new JList<>(girlsList.all_girls.toArray(new String[0]));
            JScrollPane scrollPane = new JScrollPane(displayList);
            getContentPane().add(scrollPane);
            pack();
            setVisible(true);
        });

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

        JButton pick_flower = new JButton("Собрать цветы");
        panel.add(pick_flower);
        pick_flower.setBounds(600, 110, 130, 60);

        pick_flower.addActionListener(v -> {
            Pick_Flowers pick_flowers = new Pick_Flowers();
            flower_count.setText(pick_flowers.flowerPicker() + " цветов собрано (*´▽`*)");
        });

        JButton pick_garbage = new JButton("Собрать мусор");
        panel.add(pick_garbage);
        pick_garbage.setBounds(600, 180, 130, 60);

        pick_garbage.addActionListener(k -> {
            Pick_Garbage garbage = new Pick_Garbage();
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    garbage_count.setText(garbage.garbagePicker() + " мусора собрано (≧◡≦)");
                }
            }, 0, 5000);
        });

        JButton make_fuel = new JButton("Экстрактить топливо");
        panel.add(make_fuel);
        make_fuel.setBounds(600, 250, 160, 60);

        make_fuel.addActionListener(mi -> {
            Alchemy alchemy = new Alchemy();
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    fuel_count.setText(alchemy.alchemyFuel() + " топлива извлечено");
                }
            }, 0, 15000);
        });

        JButton make_details = new JButton("Выплавить детальки");
        panel.add(make_details);
        make_details.setBounds(600, 320, 170, 60);

        make_details.addActionListener(nipa -> {
            Mechanic mechanic = new Mechanic();
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    details_count.setText(mechanic.mechanicDetails() + " деталек выплавлено");
                }
            }, 0, 15000);
        });

        frame.setVisible(true);
    }

}
