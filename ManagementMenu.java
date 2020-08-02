import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;


public class ManagementMenu implements ActionListener, ItemListener {
    GirlsLists girlsLists = GirlsLists.getInstance();
    DefaultListModel free_girls_model = new DefaultListModel();
    DefaultListModel mechanic_girls_model = new DefaultListModel();
    DefaultListModel garbagers_model = new DefaultListModel();
    JList free_girls_list = new JList(free_girls_model);
    JList garbagers_list = new JList(garbagers_model);
    JList mechanics_list = new JList(mechanic_girls_model);

    public void createPopUpMenu(JButton manage, JLabel free_label, JLabel garbagers_label, Thread garbage_thread,
                                JLabel mechanic_label, Thread mechanic_thread){
        JPopupMenu menu = new JPopupMenu("Menu");
        JMenu hireSubmenu = new JMenu("Hire");
        JMenu hireGarbargers = new JMenu("to Garbagers");
        JMenuItem toWastelands = new JMenuItem("In wastelands");
        toWastelands.addActionListener(e -> {
            hireGirl("garbage_picker", garbagers_label, free_label,
                    "Garbagers: ", garbage_thread);

        });
        JMenuItem toGreenhouse = new JMenuItem("In greenhouse");
        //если нет теплицы, то функция не работает
        toGreenhouse.setEnabled(false);
        hireGarbargers.add(toWastelands);
        hireGarbargers.add(toGreenhouse);
        JMenu hireMechanics = new JMenu("to Mechanics");
        JMenuItem make_details = new JMenuItem("Making details");
        make_details.addActionListener(e -> {
            hireGirl("mechanic_gurlz", mechanic_label, free_label,
                    "Mechanics: ", mechanic_thread);

        });
        hireSubmenu.add(hireGarbargers);
        hireSubmenu.add(hireMechanics);
        JMenu dismissSubmenu = new JMenu("Dismiss");
        JMenuItem dismissGarbargers = new JMenuItem("from Garbagers");
        JMenuItem dismissMechanics = new JMenuItem("from Mechanics");
        dismissSubmenu.add(dismissGarbargers);
        dismissSubmenu.add(dismissMechanics);
        menu.add(hireSubmenu);
        menu.add(dismissSubmenu);
        menu.show(manage, manage.getWidth() / 2, manage.getHeight() / 2);
    }

    public JMenuBar createMenuBar() {

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



        });
        hiresubmenu.add(garbage_menuItem);

        mechanic_menuItem = new JMenuItem("Mechanics");
        mechanic_menuItem.getAccessibleContext().setAccessibleDescription("");
        mechanic_menuItem.addActionListener(k -> {

        });
        hiresubmenu.add(mechanic_menuItem);

        dismisssubmenu = new JMenu("Dismiss girl here");
        dismisssubmenu.setMnemonic(KeyEvent.VK_X);
        dismisssubmenu.addActionListener(this);
        managemenu.add(dismisssubmenu);

        garbage1_menuItem = new JMenuItem("Garbargers");
        garbage1_menuItem.getAccessibleContext().setAccessibleDescription("");
        garbage1_menuItem.addActionListener(b -> {
            /*String girl = null;
            try {
                girl = dismissGirl("C:/Users/User/Documents/NyanData/garbagers_girls_count",
                        "C:/Users/User/Documents/NyanData/garbagers_girls_names");
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkListPlz((ArrayList<String>) girlsLists.free_girls, free_girls_model);
            garbagers_model.removeElement(girl);
             */
        });
        dismisssubmenu.add(garbage1_menuItem);

        mechanic1_menuItem = new JMenuItem("Mechanics");
        mechanic1_menuItem.getAccessibleContext().setAccessibleDescription("");
        mechanic1_menuItem.addActionListener(l -> {
            /*String girl = null;
            try {
                girl = dismissGirl("C:/Users/User/Documents/NyanData/mechanic_girls_count",
                        "C:/Users/User/Documents/NyanData/mechanic_girls_names");
            } catch (IOException e) {
                e.printStackTrace();
            }
            checkListPlz("C:/Users/User/Documents/NyanData/free_girls_names", free_girls_model);
            mechanic_girls_model.removeElement(girl);
             */
        });
        dismisssubmenu.add(mechanic1_menuItem);

        return menuBar;
    }

    public void createManageMenu() {
        System.out.println("Безработных: " + girlsLists.free_girls.size());
        System.out.println("Старьевщиц: " + girlsLists.garbage_picker.size());
        System.out.println("Механиков: " + girlsLists.mechanic_gurlz.size());
        checkListPlz(girlsLists.free_girls, free_girls_model);
        checkListPlz(girlsLists.garbage_picker, garbagers_model);
        checkListPlz(girlsLists.mechanic_gurlz, mechanic_girls_model);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        JPanel pan1 = new JPanel(new GridLayout(3, 1));
        pan1.add(free_girls_list);
        JPanel pan2 = new JPanel(new GridLayout(3, 1));
        pan2.add(garbagers_list);
        JPanel pan3 = new JPanel(new GridLayout(3, 1));
        pan3.add(mechanics_list);
        JScrollPane scrollPane1 = new JScrollPane(pan1);
        JScrollPane scrollPane2 = new JScrollPane(pan2);
        JScrollPane scrollPane3 = new JScrollPane(pan3);
        tabbedPane.add("Free girls", scrollPane1);
        tabbedPane.add("Garbargers", scrollPane2);
        tabbedPane.add("Mechanics", scrollPane3);

        JFrame frame = new JFrame("Android Management Menu v.1337");
        frame.setJMenuBar(createMenuBar());
        frame.add(tabbedPane);

        frame.pack();
        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    private NewAndroid hireGirl(String job_name, JLabel worker_label, JLabel free_label, String worker,
                            Thread thread) {
        NewAndroid random_girl = randomName(girlsLists.free_girls);
        int free_girls_count = girlsLists.getGirlsList("free_girls").size();
        if (free_girls_count > 0) {
            girlsLists.free_girls.remove(random_girl);
            girlsLists.getGirlsList(job_name).add(random_girl);
            worker_label.setText(worker + girlsLists.getGirlsList(job_name).size());
            free_label.setText("Free: " + girlsLists.free_girls.size());
            if(!thread.isAlive()){
                thread.start();
            }
        }
        return random_girl;
    }

    private NewAndroid dismissGirl(String job_name, JLabel worker_label, JLabel free_label, String worker,
                               Thread thread){
        NewAndroid random_girl = randomName(girlsLists.getGirlsList(job_name));
        int job_count = girlsLists.getGirlsList(job_name).size();
        if (job_count > 0) {
            girlsLists.free_girls.remove(random_girl);
            girlsLists.getGirlsList(job_name).remove(random_girl);
            int size = girlsLists.getGirlsList(job_name).size();
            worker_label.setText(worker + size);
            free_label.setText("Free: " + girlsLists.free_girls.size());
            if (size < 1){
                thread.interrupt();
            }
        }
        return random_girl;
    }

    private NewAndroid randomName(ArrayList<NewAndroid> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    private void checkListPlz(ArrayList <NewAndroid> list, DefaultListModel model) {
        for (NewAndroid gurlz : list) {
            model.addElement(gurlz.getName());
            model.addElement(gurlz.getInfo());
            model.addElement(gurlz.getVersion());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JMenuItem jmi = (JMenuItem) e.getSource();
        if (jmi.getText().equalsIgnoreCase("close")) {
            System.exit(0);
        }

    }
}
