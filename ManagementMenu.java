import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ManagementMenu implements ActionListener, ItemListener {
    GirlsLists girlsLists = GirlsLists.getInstance();
    Map<JPanel, NewAndroid> checkList = new HashMap<>();
    Map<JPanel, NewAndroid> all = new HashMap<>();
    JMenuBar menuBar = new JMenuBar();
    JMenu managemenu, managemenu1;
    JPanel free_panel = new JPanel();
    JPanel garbage_panel = new JPanel();
    JPanel mechanic_panel = new JPanel();
    JLabel free_label, garbage_label, mechanic_label;
    TimerWrapper garbage_wrapper;

    public ManagementMenu(JLabel free_label, JLabel garbage_label, TimerWrapper garbage_wrapper) {
        this.free_label = free_label;
        this.garbage_label = garbage_label;
        this.mechanic_label = mechanic_label;
        this.garbage_wrapper = garbage_wrapper;
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
                    String subString = girl.getJob();
                    System.out.println(subString);
                    changeWorkStatus(subString, "free_girls",
                            getObject(subString, "_label"), free_label, subString,
                            "Free: ", garbage_wrapper, false, true);
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

        JMenu hireSubmenu = new JMenu("Hire");
        JMenu hireGarbargers = new JMenu("to Garbagers");
        JMenuItem toWastelands = new JMenuItem("In wastelands");
        toWastelands.addActionListener(e -> {
            //ТОЛЬКО ИЗ FREE, если нет никого, то setEnabled (false)
            System.out.println("Нанимаем рандомом");
            changeWorkStatus("free_girls", "garbage_girls", free_label, garbage_label,
                    "Free: ", "Garbagers: ", garbage_wrapper, true, false);
            resetPanels(garbage_panel, true);
        });
        JMenuItem toGreenhouse = new JMenuItem("In greenhouse");
        //если нет теплицы, то функция не работает
        toGreenhouse.setEnabled(false);
        hireGarbargers.add(toWastelands);
        hireGarbargers.add(toGreenhouse);
        JMenu hireMechanics = new JMenu("to Mechanics");
        JMenuItem make_details = new JMenuItem("Making details");
        make_details.addActionListener(e -> {
            //changeWorkStatus("mechanic_gurlz", mechanic_label, free_label,
            //"Mechanics: ", mechanic_thread, true);

        });
        hireSubmenu.add(hireGarbargers);
        hireSubmenu.add(hireMechanics);
        JMenu dismissSubmenu = new JMenu("Dismiss");
        JMenuItem dismissGarbargers = new JMenuItem("from Garbagers");
        dismissGarbargers.addActionListener(e -> {

        });
        JMenuItem dismissMechanics = new JMenuItem("from Mechanics");
        dismissSubmenu.add(dismissGarbargers);
        dismissSubmenu.add(dismissMechanics);
        managemenu.add(hireSubmenu);
        managemenu.add(dismissSubmenu);

        return menuBar;
    }

    public Frame createManageMenu(JMenuBar jMenuBar) {
        addToMainPanel(girlsLists.free_girls, free_panel);
        addToMainPanel(girlsLists.garbage_girls, garbage_panel);
        addToMainPanel(girlsLists.mechanic_girls, mechanic_panel);
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        JFrame frame = new JFrame("Android Management Menu v.1337");

        JScrollPane scrollPane1 = new JScrollPane(free_panel);
        JScrollPane scrollPane2 = new JScrollPane(garbage_panel);
        JScrollPane scrollPane3 = new JScrollPane(mechanic_panel);

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

    private void changeWorkStatus(String fromJob, String toJob, JLabel exwork, JLabel now_work, String exworker,
                                  String now_worker, TimerWrapper wrapper, boolean isAuto, boolean isDismiss) {
        int girlsSize = girlsLists.getGirlsList(fromJob).size();
        if (girlsSize > 0) {
            if (isAuto) {
                NewAndroid random_girl = randomName(all);
                String job_name = random_girl.getJob();
                System.out.println(random_girl.getJob() + " ТТУУУУУУТ");
                girlsLists.getGirlsList(job_name).remove(random_girl);
                girlsLists.setGirlsList(toJob, random_girl);
                random_girl.setJob(toJob);
            } else {
                for (Map.Entry<JPanel, NewAndroid> pair : checkList.entrySet()) {
                    NewAndroid girl = pair.getValue();
                    girlsLists.getGirlsList(fromJob).remove(girl);
                    girlsLists.getGirlsList(toJob).add(girl);
                    girl.setJob(toJob);
                }
            }
            System.out.println(wrapper.getStatus() + " перед проверкой на hire");
            if (!isDismiss && !wrapper.getStatus()) {

                System.out.println("HIRE");
                wrapper.timerStart();
            }
        }
        exwork.setText(exworker + girlsLists.getGirlsList(fromJob).size());
        now_work.setText(now_worker + girlsLists.getGirlsList(toJob).size());
        System.out.println(wrapper.getStatus() + " перед проверкой на dismiss");
        if (isDismiss && girlsSize < 1) {
            System.out.println(wrapper.getStatus() + " прошел проверку на dismiss");
            System.out.println("DISMISS");
            wrapper.timerStop();
            System.out.println(wrapper.getStatus() + " после проверки на dismiss");
        }
    }

    private NewAndroid randomName(Map<JPanel, NewAndroid> map) {
        Object[] keys = map.keySet().toArray();
        Object key = keys[new Random().nextInt(keys.length)];
        return map.get(key);
    }

    private JLabel getObject(String subString, String endString) {
        Pattern pattern = Pattern.compile(".+?(?=_)");
        Matcher matcher = pattern.matcher(subString);
        if (matcher.find()) {
            subString = matcher.group();
        }

        String name;
        JLabel label = null;
        List<String> names = Arrays.asList("free_label", "garbage_label", "mechanic_label");
        List<JLabel> objects = Arrays.asList(free_label, garbage_label, mechanic_label);
        int i = 0;
        for (String n : names) {
            name = n;
            System.out.println(name);
            if (name.equals(subString + endString)) {
                System.out.println("СРАБОТАЛО");
                label = objects.get(i);
                break;
            }
            i++;
        }

        return label;
    }

    private void addToMainPanel(ArrayList<NewAndroid> list, JPanel mainPanel) {
        mainPanel.setLayout(getLayout());
        for (NewAndroid girl : list) {
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
