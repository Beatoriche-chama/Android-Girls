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
    JMenu managemenu, managemenu1;
    JPanel free_panel = new JPanel();
    JPanel garbagers_panel = new JPanel();
    JPanel mechanics_panel = new JPanel();
    JLabel free_label, garbagers_label, mechanics_label;
    TimerWrapper garbagers_wrapper;
    JMenu hireSubmenu;

    public ManagementMenu(JLabel free_label, JLabel garbage_label, TimerWrapper garbage_wrapper) {
        this.free_label = free_label;
        this.garbagers_label = garbage_label;
        this.mechanics_label = mechanics_label;
        this.garbagers_wrapper = garbage_wrapper;
    }

    public JMenuBar createMenuBar() {

        managemenu = new JMenu("Auto job manage");
        managemenu.setMnemonic(KeyEvent.VK_R);
        managemenu.getAccessibleContext().setAccessibleDescription("");
        menuBar.add(managemenu);

        managemenu1 = new JMenu("Dismiss");
        managemenu1.setMnemonic(KeyEvent.VK_D);
        managemenu1.getAccessibleContext().setAccessibleDescription("");
        menuBar.add(managemenu1);
        managemenu1.setVisible(false);
        managemenu1.addMenuListener(new MenuListener() {
            public void menuSelected(MenuEvent e) {
                for (Map.Entry<JPanel, NewAndroid> pair : checkList.entrySet()) {
                    NewAndroid girl = pair.getValue();
                    changeWorkStatus("free", true, girl);
                    resetPanels(free_panel, false);
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

        ActionListener hireListener = e -> {

        };

        ActionListener dismissListener = e -> {

        };

        hireSubmenu = new JMenu("Hire to");
        JMenu hireGarbargers = new JMenu("garbagers");
        JMenuItem toWastelands = new JMenuItem("in wastelands");
        toWastelands.getAccessibleContext().setAccessibleDescription("");
        toWastelands.addActionListener(e -> {
            //ТОЛЬКО ИЗ FREE, если нет никого, то setEnabled (false)
            System.out.println("Нанимаем рандомом");
            NewAndroid random_girl = randomName(all);
            String toJob = hireGarbargers.getText();
            changeWorkStatus(toJob, false, random_girl);
            resetPanels(garbagers_panel, true);
        });
        JMenuItem toGreenhouse = new JMenuItem("in greenhouse");
        //если нет теплицы, то функция не работает
        toGreenhouse.setEnabled(false);
        hireGarbargers.add(toWastelands);
        hireGarbargers.add(toGreenhouse);
        JMenu hireMechanics = new JMenu("mechanics");
        JMenuItem make_details = new JMenuItem("Making details");
        make_details.addActionListener(e -> {

        });
        hireSubmenu.add(hireGarbargers);
        hireSubmenu.add(hireMechanics);
        JMenu dismissSubmenu = new JMenu("Dismiss from");
        JMenuItem dismissGarbargers = new JMenuItem("garbagers");
        dismissGarbargers.addActionListener(e -> {

        });
        JMenuItem dismissMechanics = new JMenuItem("mechanics");
        dismissSubmenu.add(dismissGarbargers);
        dismissSubmenu.add(dismissMechanics);
        managemenu.add(hireSubmenu);
        managemenu.add(dismissSubmenu);

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
                "garbagers_wrapper");
        List<Object> objects = Arrays.asList(free_label, garbagers_label, mechanics_label,
                garbagers_wrapper);
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
                managemenu1.setVisible(false);
                managemenu.setVisible(true);
                if (girlsLists.getJobs("free") == 0) {
                    hireSubmenu.setEnabled(false);
                }

            } else {
                managemenu1.setVisible(true);
                managemenu.setVisible(false);
            }
            System.out.println(checkList.size());

        });
        panel.add(checkBox);
    }

    private void resetPanels(JPanel toMainPanel, boolean isAuto) {
        Map<JPanel, NewAndroid> map;
        if (isAuto) {
            map = all;
        } else {
            map = checkList;
        }
        for (Map.Entry<JPanel, NewAndroid> pair : map.entrySet()) {
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
