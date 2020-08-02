import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Supplier;

public class TimerWrapper {

    private final long period;
    private final JLabel item;
    private boolean isActivated;

    public TimerWrapper(JLabel item, long period) {
        this.isActivated = false;
        this.item = item;
        this.period = period;
    }

    public void run(Supplier<Integer> supplier, String cute, boolean isLimited) {
        if (isActivated) return;
        isActivated = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int x = supplier.get();
                item.setText(x + cute);
                if(isLimited && x < 1){
                    timer.cancel();
                }
            }
        }, 0, period);
    }

}
