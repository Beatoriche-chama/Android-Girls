import java.util.*;

public class Pick_Flowers {
    //здесь девочки собирают цветы
    GirlsList girlsList = new GirlsList();
    List<Integer> flowers = new ArrayList<>();
    Android_Helper android_helper = new Android_Helper();

    public static void main(String[] args) {

    }

    public Pick_Flowers(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override public void run() {
                flowerPicker();
            }
        }, 0, 5000);

    }


    public List<Integer> getFlowerList() { return flowers; }

    public int flowerPicker() {
        int flower_picked = 1 * girlsList.flower_girls.size();
        flowers.add(flower_picked);
        System.out.println(android_helper.sum(flowers));
        return android_helper.sum(flowers);
    }

}
