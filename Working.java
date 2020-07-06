import java.util.*;

public class Working {
    //здесь девочки собирают цветы и мусор
    GirlsList girlsList = new GirlsList();
    List<Integer> flowers = new ArrayList<>();
    List<Integer> garbage = new ArrayList<>();

    public static void main(String[] args) {

    }

    public Working() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                flowerPicker(girlsList.flower_girls);
                garbagePicker(girlsList.garbagers);
            }
        };
        timer.schedule(task, 0, 5000);

    }

    public List<Integer> getFlowerList() { return flowers; }
    public List<Integer> getGarbageList() { return garbage; }

    public int sum(List<Integer> list) {
        int sum = 0;
        for (int i : list)
            sum = sum + i;
        return sum;
    }


    public int flowerPicker(List<String> flower_picker) {
        int flower_picked = 1 * flower_picker.size();
        flowers.add(flower_picked);
        System.out.println("Цветочницы собрали цветочков: " + sum(flowers));
        return sum(flowers);
    }

    public int garbagePicker(List<String> garbage_picker) {

        int garbage_picked = 1 * garbage_picker.size();
        garbage.add(garbage_picked);
        System.out.println("Исследовательницы собрали мусора: " + sum(garbage));
        return sum(garbage);
    }

}
