import java.util.*;

//здесь девочки собирают цветы и мусор
public class Working {
    List <String> flower_girls = Arrays.asList("Anna", "Nina", "Alice");
    List <String> garbager = Arrays.asList("Rika", "Elly");
    List<Integer> flowers = new ArrayList<>();
    List<Integer> garbage = new ArrayList<>();

    public static void main(String[] args) {

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
