import javax.swing.*;
import java.awt.*;


public class GUI extends JFrame {
    public static void main(String args[]) {
        GUI g = new GUI();
    }

    public GUI() {
        JFrame frame = new JFrame("Android Girls");
        JPanel panel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        JButton button = new JButton("Привет, новый робохозяин!" +
                " Щелкни на меня и получи своего первого рандомного помощника!");
        Dimension size = button.getPreferredSize();
        button.setBounds(650, 500, size.width, size.height);
        panel.setLayout(null);
        panel.add(button);
        frame.add(panel);

        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(GUI.this, "Поздравляю, хозяин!" +
                    " Теперь, у тебя есть личная робо-девочка, заботься о состоянии ее" +
                    " механического тела, собирайте вместе новых робо-девочек и стремитесь" +
                    " к робораю. ALL HAIL TECHNOCRACY!!");
            panel.remove(button);
            panel.repaint();

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

            JButton job = new JButton(android_helper.giveMejob());
            panel.add(job);
            Dimension job_size = job.getPreferredSize();
            job.setBounds(350, 110, job_size.width, job_size.height);

            job.addActionListener(n -> {
                JFrame job_frame = new JFrame("Все девочки");
                JScrollBar hbar= new JScrollBar(JScrollBar.HORIZONTAL, 30, 20, 0, 500);
                JScrollBar vbar= new JScrollBar(JScrollBar.VERTICAL, 30, 40, 0, 500);
                job_frame.setLayout(new BorderLayout( ));
                job_frame.setSize(300,200);
                job_frame.getContentPane().add(hbar, BorderLayout.SOUTH);
                job_frame.getContentPane().add(vbar, BorderLayout.EAST);
                job_frame.setVisible(true);
            });

        });


        frame.setVisible(true);
    }

}
