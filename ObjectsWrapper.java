import javax.swing.*;
import java.util.function.Supplier;

public class ObjectsWrapper{
    private JLabel androidWorker, itemProduced;
    private TimerWrapper timerWrapper;
    private String workerText, itemText;
    private JPanel mainPanel;
    //private JLabel podWorker;

    public ObjectsWrapper timerSet(String itemText, long timeTick, Supplier <Integer> supplier, boolean isLimited){
        this.itemText = itemText;
        this.itemProduced = new JLabel();
        this.timerWrapper = new TimerWrapper(itemProduced, itemText, timeTick, supplier, isLimited);
        return this;
    }

    public ObjectsWrapper workerSet(String workerText){
        this.workerText = workerText;
        this.androidWorker = new JLabel();
        return this;
    }

    public ObjectsWrapper panelSet(){
        this.mainPanel = new JPanel();
        return this;
    }

    public JLabel getAndroidWorker(){
        return androidWorker;
    }

    public JLabel getItemProduced(){return itemProduced;}

    public TimerWrapper getTimerWrapper(){
        return timerWrapper;
    }

    public String getWorkerText(){
        return workerText;
    }

    public String getItemText(){
        return itemText;
    }

    public JPanel getMainPanel(){
        return mainPanel;
    }
}
