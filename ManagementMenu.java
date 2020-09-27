import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.util.Pair;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;


public class ManagementMenu implements ActionListener, MouseListener {
    Lists lists = Lists.getInstance();
    JLayeredPane jDesktopPane;
    JButton manageButton;
    JMenuBar menuBar;
    List<JobWrapper> objects;
    BiMap<JPanel, NewAndroid> girlPanels;
    JMenu hireAutoMenu, dismissAutoMenu, hireMenu, dismissMenu, pickFilterMenu;
    JMenu[] jMenuss;

    public ManagementMenu(List<JobWrapper> objects, JLayeredPane jDesktopPane, JButton manageButton) {
        this.objects = objects;
        this.jDesktopPane = jDesktopPane;
        this.manageButton = manageButton;
        girlPanels = HashBiMap.create();
        jDesktopPane.add(createManageMenu(createMenuBar()), JLayeredPane.PALETTE_LAYER);
    }

    public JMenuBar createMenuBar() {
        //сделать только три постоянных меню: dismiss, hire, upgrade all available
        //upgrade ручной для каждой девочки (панельки), если кто-то выбран
        //меню select появляется только при чеке
        menuBar = new JMenuBar();
        hireAutoMenu = new JMenu("Hire 1 to");
        dismissAutoMenu = new JMenu("Dismiss 1 from");
        hireMenu = new JMenu("Hire selected");
        dismissMenu = new JMenu("Dismiss selected");
        pickFilterMenu = new JMenu("Pick filter");
        jMenuss = new JMenu[]{hireAutoMenu, dismissAutoMenu, hireMenu, dismissMenu};
        Arrays.stream(jMenuss).forEach((jMenu -> {
            if (jMenu.getText().contains("selected")) {
                jMenu.setVisible(false);
            }
        }));
        dismissMenu.addMouseListener(this);

        objects.stream().filter((job) -> !job.getKey().equals("free"))
                .forEach((job) -> {
                    String jobName = job.getKey();
                    Arrays.stream(jMenuss)
                            .filter((jMenu -> !jMenu.getText().equals("Dismiss selected")))
                            .forEach(jMenu -> {
                                menuBar.add(jMenu);

                                if (job.getIsRecyclist()) {
                                    JMenu subMenu = new JMenu(jobName);
                                    jMenu.add(subMenu);
                                    job.getTasks().forEach((recycleTask) -> {
                                                JMenuItem menuItem = new JMenuItem(recycleTask.getKey());
                                                menuItem.addActionListener(this);
                                                subMenu.add(menuItem);
                                            }
                                    );
                                } else {
                                    JMenuItem subMenuItem = new JMenuItem(jobName);
                                    jMenu.add(subMenuItem);
                                    subMenuItem.addActionListener(this);
                                }
                            });
                    if (!job.getIsRecyclist()) {
                        JMenu pickSubMenu = new JMenu(jobName);
                        JMenuItem pickAll = new JMenuItem("all");
                        pickAll.addActionListener(this);
                        pickSubMenu.add(pickAll);
                        job.getTasks().forEach((pickTask) -> {
                            String pickTaskName = pickTask.getKey();
                            JMenuItem pickMenuItem = new JMenuItem(pickTaskName);
                            pickMenuItem.addActionListener(this);
                            setPickItemColor(pickTaskName, pickMenuItem);
                            pickSubMenu.add(pickMenuItem);
                        });
                        pickFilterMenu.add(pickSubMenu);
                    }
                });
        menuBar.add(dismissMenu);
        menuBar.add(pickFilterMenu);
        return menuBar;
    }


