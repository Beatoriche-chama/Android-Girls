import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.*;


public class ManagementMenu implements ActionListener, ItemListener {
    FileManage fileManage = new FileManage();
    DefaultListModel free_girls_model = new DefaultListModel();
    DefaultListModel garbagers_model = new DefaultListModel();
    JList free_girls_list = new JList(free_girls_model);
    JList garbagers_list = new JList(garbagers_model);

    public JMenuBar createMenuBar() {
        //create a menubar

        JMenuBar menuBar;
        JMenu managemenu;
        JMenu hiresubmenu, dismisssubmenu;
        JMenuItem garbage_menuItem, garbage1_menuItem, mechanic_menuItem, mechanic1_menuItem;


        //Create the menu bar.
        menuBar = new JMenuBar();

        //Build the Edit menu.
        managemenu = new JMenu("Manage");
        managemenu.setMnemonic(KeyEvent.VK_E);
        managemenu.getAccessibleContext().setAccessibleDescription("Girls picking up garbage.");
        menuBar.add(managemenu);

        //menu items
        hiresubmenu = new JMenu("Hire girl here");
        hiresubmenu.setMnemonic(KeyEvent.VK_X);
        hiresubmenu.addActionListener(this);
        managemenu.add(hiresubmenu);

        garbage_menuItem = new JMenuItem("Garbargers");
        garbage_menuItem.getAccessibleContext().setAccessibleDescription("");
        garbage_menuItem.addActionListener(e -> {

            try {
                hireGirl("garbagers_girls_count", "garbagers_girls_names");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            //только что убранную из безработных убрать в списке свободных
            //и добавить в список собирательниц мусора
            for (String gurlz : fileManage.girlsLoad("garbagers_girls_names").toArray(new String[0])) {
                garbagers_model.addElement(gurlz);
            }
            free_girls_model.addElement("Нян");

        });
        hiresubmenu.add(garbage_menuItem);

        mechanic_menuItem = new JMenuItem("Mechanics");
        mechanic_menuItem.getAccessibleContext().setAccessibleDescription("");
        mechanic_menuItem.addActionListener(k -> {
            try {
                hireGirl("mechanic_girls_count", "mechanic_girls_count");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        hiresubmenu.add(mechanic_menuItem);

        dismisssubmenu = new JMenu("Dismiss girl here");
        dismisssubmenu.setMnemonic(KeyEvent.VK_X);
        dismisssubmenu.addActionListener(this);
        managemenu.add(dismisssubmenu);

        garbage1_menuItem = new JMenuItem("Garbargers");
        garbage1_menuItem.getAccessibleContext().setAccessibleDescription("");
        garbage1_menuItem.addActionListener(b -> {
            dismissGirl("garbagers_girls_count");
        });
        dismisssubmenu.add(garbage1_menuItem);

        mechanic1_menuItem = new JMenuItem("Mechanics");
        mechanic1_menuItem.getAccessibleContext().setAccessibleDescription("");
        mechanic1_menuItem.addActionListener(l -> {
            dismissGirl("mechanic_girls_count");
        });
        dismisssubmenu.add(mechanic1_menuItem);

        return menuBar;
    }

    public void createManageMenu() {


        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        JPanel pan1 = new JPanel(new GridLayout(3, 1));
        pan1.add(free_girls_list);
        JPanel pan2 = new JPanel(new GridLayout(3, 1));
        pan2.add(garbagers_list);
        JScrollPane scrollPane1 = new JScrollPane(pan1);
        JScrollPane scrollPane2 = new JScrollPane(pan2);
        tabbedPane.add("Free girls", scrollPane1);
        tabbedPane.add("Garbargers", scrollPane2);

        JFrame frame = new JFrame("Android Management Menu v.1337");
        ManagementMenu smenu = new ManagementMenu();
        frame.setJMenuBar(smenu.createMenuBar());
        frame.add(tabbedPane);

        /* show frame */
        frame.pack();
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private String hireGirl(String job_counter, String job_name) throws IOException {
        String job_names = "free_girls_names";
        String random_girl_name = randomName(job_names);
        int free_girls_count = fileManage.fileLoad("free_girls_count");
        if (free_girls_count > 0) {
            fileManage.fileSave("free_girls_count",
                    free_girls_count - 1);
            fileManage.fileSave(job_counter,
                    1 + fileManage.fileLoad(job_counter));
            fileManage.girlSave(job_name, random_girl_name);
        }
        return random_girl_name;
    }

    private void dismissGirl(String job_name) {
        try {
            int job_count = fileManage.fileLoad(job_name);
            if (job_count > 0) {
                fileManage.fileSave(job_name,
                        job_count - 1);
                fileManage.fileSave("free_girls_count",
                        1 + fileManage.fileLoad("free_girls_count"));
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private String randomName(String file_name) {
        Scanner s = null;
        try {
            s = new Scanner(new File(file_name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<>();
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();
        Random rand = new Random();
        String name = list.get(rand.nextInt(list.size()));
        return name;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JMenuItem jmi = (JMenuItem) e.getSource();
        System.out.println("menu item clicked: " + jmi.getText());
        if (jmi.getText().equalsIgnoreCase("close")) {
            System.exit(0);
        }

    }
}