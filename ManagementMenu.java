import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;


public class ManagementMenu implements ActionListener, ItemListener {
    Lists lists = Lists.getInstance();
    Map<JPanel, NewAndroid> checkList = new HashMap<>();
    Map<JPanel, NewAndroid> all = new HashMap<>();
    JLayeredPane jDesktopPane;
    JButton manageButton;
    JMenuBar menuBar = new JMenuBar();
    Map<String, ObjectsWrapper> objects;
    JMenu hireMenu, dismissAutoMenu, dismissMenu;

    public ManagementMenu(Map<String, ObjectsWrapper> objects, JLayeredPane jDesktopPane, JButton manageButton) {
        this.objects = objects;
        this.jDesktopPane = jDesktopPane;
        this.manageButton = manageButton;
        jDesktopPane.add(createManageMenu(createMenuBar()), 2, 0);
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
                    resetPanels(getObj("free").getMainPanel(), girl);
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
                    item.addActionListener(this);
                    JMenuItem item1 = new JMenuItem(item_name);
                    item1.addActionListener(this);
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


    public JInternalFrame createManageMenu(JMenuBar jMenuBar) {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        for (Map.Entry<String, ObjectsWrapper> pair : objects.entrySet()){
            ObjectsWrapper wrapper = pair.getValue();
            wrapper.panelSet();
            JPanel mainPanel = wrapper.getMainPanel();
            addToMainPanel(lists.androids, pair.getKey(), mainPanel);
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            tabbedPane.add(scrollPane, wrapper.getWorkerText());
        }
        JInternalFrame jInternalFrame = new JInternalFrame("Android Management Menu v.1337",
                true,true, true, true);
        BufferedImage icon = null;
        try {
            icon = ImageIO.read(this.getClass().getResource("/manageIcon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jInternalFrame.addInternalFrameListener(new InternalFrameAdapter(){
            public void internalFrameClosing(InternalFrameEvent e) {
                manageButton.setEnabled(true);
            }
        });
        jInternalFrame.setFrameIcon(new ImageIcon(icon));
        jInternalFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jInternalFrame.setJMenuBar(jMenuBar);
        jInternalFrame.add(tabbedPane);
        jInternalFrame.toFront();
        jInternalFrame.pack();
        jInternalFrame.setSize(new Dimension(400, 300));
        jInternalFrame.setVisible(true);
        return jInternalFrame;
    }

    private void changeWorkStatus(String toJob, boolean isDismiss, NewAndroid girl) {
        String exWorkKey = girl.getJob();
        ObjectsWrapper objToJob = getObj(toJob);
        ObjectsWrapper objFromJob = getObj(exWorkKey);
        TimerWrapper wrapperTo = objToJob.getTimerWrapper();
        TimerWrapper wrapperFrom = objFromJob.getTimerWrapper();
        JLabel exwork = objFromJob.getAndroidWorker();
        String exworkText = objFromJob.getWorkerText();
        JLabel newWork = objToJob.getAndroidWorker();
        String newWorkText = objToJob.getWorkerText();
        girl.setJob(toJob);
        if (wrapperTo != null || wrapperFrom != null) {
            if (!isDismiss && !wrapperTo.getStatus()) {
                System.out.println("HIRE");
                wrapperTo.timerStart();
            }

            if (isDismiss && lists.getJobs(exWorkKey) < 1) {
                System.out.println(wrapperFrom.getStatus() + " прошел проверку на dismiss");
                System.out.println("DISMISS");
                wrapperFrom.timerStop();
                System.out.println(wrapperFrom.getStatus() + " после проверки на dismiss");
            }
        }

        exwork.setText(exworkText + lists.getJobs(exWorkKey));
        newWork.setText(newWorkText + lists.getJobs(toJob));
        //resetPanels()
    }


    private NewAndroid randomName(Map<JPanel, NewAndroid> map) {
        Object[] keys = map.keySet().toArray();
        Object key = keys[new Random().nextInt(keys.length)];
        return map.get(key);
    }

    private ObjectsWrapper getObj(String key) {
        System.out.println(key);
        ObjectsWrapper certain_object = objects.get(key);
        System.out.println("ПОЛУЧАЕМ ОБЪЕКТ " + certain_object);
        return certain_object;
    }

    private void addToMainPanel(ArrayList<NewAndroid> list, String job, JPanel mainPanel) {
        mainPanel.setLayout(new MigLayout("fillx, insets 0, gapy 0, flowy, toptobottom"));
        for (NewAndroid girl : list) {
            if (girl.getJob().equals(job)) {
                JPanel panel = new JPanel();
                String[] data = {girl.getName(), girl.getInfo(), girl.getVersion()};
                panel.setLayout(new MigLayout("fill, debug"));
                panel.setBackground(Color.WHITE);
                ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(
                        "/image" + girl.getIconId() + ".png")));
                JLabel iconLabel = new JLabel(icon);
                panel.add(iconLabel, "growx");
                for (String s : data) {
                    JLabel label = new JLabel(s);
                    label.setFont(new Font("Serif", Font.BOLD, 18));
                    panel.add(label, "split 3, flowy, growx");
                }
                mainPanel.add(panel, "growx");
                getCheckBox(panel, girl);
                all.put(panel, girl);
            }

        }
        mainPanel.setBackground(Color.LIGHT_GRAY);
    }

    private void getCheckBox(JPanel panel, NewAndroid girl) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
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
        panel.add(checkBox, "center, growx");
    }

    private void resetPanels(JPanel toMainPanel, NewAndroid randomGirl) {
        Map<JPanel, NewAndroid> map;
        if (randomGirl != null) {
            map = all;
        } else {
            map = checkList;
        }
        for (Map.Entry<JPanel, NewAndroid> pair : map.entrySet()) {
            if (map == all && pair.getValue() != randomGirl) {
                continue;
            }
            JPanel child = pair.getKey();
            JPanel parent = (JPanel) child.getParent();
            parent.remove(child);
            parent.revalidate();
            parent.repaint();
            toMainPanel.add(child, "growx");
        }
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
                resetPanels(getObj(toJob).getMainPanel(), girl);
                break;
            case "Dismiss 1 from":
                System.out.println("Увольняем рандомом");
                girl = randomName(all);
                changeWorkStatus("free", true, girl);
                resetPanels(getObj("free").getMainPanel(), girl);
                break;
            case "Hire selected":
                for (Map.Entry<JPanel, NewAndroid> pair : checkList.entrySet()) {
                    girl = pair.getValue();
                    changeWorkStatus(toJob, false, girl);
                    resetPanels(getObj(toJob).getMainPanel(), girl);
                    System.out.println("Убираем ручками");
                }
                break;
        }
    }
}
