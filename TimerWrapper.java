import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class TimerWrapper {
    private final long period;
    private final JLabel item;
    private boolean isActivated;
    private Supplier<Integer> supplier;
    private final String cute;
    private boolean isLimited;
    private Timer timer;

    public TimerWrapper(JLabel item, long period, Supplier<Integer> supplier, String cute, boolean isLimited) {
        this.isActivated = false;
        this.item = item;
        this.period = period;
        this.supplier = supplier;
        this.cute = cute;
        this.isLimited = isLimited;
        this.timer = new Timer();
    }

    public boolean getStatus() {
        return isActivated;
    }

    public void addTask() {
        if (!isActivated) return;
        int x = supplier.get();
        System.out.println("Сейчас при значении: " + x + " статус " + getStatus());
        item.setText(x + cute);
        if (isLimited && x < 1) {
            timerStop();
        }
    }

    public void timerStop() {
        this.timer.cancel();
        isActivated = false;
    }

    public void timerStart() {
        this.timer = new Timer();
        isActivated = true;
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                addTask();
            }
        },0, period);
    }

}
