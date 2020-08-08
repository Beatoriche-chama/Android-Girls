import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;


public class ManagementMenu implements ActionListener, ItemListener {
    GirlsLists girlsLists = GirlsLists.getInstance();
    Map<JPanel, NewAndroid> checkList = new HashMap<>();
    Map<JPanel, NewAndroid> all = new HashMap<>();
    JMenuBar menuBar = new JMenuBar();
    JPanel free_panel = new JPanel();
    JPanel garbagers_panel = new JPanel();
    JPanel mechanics_panel = new JPanel();
    JLabel free_label, garbagers_label, mechanics_label;
    TimerWrapper garbagers_wrapper;
    JMenu hireMenu, dismissAutoMenu, dismissMenu;

    public ManagementMenu(JLabel free_label, JLabel garbage_label, TimerWrapper garbage_wrapper) {
        this.free_label = free_label;
        this.garbagers_label = garbage_label;
        this.mechanics_label = mechanics_label;
        this.garbagers_wrapper = garbage_wrapper;
    }

    public JMenuBar createMenuBar() {
        //сделать только три постоянных меню: dismiss, hire, upgrade (при чеке + "selected")
        //меню select появляется только при чеке
        //РАНДОМНЫЙ HIRE ТОЛЬКО ИЗ FREE, если нет никого, то setEnabled (false)
        /*
        if (girlsLists.getJobs("free") == 0) {
                    hireMenu.setEnabled(false);
                }
         */
        hireMenu = new JMenu("Hire 1 to");
        dismissAutoMenu = new JMenu("Dismiss 1 from");
        dismissMenu = new JMenu("Dismiss Selected");
        dismissMenu.setVisible(false);
        menuBar.add(hireMenu);
        menuBar.add(dismissAutoMenu);
        menuBar.add(dismissMenu);

        dismissMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                for (Map.Entry<JPanel, NewAndroid> pair : checkList.entrySet()) {
                    NewAndroid girl = pair.getValue();
                    changeWorkStatus("free", true, girl);
                    resetPanels(free_panel, null);
                    System.out.println("Убираем ручками");
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        String[] subMenu = {"garbagers", "gardeners", "mechanics", "alchemists", "priests"};
        //для каждого subMenu свои menuItem (ПОМЕНЯТЬ)
        //сделать отдельные 4 menu (для selected-чекнутых не нужны JItem)
        Map<String, Integer> menuItemMap = new HashMap<>();
        menuItemMap.put("garbagers", 1);
        menuItemMap.put("in wastelands", 2);
        menuItemMap.put("in greenhouse", 2);
        menuItemMap.put("make details", 3);
        int i = 1;
        for (String s : subMenu) {
            JMenu menu = new JMenu(s);
            JMenu menu1 = new JMenu(s);
            String item_name;
            for (Map.Entry<String, Integer> pair : menuItemMap.entrySet()) {
                if (pair.getValue() == i) {
                    item_name = pair.getKey();
                    JMenuItem item = new JMenuItem(item_name);
                    item.addActionListener(this::actionPerformed);
                    JMenuItem item1 = new JMenuItem(item_name);
                    item1.addActionListener(this::actionPerformed);
                    menu.add(item);
                    menu1.add(item1);
                }
                dismissAutoMenu.add(menu);
                hireMenu.add(menu1);
            }
            i++;
        }
        return menuBar;
    }


    public Frame createManageMenu(JMenuBar jMenuBar) {
        Map<JPanel, String> map = new HashMap<>();
        map.put(free_panel, "free");
        map.put(garbagers_panel, "garbage");
        map.put(mechanics_panel, "mechanic");
        for (Map.Entry<JPanel, String> pair : map.entrySet()) {
            addToMainPanel(girlsLists.androids, pair.getValue(), pair.getKey());
        }

        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        JFrame frame = new JFrame("Android Management Menu v.1337");

        JScrollPane scrollPane1 = new JScrollPane(free_panel);
        JScrollPane scrollPane2 = new JScrollPane(garbagers_panel);
        JScrollPane scrollPane3 = new JScrollPane(mechanics_panel);

        tabbedPane.add(scrollPane1, "Free girls");
        tabbedPane.add("Garbargers", scrollPane2);
        tabbedPane.add("Mechanics", scrollPane3);

        frame.setJMenuBar(jMenuBar);
        frame.add(tabbedPane);

        frame.pack();
        frame.setSize(400, 300);
        frame.setVisible(true);
        return frame;
    }

    private void changeWorkStatus(String toJob, boolean isDismiss, NewAndroid girl) {
        String fromJob = girl.getJob();
        System.out.println(fromJob);
        String keyWord = fromJob;
        TimerWrapper wrapperTo = (TimerWrapper) getObj(toJob, "_wrapper");
        TimerWrapper wrapperFrom = (TimerWrapper) getObj(fromJob, "_wrapper");
        JLabel exwork = (JLabel) getObj(keyWord, "_label");
        String exworker = (String) getObj(keyWord, null);
        String now_worker = (String) getObj(toJob, null);
        JLabel now_work = (JLabel) getObj(toJob, "_label");

        int girlsSize = girlsLists.getJobs(fromJob);
        if (girlsSize > 0) {
            girl.setJob(toJob);

            if (wrapperTo != null || wrapperFrom != null) {
                if (!isDismiss && !wrapperTo.getStatus()) {
                    System.out.println("HIRE");
                    wrapperTo.timerStart();
                }

                if (isDismiss && girlsLists.getJobs(fromJob) < 1) {
                    System.out.println(wrapperFrom.getStatus() + " прошел проверку на dismiss");
                    System.out.println("DISMISS");
                    wrapperFrom.timerStop();
                    System.out.println(wrapperFrom.getStatus() + " после проверки на dismiss");
                }
            }
        }
        exwork.setText(exworker + girlsLists.getJobs(fromJob));
        now_work.setText(now_worker + girlsLists.getJobs(toJob));
    }

    private NewAndroid randomName(Map<JPanel, NewAndroid> map) {
        Object[] keys = map.keySet().toArray();
        Object key = keys[new Random().nextInt(keys.length)];
        return map.get(key);
    }

    private Object getObj(String obj_name, String class_name) {
        System.out.println(obj_name);
        Object certain_object = null;
        List<String> var_names = Arrays.asList("free_label", "garbagers_label", "mechanics_label",
                "garbagers_wrapper", "free_panel", "garbagers_panel");
        List<Object> objects = Arrays.asList(free_label, garbagers_label, mechanics_label,
                garbagers_wrapper, free_panel, garbagers_panel);
        List<String> texts = Arrays.asList("Free: ", "Garbagers: ", "Mechanics: ", "Alchemists: ", "Flower pickers: ");
        int i = 0;
        if (obj_name != null && class_name == null) {
            for (String s : texts) {
                if (s.regionMatches(true, 0, obj_name, 0, 3)) {
                    certain_object = s;
                }
            }
        } else {
            for (String s : var_names) {
                if (s.equals(obj_name + class_name)) {
                    certain_object = objects.get(i);
                    System.out.println(s);
                }
                i++;
            }
        }
        System.out.println("ПОЛУЧАЕМ ОБЪЕКТ " + certain_object);
        return certain_object;
    }

    private void addToMainPanel(ArrayList<NewAndroid> list, String job, JPanel mainPanel) {
        mainPanel.setLayout(getLayout());
        for (NewAndroid girl : list) {
            if (girl.getJob().equals(job)) {
                JPanel panel = new JPanel();
                String[] data = {girl.getName(), girl.getInfo(), girl.getVersion()};
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                panel.setBackground(Color.WHITE);
                for (String s : data) {
                    JLabel label = new JLabel(s);
                    panel.add(label);
                }
                mainPanel.add(panel, getCons());
                getCheckBox(panel, girl);
                all.put(panel, girl);
            }

        }
        mainPanel.setBackground(Color.LIGHT_GRAY);
    }

    private void getCheckBox(JPanel panel, NewAndroid girl) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                checkList.put(panel, girl);
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                checkList.remove(panel, girl);
            }

