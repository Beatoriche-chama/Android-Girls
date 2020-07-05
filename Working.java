import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Consumer;

public class Working {
    //здесь девочки собирают цветы и мусор
    public static void main(String[] args) {

    }

    public Working() {
        flowerPicker((flowers) -> System.out.println("Девочки собрали: " + sum(flowers) + " цветочков."));
    }

    public int sum(List<Integer> list) {
        int sum = 0;
        for (int i : list)
            sum = sum + i;
        return sum;
    }


    public void flowerPicker(Consumer<List<Integer>> callback) {
        String[] flower_picker = {"Рика", "Сатоко", "Ханю"};
        Timer timer = new Timer();
        List<Integer> flowers = new ArrayList<>();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int flower_picked = 1 * flower_picker.length;
                flowers.add(flower_picked);
                callback.accept(flowers);
            }
        };
        timer.schedule(task, 0, 5000);
    }

    public void garbagePicker(Consumer<Integer> callback1) {
        String[] garbager = {"Алиса", "Элли"};
        Timer timer = new Timer();
        List<Integer> garbage = new ArrayList<>();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int garbage_picked = 1 * garbager.length;
                garbage.add(garbage_picked);
                int sum_garbage = sum(garbage);
                callback1.accept(sum_garbage);
            }
        };
        timer.schedule(task, 0, 5000);
    }
}
