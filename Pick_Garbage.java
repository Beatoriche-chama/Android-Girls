import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Pick_Garbage {
    //здесь девочки собирают мусор
    GirlsList girlsList = new GirlsList();
    List<Integer> garbage = new ArrayList<>();
    Android_Helper android_helper = new Android_Helper();

    public static void main(String[] args) {
    }

    public int garbagePicker() {

        int garbage_picked = 1 * girlsList.garbagers.size();
        garbage.add(garbage_picked);
        return android_helper.sum(garbage);
    }
}
