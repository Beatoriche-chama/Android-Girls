import javafx.util.Pair;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;


public class ManagementMenu implements ActionListener {
    Lists lists = Lists.getInstance();
    Map<NewAndroid, JPanel> checkList, all;
    List<JMenu> jMenus;
    JLayeredPane jDesktopPane;
    JButton manageButton;
    JMenuBar menuBar;
    Map<String, ObjectsWrapper> objects;
    JMenu hireAutoMenu, dismissAutoMenu, hireMenu, dismissMenu;

    public ManagementMenu(Map<String, ObjectsWrapper> objects, JLayeredPane jDesktopPane, JButton manageButton) {
        this.objects = objects;
        this.jDesktopPane = jDesktopPane;
        this.manageButton = manageButton;
        all = new HashMap<>();
        checkList = new HashMap<>();
        jDesktopPane.add(createManageMenu(createMenuBar()), JLayeredPane.PALETTE_LAYER);
    }

    public JMenuBar createMenuBar() {
        //сделать только три постоянных меню: dismiss, hire, upgrade (при чеке + "selected")
        //меню select появляется только при чеке
        menuBar = new JMenuBar();
        hireAutoMenu = new JMenu("Hire 1 to");
        hireMenu = new JMenu("Hire selected");
        dismissAutoMenu = new JMenu("Dismiss 1 from");
        dismissMenu = new JMenu("Dismiss Selected");
        dismissMenu.setVisible(false);
        hireMenu.setVisible(false);
        dismissMenu.addMenuListener(new MenuListener() {

            @Override
            public void menuSelected(MenuEvent e) {
                checkList.forEach((key, value) -> changeWorkStatus("free", true, key, value));
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        List<String> subMenuList = Arrays.asList("garbagers", "gardeners",
                "mechanics", "alchemists", "priests");
        jMenus = Arrays.asList(dismissAutoMenu, hireAutoMenu, hireMenu);
        Map<String, Integer> menuItemMap = new HashMap<>();
        menuItemMap.put("garbagers", 0);
        menuItemMap.put("pick cpu", 0);
        menuItemMap.put("in wastelands", 1);
        menuItemMap.put("in greenhouse", 1);
        menuItemMap.put("make details", 2);

        jMenus.forEach(jMenu -> {
            menuBar.add(jMenu);
            subMenuList.forEach(subMenu -> {
                JMenu menu = new JMenu(subMenu);
                menuItemMap.forEach((key, value) -> {
                    if (value == subMenuList.indexOf(subMenu)) {
                        JMenuItem jMenuItem = new JMenuItem(key);
                        jMenuItem.addActionListener(this);
                        menu.add(jMenuItem);
                    }
                });
                jMenu.add(menu);
            });
        });
        menuBar.add(dismissMenu);
        return menuBar;
    }


    public JInternalFrame createManageMenu(JMenuBar jMenuBar) {
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        objects.forEach((key, wrapper)->{
            wrapper.panelSet();
            JPanel mainPanel = wrapper.getMainPanel();
            addToMainPanel(lists.androids, key, mainPanel);
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            tabbedPane.add(scrollPane, wrapper.getWorkerText());
        });

        JInternalFrame jInternalFrame = new JInternalFrame("Android Management Menu v.1337",
                true, true, true, true);
        BufferedImage icon = null;
        try {
            icon = ImageIO.read(this.getClass().getResource("/frameIcon.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        jInternalFrame.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosing(InternalFrameEvent e) {
                manageButton.setEnabled(true);
            }
        });
        jInternalFrame.setFrameIcon(new ImageIcon(icon));
        jInternalFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jInternalFrame.setJMenuBar(jMenuBar);
        jInternalFrame.add(tabbedPane);
        jInternalFrame.pack();
        jInternalFrame.setSize(new Dimension(400, 400));
        jInternalFrame.setVisible(true);
        return jInternalFrame;
    }

    private void changeWorkStatus(String toJob, boolean isDismiss, NewAndroid girl, JPanel panel) {
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
                wrapperTo.timerStart();
            }

            if (isDismiss && lists.getJobs(exWorkKey) < 1) {
                wrapperFrom.timerStop();
            }
        }
        hireAutoMenu.setEnabled(lists.getJobs("free") != 0);
        exwork.setText(exworkText + lists.getJobs(exWorkKey));
        newWork.setText(newWorkText + lists.getJobs(toJob));
        resetPanels(getObj(toJob).getMainPanel(), panel);
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
                panel.setLayout(new MigLayout("fill"));
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
                all.put(girl, panel);
            }

        }
        mainPanel.setBackground(Color.LIGHT_GRAY);
    }

    private void getCheckBox(JPanel panel, NewAndroid girl) {
        JCheckBox checkBox = new JCheckBox();
        checkBox.setOpaque(false);
        checkBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                checkList.put(girl, panel);
            }
            if (e.getStateChange() == ItemEvent.DESELECTED) {
                checkList.remove(girl, panel);
            }

            if (checkList.isEmpty()) {
                hireMenu.setVisible(false);
                hireAutoMenu.setVisible(true);
                dismissMenu.setVisible(false);
                dismissAutoMenu.setVisible(true);
            } else {
                hireMenu.setVisible(true);
                dismissMenu.setVisible(true);
                hireAutoMenu.setVisible(false);
                dismissAutoMenu.setVisible(false);
            }
            System.out.println(checkList.size());

        });
        panel.add(checkBox, "center, growx");
    }

    private Pair<NewAndroid, JPanel> randomName() {
        NewAndroid randomGirl = lists.androids.get(new Random().nextInt(lists.androids.size()));
        return new Pair<>(randomGirl, all.get(randomGirl));
    }

    private void resetPanels(JPanel toMainPanel, JPanel panelWithGirl) {
        JPanel parent = (JPanel) panelWithGirl.getParent();
        parent.remove(panelWithGirl);
        parent.revalidate();
        parent.repaint();
        toMainPanel.add(panelWithGirl, "growx");
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
    public void actionPerformed(ActionEvent e) {
        JMenuItem jmi = (JMenuItem) e.getSource();
        JMenu mainMenu = getMenuBarMenu(jmi);
        String toJob = jmi.getText();
        String menuText = mainMenu.getText();
        boolean isDismiss;
        if (menuText.contains("Hire")) {
            isDismiss = false;
        } else {
            isDismiss = true;
            toJob = "free";
        }

        if (menuText.contains("1")) {
            Pair<NewAndroid, JPanel> pair = randomName();
            changeWorkStatus(toJob, isDismiss, pair.getKey(), pair.getValue());
        } else {
            String finalToJob = toJob;
            checkList.forEach((key, value) -> changeWorkStatus(finalToJob, isDismiss, key, value));
        }
    }
}
