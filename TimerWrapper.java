import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;
import javax.swing.JLabel;

public class TimerWrapper {

    private final long period;
    private final JLabel label;
    private boolean isActivated;

    public TimerWrapper(JLabel label, long period) {
        this.isActivated = false;
        this.label = label;
        this.period = period;
    }

    public void run(Supplier<Integer> supplier, String cute) {
        if (isActivated) return;
        isActivated = true;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                label.setText(supplier.get() + cute);
            }
        }, 0, period);
    }

}
