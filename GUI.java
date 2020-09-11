import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GUI implements ComponentListener {
    JPanel backgroundMainPanel;
    JLayeredPane mainContainer;
    JLabel icon, brokenGeneratorLabel, helperName, energyCount;
    JButton doll;
    BufferedImage helper_icon, lain, rozen;
    FileManage fileManage;
    Lists data;
    List<JobWrapper> jobList;
    List<ResourcesWrapper> resources;
    WorkProceeding work;
    int helperIconId;
    String directory = "C:/Users/User/Documents/NyanData";
    String androidPath = "C:/Users/User/Documents/NyanData/Androids";
    String generatorPath = "C:/Users/User/Documents/NyanData/EnergyGenerators";
    String resourcesPath = "C:/Users/User/Documents/NyanData/Resources";

    public static void main(String[] args) {
        new GUI();
    }

    public GUI() {
        fileManage = new FileManage();
        work = new WorkProceeding();
        data = Lists.getInstance();
        try {
            init();
            helper_icon = ImageIO.read(this.getClass().getResource("/image" + helperIconId + ".png"));
            lain = ImageIO.read(this.getClass().getResource("/broken_generator.png"));
            rozen = ImageIO.read(this.getClass().getResource("/doll1.jpg"));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new AndroidFrame();
    }

    private void init() throws IOException, ClassNotFoundException {
        helperName = new JLabel();
        energyCount = new JLabel();
        if (!Files.isDirectory(Paths.get(directory))) {
            new File(directory);
            new NewAndroid(true);
            new NewAndroid(false);
            new NewAndroid(false);

            //new EnergyGenerator(true);
            //add energy to resources List

        } else {
            data.androids = (ArrayList<NewAndroid>) (Object) fileManage.objectsLoad(androidPath);
            //load energy generators arraylist
            //data.resources = (Map<String, Integer>) (Object) fileManage.mapLoad(resourcesPath);
        }
        //мусор, цветы - абстрактные ресурсы c рандомными выпадениями штук
        //механики и алхимики учатся экстрактить новые вещи из штук
        //JobWrapper - хранятся изученные ресурсы ("нептуний", "серебро")
        //ресурсы подгружаются как объекты в arraylist (внутри Lists)

        //подгружать ресурсы
        //если нет файла, то ничего не инициализировать
        //минимальные ресурсы (?): мусор, детали, цветы, топливо
        //при открытии технологии появляется новый ресурс

        resources = Arrays.asList(
                new ResourcesWrapper("energy").setResLabel(energyCount),
                new ResourcesWrapper("garbage"),
                new ResourcesWrapper("cpu"),
                new ResourcesWrapper("motherboards"),
                new ResourcesWrapper("flowers")
        );
        data.res = resources;
        resources.get(0).setValue(400);
        resources.get(1).setValue(40);
        resources.get(2).setSource("garbage");
        resources.get(3).setSource("garbage");

        resources.forEach((resource) -> {
            String itemText = resource.getLabelText();
            resource.getResLabel().setText(itemText + data.getResValue(resource.getKey()));
        });

        jobList = Arrays.asList(
                new JobWrapper("free"),
                new JobWrapper("garbagers").setWorkerCondition("garbage", 5000, false),
                new JobWrapper("mechanics")
                        .setWorkerCondition(null, 15000, true)
                .setProducts("motherboards cpu"),
                new JobWrapper("gardeners").setWorkerCondition("flowers", 5000, false)
        );

        jobList.forEach((jobWrapper) -> {
            String key, textWorker;
            key = jobWrapper.getKey();
            textWorker = jobWrapper.getWorkerText();
            int workerCount = data.getWorkersNumber(key, false);
            jobWrapper.getAndroidWorker().setText(textWorker + workerCount);

            if (!key.equals("free")) {
                if (jobWrapper.getIsRecyclist()) {
                    jobWrapper.setTimer(
                            () -> work.converseItem(jobWrapper.getResourceKey()));
                } else {
                    jobWrapper.setTimer(
                            () -> work.giveItem(data.getWorkersNumber(key, false),
                                    jobWrapper.getResourceKey()));
                }
                TimerWrapper timerWrapper = jobWrapper.getTimerWrapper();
                if (workerCount > 0) {
                    timerWrapper.timerStart();
                }
            }
        });

        NewAndroid helper = data.androids.get(0);
        helperName.setText(helper.getName());
        helperIconId = helper.getIconId();

        TimerWrapper energyWrapper = new TimerWrapper(
                ()-> work.giveItem(- 5 * data.androids.size(),"energy"), 10000);
        energyWrapper.timerStart();
    }

    private void saveData() throws IOException {
        fileManage.objectsSave(androidPath, data.androids);
        fileManage.objectsSave(generatorPath, data.generators);
        //сохранить ресурсы
    }

    private void restartGame() {
        //закрыть все внутренние фреймы, если они вызваны
        //Free: 1
        //энергия снова на максимуме
        //все остальные jlabel: 0
        //рестартнуть таймер у энергии

        //delete directory and init()?
        //или итерация workers (set Jlabels texts to 0 - ?)
        //+ чистка arraylists и map?
    }

    private void disableActivity() {
        //все таймеры отключаются, если энергия на нуле
    }

    private String addCapitalLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        BufferedImage image;
        Object source = e.getSource();
        Container container = (Container) source;
        switch (container.getName()) {
            case "Helper":
                image = helper_icon;
                break;
            case "Rozen":
                image = rozen;
                break;
            case "Generator":
                image = lain;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + container.getName());
        }
        if (source instanceof JLabel) {
            JLabel label = (JLabel) source;
            label.setIcon(new ImageIcon(getScaledImage(image, container.getWidth(), container.getHeight())));
        } else if (source instanceof JButton) {
            JButton button = (JButton) source;
            button.setIcon(new ImageIcon(getScaledImage(image, container.getWidth(), container.getHeight())));
        }

    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    class AndroidFrame extends JFrame {
        AndroidFrame() {
            new JFrame();
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setMinimumSize(new Dimension(200, 200));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            backgroundMainPanel = new MainPanel();
            mainContainer = new JDesktopPane();
            mainContainer.add(backgroundMainPanel, JLayeredPane.DEFAULT_LAYER);
            setContentPane(mainContainer);
            addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent windowEvent) {
                    UIManager.put("OptionPane.yesButtonText", "Угу..");
                    UIManager.put("OptionPane.noButtonText", "Нет, я скоро вернусь..");
                    if (JOptionPane.showConfirmDialog(AndroidFrame.this,
                            "Удалить текущий мир перед возвращением?", "Возвращение в свою реальность.",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        fileManage.deleteDirectory(new File("C:/Users/User/Documents/NyanData"));
                    } else {
                        try {
                            saveData();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    System.exit(0);
                }
            });
            addComponentListener(new ComponentAdapter() {

                @Override
                public void componentResized(ComponentEvent e) {

                    SwingUtilities.invokeLater(() -> backgroundMainPanel.setSize(getWidth(), getHeight()));
                }
            });
            setTitle("Android daughters~♡");
            ImageIcon icon = new ImageIcon(getClass().getResource("/frame.jpg"));
            setIconImage(icon.getImage());
            pack();
            setVisible(true);
        }
    }

    class MainPanel extends JPanel {
        MainPanel() {
            new JPanel();
            setLayout(new MigLayout("fill"));
            add(new HelperPanel(), "grow");
            add(new JobPanel(), "top, split 2, flowy, sgx[nya]");
            add(new ResourcesPanel(), "sgx[nya]");
            add(new GeneratorPanel(), "grow");
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Image background = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/background.jpg"));
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    class HelperPanel extends JPanel {
        HelperPanel() {
            new JPanel();
            setLayout(new MigLayout("fill"));
            setOpaque(false);
            icon = new JLabel();
            icon.setIcon(new ImageIcon(helper_icon));
            add(icon, "top, split 2, flowx, grow, gapx 0, wmin 0, hmin 0," + " wmax " + getW(helper_icon) + "," +
                    " hmax " + getH(helper_icon));
            icon.setName("Helper");
            icon.addComponentListener(GUI.this);
            JPanel miniPanel = new JPanel();
            miniPanel.setLayout(new MigLayout("fill, flowy"));
            miniPanel.setOpaque(false);
            miniPanel.add(helperName);
            miniPanel.add(new JButton("Diary"));
            miniPanel.add(new JButton("Pray"));
            add(miniPanel, "top, wrap");
            doll = new JButton();
            doll.setName("Rozen");
            doll.setText("READY TO MAKE ANDROIDS");
            doll.setHorizontalTextPosition(SwingConstants.CENTER);
            add(doll, "grow, wmin 0, hmin 0, top," + " wmax " + getW(rozen) + "," + " hmax " + getH(rozen));
            doll.addComponentListener(GUI.this);

        }
    }

    class JobPanel extends JPanel {
        JobPanel() {
            new JPanel();
            setOpaque(false);
            setLayout(new MigLayout("flowy"));
            JButton manage = new JButton("Manage androids&pods");
            manage.addActionListener(e -> {
                new ManagementMenu(jobList, mainContainer, manage);
                manage.setEnabled(false);
            });
            add(manage);
            jobList.forEach((job)-> add(job.getAndroidWorker()));
            add(new JButton("Technology Tree"));
        }

        public void paintComponent(Graphics g) {
            Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/button.jpg"));
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    class GeneratorPanel extends JPanel {
        GeneratorPanel() {
            new JPanel();
            setOpaque(false);
            setLayout(new MigLayout("fill"));
            add(energyCount, "flowy, top, split 4");
            brokenGeneratorLabel = new JLabel();
            brokenGeneratorLabel.setName("Generator");
            brokenGeneratorLabel.setIcon(new ImageIcon(lain));
            brokenGeneratorLabel.addComponentListener(GUI.this);
            add(brokenGeneratorLabel, "grow, wmin 0, hmin 0," + " wmax " + getW(lain) + "," +
                    " hmax " + getH(lain));
            add(new JLabel("Осталось немного"));
            JScrollPane pane = new JScrollPane();
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            String[] res = {"Вы вышли на связь с одиноким андроидом", "Обнаружен поломанный ветряной генератор",
                    "Вы смогли починить генератор, теперь он дает энергию"};
            for (String s : res) {
                JLabel label = new JLabel(s);
                label.setFont(new Font("Serif", Font.BOLD, 14));
                panel.add(label);
            }
            pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            pane.setLayout(new ScrollPaneLayout());
            pane.setViewportView(panel);
            add(pane, "grow, wmax " + getW(lain));
        }
    }

    class ResourcesPanel extends JScrollPane {
        ResourcesPanel() {
            new JScrollPane();
            JPanel panel = new JPanel();
            panel.setBackground(Color.WHITE);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            resources.stream()
                    .filter((res)-> !res.getKey().equals("energy"))
                    .forEach((res)-> {
                        JLabel label = res.getResLabel();
                        label.setFont(new Font("Serif", Font.BOLD, 16));
                        panel.add(label);
                    });

            setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            setLayout(new ScrollPaneLayout());
            setViewportView(panel);
        }
    }

    private BufferedImage getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }

    private String getW(BufferedImage image) {
        return String.valueOf(image.getWidth());
    }

    private String getH(BufferedImage image) {
        return String.valueOf(image.getHeight());
    }
}
