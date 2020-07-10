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
    GirlsList girlsList = new GirlsList();
    private static final long serialVersionUID = -1267352938435573633L;
    Pick_Flowers p = new Pick_Flowers();
    Pick_Garbage g = new Pick_Garbage();
    Alchemy a = new Alchemy();
    Mechanic m = new Mechanic();

    public static void main(String args[]) {
        new GUI();
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

        TimerWrapper pick_flowers = new TimerWrapper(flower_count, 5_000);
        pick_flower.addActionListener(v -> pick_flowers.run(()-> {
            try {
                return p.flowerPicker();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }, " собрано цветочков (´｡• ω •｡`)"));

        ImageIcon icon1 = new ImageIcon("C:/Users/User/Downloads/Android_girls/src/button.jpg");
        JButton pick_garbage = new JButton("Pick garbage", icon1);
        pick_garbage.setFont(new Font("Century Schoolbook", Font.BOLD, 16));
        pick_garbage.setHorizontalTextPosition(SwingConstants.CENTER);
        panel.add(pick_garbage);
        pick_garbage.setBounds(600, 180, 145, 60);

        TimerWrapper pick_old_staff = new TimerWrapper(garbage_count, 5_000);
        pick_garbage.addActionListener(k -> pick_old_staff.run(()-> g.garbagePicker(), " собрано мусора (*¯︶¯*)"));

        JButton make_fuel = new JButton("Экстрактить топливо");
        panel.add(make_fuel);
        make_fuel.setBounds(600, 250, 160, 60);

        TimerWrapper making_fuel = new TimerWrapper(fuel_count, 15_000);
        make_fuel.addActionListener(v -> making_fuel.run(()-> a.alchemyFuel(), " топлива выделено (≧◡≦)"));

        JButton make_details = new JButton("Выплавить детальки");
        panel.add(make_details);
        make_details.setBounds(600, 320, 170, 60);

        TimerWrapper making_details = new TimerWrapper(fuel_count, 15_000);
        make_details.addActionListener(v -> making_details.run(()-> m.mechanicDetails(), " деталек изготовлено <(￣︶￣)>"));

        frame.setVisible(true);
    }

}
