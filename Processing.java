import java.util.*;

public class Processing{
    List <String> alchemy_girls = Arrays.asList("Nika", "Rachel", "Lily");
    List <String> flower_girls = Arrays.asList("Anna", "Nina", "Alice");
    List <String> garbager = Arrays.asList("Rika", "Elly", "Rina", "Kira");
    List <String> mechanic = Arrays.asList("Rika", "Elly");
    List<Integer> fuel_tanks = new ArrayList<>();
    List <Integer> details_stack = new ArrayList<>();
    Working w = new Working();


    public Processing(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                alchemyFuel(flower_girls);
                mechanicDetails(garbager);
            }
        };
        timer.schedule(task, 0, 15000);
    }

    public static void main(String[] args) {
        Processing p = new Processing();
    }

    public int alchemyFuel(List<String> flower_girls){
        int flower_sum = w.sum(w.getFlowerList());
        int fuel = (flower_sum / flower_girls.size()) * alchemy_girls.size();
        fuel_tanks.add(fuel);
        int now_flowers = flower_sum - fuel;
        System.out.println("Алхимики расходовали " + flower_sum + " цветочков и их осталось " +
                now_flowers);
        w.flowers.clear();
        w.flowers.add(now_flowers);
        System.out.println("Алхимики сделали топлива: " + w.sum(fuel_tanks));
        return w.sum(fuel_tanks);
    }

    public int mechanicDetails (List<String> garbager){
        int garbage_sum = w.sum(w.getGarbageList());
        int details = (garbage_sum / garbager.size()) * mechanic.size();
        details_stack.add(details);
        int now_garbage = garbage_sum - details;
        System.out.println("Механики переработали " + garbage_sum + " мусора и их осталось " +
                now_garbage);
        w.garbage.clear();
        w.garbage.add(now_garbage);
        System.out.println("Механики изготовили деталек: " + w.sum(details_stack));
        return w.sum(details_stack);
    }
}