    public JInternalFrame createManageMenu(JMenuBar jMenuBar) {
        //internal frame = pip-girl
        //newPanel.setVisible(true)
        //oldPanel.setVisible(false)
        //андроиды = панель с вкладками

        //оставить tabbedPane
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        objects.forEach((wrapper) -> {
            JPanel mainPanel = addToMainPanel(lists.androids, wrapper);
            JScrollPane scrollPane = new JScrollPane(mainPanel);
            tabbedPane.add(scrollPane, wrapper.getWorkerText());
        });
        //создать, только потом перебором по девочкам

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

    private JPanel addToMainPanel(ArrayList<NewAndroid> list, JobWrapper wrapper) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new MigLayout("fillx, insets 0, gapy 0, flowy, toptobottom"));
        //по-другому расписать
        //сначала вкладки с job
        //
        if (wrapper.getIsRecyclist()) {
            for (TaskWrapper task : wrapper.getTasks()) {
                String productName = task.getKey();
                JList<JPanel> taskList = new JPanelJList(jMenuss);
                task.setJList(taskList);
                Border taskBorder = BorderFactory.createTitledBorder(productName.toUpperCase());
                JPanel productPanel = new JPanel();
                productPanel.setBorder(taskBorder);
                productPanel.setLayout(new MigLayout("fillx, insets 0, gapy 0, flowy, toptobottom"));
                createHideShowButton(productPanel);
                productPanel.add(taskList, "grow, hidemode 2");
                for (NewAndroid girl : list) {
                    if (girl.getTask() != null && girl.getTask().equals(productName)) {
                        addGirlPanel(girl, taskList);
                    }
                }
                mainPanel.add(productPanel, "growx");
            }

        } else {
            JList<JPanel> jobList = new JPanelJList(jMenuss);
            wrapper.setJList(jobList);
            mainPanel.add(jobList, "growx");
            for (NewAndroid girl : list) {
                if (girl.getJob().equals(wrapper.getKey())) {
                    addGirlPanel(girl, jobList);
                }
            }
        }
        mainPanel.setBackground(Color.LIGHT_GRAY);
        return mainPanel;
    }

    private void createHideShowButton(JPanel panel) {
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

    private void addGirlPanel(NewAndroid girl, JList<JPanel> taskList) {
        DefaultListModel<JPanel> model = (DefaultListModel<JPanel>) taskList.getModel();
        JPanel girlPanel = new JPanel();
        ImageIcon icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass()
                .getResource("/image" + girl.getIconId() + ".png")));
        JLabel iconLabel = new JLabel(icon);
        String[] data = {girl.getName(), girl.getInfo(), girl.getVersion()};
        girlPanel.setLayout(new MigLayout("fill"));
        girlPanel.setBackground(Color.WHITE);
        Border blackline = BorderFactory.createLineBorder(Color.black);
        girlPanel.setBorder(blackline);
        girlPanel.add(iconLabel, "growx");
        for (String s : data) {
            JLabel label = new JLabel(s);
            label.setFont(new Font("Serif", Font.BOLD, 18));
            girlPanel.add(label, "split 3, flowy, growx");
        }
        girlPanels.put(girlPanel, girl);
        model.addElement(girlPanel);
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

    private void check(String menuText, String toJob, String randomGirlJob) {
        if (menuText.contains("Dismiss")) {
            toJob = "free";
        } else {
            randomGirlJob = "free";
        }

        if (menuText.contains("1")) {
            Pair<JPanel, NewAndroid> pair = ManagementUtils.autoGetGirl(randomGirlJob, girlPanels, lists);
            ManagementUtils.changeWorkStatus(toJob, pair.getValue(), pair.getKey(), lists, hireAutoMenu, objects);
        } else {
            ArrayList<JList<JPanel>> jLists = ManagementUtils.getAllSelectedJLists(objects);
            String finalToJob = toJob;
            jLists.forEach((jList) -> jList.getSelectedValuesList().forEach((panel)
                    -> ManagementUtils.changeWorkStatus(finalToJob, girlPanels.get(panel), panel,
                    lists, hireAutoMenu, objects)));
        }
    }

    private void setPickItemColor(String taskName, JMenuItem jmi) {
        TaskWrapper task = ManagementUtils.getObj(taskName, objects).getCertainTask(taskName);
        jmi.setBackground(task.getIsActive() ? Color.orange : Color.WHITE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMenuItem jmi = (JMenuItem) e.getSource();
        JMenu mainMenu = getMenuBarMenu(jmi);
        String toJob, randomGirlJob;
        toJob = randomGirlJob = jmi.getText();
        String menuText = mainMenu.getText();

        if (menuText.equals("Pick filter")) {
            //не по toJob, а по родителю
            JobWrapper job = ManagementUtils.getObj(toJob, objects);
            if (toJob.equals("all")) {
                job.setAllTasksActive();
                job.getTasks().forEach((task -> setPickItemColor(toJob, jmi)));
            } else {
                TaskWrapper task = job.getCertainTask(toJob);
                boolean isActive = task.getIsActive();
                task.setActive(!isActive);
                setPickItemColor(toJob, jmi);
            }

            if (job.getActiveTasks() == null) {
                job.getTimerWrapper().timerStop();
            } else if (lists.getWorkersNumber(job.getKey(), false) > 0 &&
                    job.getActiveTasks().size() == 1) {
                job.getTimerWrapper().timerStart();
            }
        } else {
            check(menuText, toJob, randomGirlJob);
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        ArrayList<JList<JPanel>> jLists = ManagementUtils.getAllSelectedJLists(objects);
        jLists.forEach((jList) -> jList.getSelectedValuesList().forEach((panel)
                -> ManagementUtils.changeWorkStatus("free", girlPanels.get(panel), panel,
                lists, hireAutoMenu, objects)));

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
