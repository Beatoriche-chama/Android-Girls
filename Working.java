import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//здесь девочки собирают цветы и мусор
public class Working {
    String[] flower_girls = {"Рика", "Сатоко", "Ханю"};
    String[] garbager = {"Алиса", "Элли"};
    List<Integer> flowers = new ArrayList<>();
    List<Integer> garbage = new ArrayList<>();

    public static void main(String[] args) {
        Working w = new Working();

    }

    public Working() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                flowerPicker(flower_girls);
                garbagePicker(garbager);
            }
        };
        timer.schedule(task, 0, 5000);

    }

    public int sum(List<Integer> list) {
        int sum = 0;
        for (int i : list)
            sum = sum + i;
        return sum;
    }


    public int flowerPicker(String[] flower_picker) {

        int flower_picked = 1 * flower_picker.length;
        flowers.add(flower_picked);
        return sum(flowers);
    }

    public int garbagePicker(String[] garbage_picker) {

        int garbage_picked = 1 * garbage_picker.length;
        garbage.add(garbage_picked);
        System.out.println(sum(garbage));
        return sum(garbage);
    }

}
