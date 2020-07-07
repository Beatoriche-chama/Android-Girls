import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Mechanic {
    //здесь механики изготавливают детали
    GirlsList girlsList = new GirlsList();
    List<Integer> details_stack = new ArrayList<>();
    Pick_Garbage pick_garbage = new Pick_Garbage();
    Android_Helper android_helper = new Android_Helper();

    public Mechanic() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                mechanicDetails();
            }
        }, 0, 15000);
    }

    public int mechanicDetails (){
        int garbage_sum = android_helper.sum(pick_garbage.getGarbageList());
        int details = (garbage_sum / girlsList.garbagers.size()) * girlsList.mechanic.size();
        details_stack.add(details);
        int now_garbage = garbage_sum - details;
        System.out.println("Механики переработали " + garbage_sum + " мусора и их осталось " +
                now_garbage);
        pick_garbage.garbage = new ArrayList<>();
        pick_garbage.garbage.add(now_garbage);
        return android_helper.sum(details_stack);
    }

    public List<Integer> getDetailsList() { return details_stack; }
}
