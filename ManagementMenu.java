import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
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
    List<JobWrapper> objects;
    JMenu hireAutoMenu, dismissAutoMenu, hireMenu, dismissMenu;

    public ManagementMenu(List<JobWrapper> objects, JLayeredPane jDesktopPane, JButton manageButton) {
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
                checkList.forEach((key, value) -> changeWorkStatus("free", key, value));
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
        menuItemMap.put("gardeners", 1);
        menuItemMap.put("in wastelands", 1);
        menuItemMap.put("in greenhouse", 1);
        menuItemMap.put("motherboards", 2);
        menuItemMap.put("cpu", 2);

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
        objects.forEach((wrapper) -> {
            JPanel mainPanel = addToMainPanel(lists.androids, wrapper);
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

    private void changeWorkStatus(String toJob, NewAndroid girl, JPanel panel) {
        String exWorkKey = girl.getJob();
        JobWrapper objToJob = getObj(toJob);
        JobWrapper objFromJob = getObj(exWorkKey);
        String newWorkKey = objToJob.getKey();

        TimerWrapper wrapperTo = objToJob.getTimerWrapper();
        TimerWrapper wrapperFrom = objFromJob.getTimerWrapper();
        JLabel exwork = objFromJob.getAndroidWorker();
        String exworkText = objFromJob.getWorkerText();
        JLabel newWork = objToJob.getAndroidWorker();
        String newWorkText = objToJob.getWorkerText();

        girl.setJob(newWorkKey);

        if ((objToJob.getIsRecyclist() | objFromJob.getIsRecyclist())) {

            boolean isNotTheSame = girl.getTask() != null && !girl.getTask().equals(toJob);
            System.out.println(isNotTheSame);
            //
                if (objFromJob.getIsRecyclist()) {
                    if(lists.getWorkersNumber(girl.getTask(), true) == 1
                            && isNotTheSame){
                        System.out.println("НИКОГО НЕ ОСТАНЕТСЯ, УБЕРЕМ РЕСУРС!");
                        objFromJob.removeResource(girl.getTask());
                    }
                    girl.setTask(null);
                    resetPanels(getObj(newWorkKey).getMainPanel(), panel);
                }

                if (objToJob.getIsRecyclist()) {
                    System.out.println("Добавим задачу андроиду");
                    System.out.println("А задач-работников вот столько! " +
                            lists.getWorkersNumber(toJob, true));

                    girl.setTask(toJob);

                    if(lists.getWorkersNumber(toJob, true) == 1
                            && (isNotTheSame | !objFromJob.getIsRecyclist())){

                        System.out.println("НИКОГО НЕТ, ДОБАВИМ РЕСУРС!");
                        objToJob.addResource(toJob);
                    }
                    resetPanels(lists.getRes(toJob).getProductPanel(), panel);
                }

        } else{
            resetPanels(getObj(newWorkKey).getMainPanel(), panel);
        }

        if (wrapperTo != null && !wrapperTo.getStatus()) {
            System.out.println("Началась работа " + newWorkKey +
                    " с ресурсами на сбор/переработку: " + objToJob.getResourceKey());
            wrapperTo.timerStart();
        }

        if (wrapperFrom != null && wrapperFrom.getStatus() && lists.getWorkersNumber(exWorkKey, false) < 1) {
            System.out.println("Завершена работа " + exWorkKey);
            wrapperFrom.timerStop();
        }

        hireAutoMenu.setEnabled(lists.getWorkersNumber("free", false) != 0);
        exwork.setText(exworkText + lists.getWorkersNumber(exWorkKey, false));
        newWork.setText(newWorkText + lists.getWorkersNumber(newWorkKey, false));

    }

    private JobWrapper getObj(String key) {
        System.out.println(key);
        JobWrapper object = null;
        for (JobWrapper obj : objects) {
            if (obj.getKey().equals(key) |
                    (obj.getProductsList() != null && obj.getProductsList().contains(key))) {
                object = obj;
            }
        }
        return object;
    }

    private JPanel addToMainPanel(ArrayList<NewAndroid> list, JobWrapper wrapper) {
        JPanel mainPanel = wrapper.getMainPanel();
        MigLayout layout = new MigLayout("fillx, insets 0, gapy 0, flowy, toptobottom");
        mainPanel.setLayout(layout);
        if (wrapper.getProductsList() != null) {
            String[] productList = wrapper.getProductsList().split(" ");
            for (String productName : productList) {
                System.out.println(productName + " PRODUCT NAME");
                Border taskBorder = BorderFactory.createTitledBorder(productName.toUpperCase());
                JPanel productPanel = lists.getRes(productName).getProductPanel();
                //если перестроить под JList, то у каждого
                //продукта есть свой Jlist с girlPanels
                //jlist на панели


                productPanel.setBorder(taskBorder);
                productPanel.setLayout(new MigLayout("fillx, insets 0, gapy 0, flowy, toptobottom"));
                createHideShowButton(productPanel);
                for (NewAndroid girl : list) {
                    if (girl.getTask() != null && girl.getTask().equals(productName)) {
                        System.out.println(girl.getName() + " GIRL NAME " +
                                girl.getInfo() + " GIRL INFO");
                        addGirlPanel(girl, productPanel);
                    }
                }
                mainPanel.add(productPanel, "grow");
            }
        } else {
            for (NewAndroid girl : list) {
                if (girl.getJob().equals(wrapper.getKey())) {
                    addGirlPanel(girl, mainPanel);
                }
            }
        }
        mainPanel.setBackground(Color.LIGHT_GRAY);
        return mainPanel;
    }

    private void createHideShowButton(JPanel panel){
        JButton hideShow = new JButton("Hide content");
        final boolean[] isVisible = {true};
        hideShow.addActionListener(e -> {
            if (hideShow.getText().equals("Show content")) {
                hideShow.setText("Hide content");
            } else {
                hideShow.setText("Show content");
            }
            isVisible[0] = !isVisible[0];
            Component[] components = panel.getComponents();
            for (Component component : components) {
                if (component instanceof JButton) continue;
                component.setVisible(isVisible[0]);
            }
        });
        panel.add(hideShow);
    }

    private void addGirlPanel(NewAndroid girl, JPanel parentPanel) {
        //переделать под jlist

        JPanel girlPanel = new JPanel();
        String[] data = {girl.getName(), girl.getInfo(), girl.getVersion()};
        girlPanel.setLayout(new MigLayout("fill"));
        girlPanel.setBackground(Color.WHITE);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        girlPanel.setBorder(blackline);
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(
                "/image" + girl.getIconId() + ".png")));
        JLabel iconLabel = new JLabel(icon);
        girlPanel.add(iconLabel, "growx");
        for (String s : data) {
            JLabel label = new JLabel(s);
            label.setFont(new Font("Serif", Font.BOLD, 18));
            girlPanel.add(label, "split 3, flowy, growx");
        }
        getCheckBox(girlPanel, girl);
        all.put(girl, girlPanel);
        parentPanel.add(girlPanel, "grow, hidemode 1");
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

        });
        panel.add(checkBox, "center, growx");
    }

    private Pair<NewAndroid, JPanel> randomName(String job) {
        NewAndroid randomGirl = null;
        for (NewAndroid girl : lists.androids) {
            if (girl.getJob().equals(job)) {
                randomGirl = girl;
            } else if (girl.getTask() != null && girl.getTask().equals(job)) {
                randomGirl = girl;
            }
        }
        return new Pair<>(randomGirl, all.get(randomGirl));
    }

    private void resetPanels(JPanel toMainPanel, JPanel panelWithGirl) {
        JPanel parent = (JPanel) panelWithGirl.getParent();
        parent.remove(panelWithGirl);
        parent.revalidate();
        parent.repaint();
        toMainPanel.add(panelWithGirl, "growx, hidemode 1");
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
        String toJob, randomGirlJob;
        toJob = randomGirlJob = jmi.getText();
        String menuText = mainMenu.getText();
        if (menuText.contains("Dismiss")) {
            toJob = "free";
        } else {
            randomGirlJob = "free";
        }

        if (menuText.contains("1")) {
            Pair<NewAndroid, JPanel> pair = randomName(randomGirlJob);
            changeWorkStatus(toJob, pair.getKey(), pair.getValue());
        } else {
            String finalToJob = toJob;
            checkList.forEach((key, value) -> changeWorkStatus(finalToJob, key, value));
        }
    }
}
