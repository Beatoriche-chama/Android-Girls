import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import net.miginfocom.swing.MigLayout;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Task implements ComponentListener {
    JLabel icon, brokenGeneratorLabel, helperName, energyCount;
    JButton doll;
    BufferedImage helper_icon, lain, rozen;
    Map <String, ObjectsWrapper> workers;
    FileManage fileManage;
    Lists data;
    WorkProceeding work;
    int helperIconId, energy;

    public static void main(String[] args) {
        new Task();
    }

    public Task() {
        fileManage = new FileManage();
        data = Lists.getInstance();
        workers = new HashMap<>();
        helperName = new JLabel();
        energyCount = new JLabel();
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
        String directory = "C:/Users/User/Documents/NyanData";
        String androidPath = "C:/Users/User/Documents/NyanData/Androids";
        //String generatorPath = "C:/Users/User/Documents/NyanData/EnergyGenerators";
        String resourcesPath = "C:/Users/User/Documents/NyanData/Resources";
        if (!Files.isDirectory(Paths.get(directory))) {
            new File(directory);
            new NewAndroid(true);
            //new EnergyGenerator(true);
            energy = 400;
        } else {
            data.androids = (ArrayList<NewAndroid>) (Object) fileManage.objectsLoad(androidPath);
            //load energy generators arraylist
            data.resources = (Map<String, Integer>) (Object)fileManage.mapLoad(resourcesPath);
        }
        //Инициализация workers map
        Table<String, String, Long> table = ImmutableTable.<String, String, Long>builder()
                .put("free", "", (long) 0)
                .put("gardener", "flowers", (long) 5000)
                .put("garbager", "garbage", (long) 5000).build();

        for (Table.Cell<String, String, Long> pair : table.cellSet()) {
            String key, textWorker;
            key = textWorker = pair.getRowKey();
            String value = pair.getColumnKey();
            ObjectsWrapper objWrapper = new ObjectsWrapper();

            if (!key.equals("free")){
                String finalValue = addCapitalLetter(value) + ": ";
                objWrapper.timerSet(value, pair.getValue(),
                        ()-> work.pickItem(data.getJobs(key), finalValue), false);
                textWorker += "s";
                data.resources.putIfAbsent(value, 0);
                objWrapper.getItemProduced().setText(finalValue + data.getResource(value));
            }
            textWorker = addCapitalLetter(textWorker) + ": ";
            objWrapper.build(textWorker);
            int workerCount = data.getJobs(key);
            objWrapper.getAndroidWorker().setText(textWorker + workerCount);
            workers.put(key, objWrapper);
            TimerWrapper timerWrapper = objWrapper.getTimerWrapper();
            if (workerCount > 0 && timerWrapper != null) {
                timerWrapper.timerStart();
            }
        }

        NewAndroid helper = data.androids.get(0);
        helperName.setText(helper.getName());
        helperIconId = helper.getIconId();
        data.resources.put("energy", energy);
        TimerWrapper energyWrapper = new TimerWrapper(energyCount,  "Now energy: ",
                10000, ()-> data.eatEnergy(), true);
        energyWrapper.timerStart();
    }

    private void saveData() {
        //fileManage.objectsSave(); -> arraylists
        //fileManage.mapSave(); -> resources
    }

    private void restartGame() {
        //закрыть все модальные jdialog, если они вызваны
        //Free: 1
        //энергия снова на максимуме
        //все остальные jlabel: 0
        //все arraylists, hashmaps: null
        //рестартнуть таймер у энергии
    }

    private void disableActivity() {
        //все таймеры отключаются, если энергия на нуле
    }

    private String addCapitalLetter(String str){
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
            setContentPane(new MainPanel());
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
                    }
                    System.exit(0);
                }
            });
            pack();
            setVisible(true);
        }
    }

    class MainPanel extends JPanel {
        MainPanel() {
            new JPanel();
            setLayout(new MigLayout("fill, debug"));
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
            setLayout(new MigLayout("debug, fill"));
            setOpaque(false);
            icon = new JLabel();
            icon.setIcon(new ImageIcon(helper_icon));
            add(icon, "top, split 2, flowx, grow, gapx 0, wmin 0, hmin 0," + " wmax " + getW(helper_icon) + "," +
                    " hmax " + getH(helper_icon));
            icon.setName("Helper");
            icon.addComponentListener(Task.this);
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
            doll.addComponentListener(Task.this);

        }
    }

    class JobPanel extends JPanel {
        JobPanel() {
            new JPanel();
            setOpaque(false);
            setLayout(new MigLayout("flowy"));
            add(new JButton("Manage androids&pods"));

            //List<String> pods = Arrays.asList("4", "5", "7", "3", "0", "9");
            //int i = 0;
            for (Map.Entry<String, ObjectsWrapper> pair : workers.entrySet()) {
                //, "split 2, flowx" -> for workerLabel
                add(pair.getValue().getAndroidWorker());
                //add(new JLabel(", pods: " + pods.get(i)));
                //i++;
            }
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
            brokenGeneratorLabel.addComponentListener(Task.this);
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
            for (Map.Entry <String, ObjectsWrapper> pair : workers.entrySet()){
                if(pair.getKey().equals("free")){
                    continue;
                }
                JLabel label = pair.getValue().getItemProduced();
                label.setFont(new Font("Serif", Font.BOLD, 16));
                panel.add(label);
            }
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
    /*
    public int energySupplier() {
        int energy_now = energyGenerator.eatEnergy();
        int details = m.getDetails();
        details_left.setText((30 - details) + " details left to gather~");
        if (details == 30) {
            details_left.setText("");
            win();
            energyGenerator.giveEnergy();
        }
        if (energy_now < 1) {
            energy_count.setText("Energy is 0. Do Androids Dream of Electric Sheep?");
            try {
                badEndmessage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return energy_now;
    }

    public void win() {
        ImageIcon icon = new ImageIcon(getClass().getResource("/goodbye.png"));
        UIManager.put("OptionPane.okButtonText", "All hail technocracy~");
        JOptionPane.showConfirmDialog(frame, "Теперь девочка обязательно выживет!", "Спасибо, хозяин",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, icon);
    }

    public void badEndmessage() throws IOException {
        System.out.println("Конец игры.");
        UIManager.put("OptionPane.yesButtonText", "Помочь мечте андроидов сбыться в новой реальности");
        UIManager.put("OptionPane.noButtonText", "Оставить этот умирающий мир");
        int result = JOptionPane.showConfirmDialog(null,
                "Няша, твоя рабыня навеки застыла без энергии." +
                        " Сейчас ее программное обеспечение связи с параллельным миром отключится.",
                "Конец реальности", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            restart();
            startGame();
        }
        if (result == JOptionPane.NO_OPTION) {
            fileManage.deleteDirectory(new File("C:/Users/User/Documents/NyanData"));
            System.exit(0);
        }
    }
     */


}