            if (checkList.isEmpty()) {
                hireMenu.setText("Hire 1 to");
                dismissMenu.setVisible(false);
                dismissAutoMenu.setVisible(true);
            } else {
                hireMenu.setText("Hire selected");
                dismissMenu.setVisible(true);
                dismissAutoMenu.setVisible(false);
            }
            System.out.println(checkList.size());

        });
        panel.add(checkBox);
    }

    private void resetPanels(JPanel toMainPanel, NewAndroid random_girl) {
        Map<JPanel, NewAndroid> map;
        if (random_girl != null) {
            map = all;
        } else {
            map = checkList;
        }
        for (Map.Entry<JPanel, NewAndroid> pair : map.entrySet()) {
            if (map == all && pair.getValue() != random_girl) {
                continue;
            }
            JPanel child = pair.getKey();
            JPanel parent = (JPanel) child.getParent();
            parent.remove(child);
            parent.revalidate();
            parent.repaint();
            toMainPanel.add(child, getCons());
        }
    }

    private GridBagConstraints getCons() {
        GridBagConstraints cons = new GridBagConstraints();
        cons.weightx = 1;
        cons.anchor = GridBagConstraints.NORTHWEST;
        cons.gridwidth = GridBagConstraints.REMAINDER;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.insets = new Insets(0, 0, 0, 0);
        return cons;
    }

    private GridBagLayout getLayout() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
        gridBagLayout.rowWeights = new double[]{0.0, 0.0, 1.0, 1.0};
        gridBagLayout.columnWidths = new int[]{0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[]{0.0, 0.0, 1.0, 1.0};
        return gridBagLayout;
    }

    private JMenu getMenuBarMenu(JMenuItem item) {
        JMenuItem menu = null;

        while (menu == null) {
            JPopupMenu popup = (JPopupMenu) item.getParent();
            item = (JMenuItem) popup.getInvoker();

            if (item.getParent() instanceof JMenuBar)
                menu = item;
        }

        return (JMenu) menu;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem jmi = (JMenuItem) e.getSource();
        JMenu mainMenu = getMenuBarMenu(jmi);
        String toJob = jmi.getText();
        NewAndroid girl;
        switch (mainMenu.getText()) {
            case "Hire 1 to":
                System.out.println("Нанимаем рандомом");
                girl = randomName(all);
                changeWorkStatus(toJob, false, girl);
                resetPanels((JPanel) getObj(toJob, "_panel"), girl);
                break;
            case "Dismiss 1 from":
                System.out.println("Увольняем рандомом");
                girl = randomName(all);
                changeWorkStatus("free", true, girl);
                resetPanels(free_panel, girl);
                break;
            case "Hire selected":
                for (Map.Entry<JPanel, NewAndroid> pair : checkList.entrySet()) {
                    girl = pair.getValue();
                    changeWorkStatus(toJob, false, girl);
                    resetPanels((JPanel) getObj(toJob, "_panel"), girl);
                    System.out.println("Убираем ручками");
                }
                break;
        }
    }
}
