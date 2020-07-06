import java.util.*;

public class Processing{
    //здесь алхимики экстрактят топливо, механики изготавливают детали
    GirlsList girlsList = new GirlsList();
    List<Integer> fuel_tanks = new ArrayList<>();
    List <Integer> details_stack = new ArrayList<>();
    Working w = new Working();


    public Processing(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                alchemyFuel(girlsList.flower_girls);
                mechanicDetails(girlsList.garbagers);
            }
        };
        timer.schedule(task, 0, 15000);
    }

    public static void main(String[] args) {

    }

    public int alchemyFuel(List<String> flower_girls){
        int flower_sum = w.sum(w.getFlowerList());
        int fuel = (flower_sum / flower_girls.size()) * girlsList.alchemy_girls.size();
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
        int details = (garbage_sum / garbager.size()) * girlsList.mechanic.size();
        details_stack.add(details);
        int now_garbage = garbage_sum - details;
        System.out.println("Механики переработали " + garbage_sum + " мусора и их осталось " +
                now_garbage);
        w.garbage.clear();
        w.garbage.add(now_garbage);
        System.out.println("Механики изготовили деталек: " + w.sum(details_stack));
        return w.sum(details_stack);
    }

    public List<Integer> getDetailsList() { return details_stack; }
    public List <Integer> getFuelTanksList(){ return fuel_tanks; }
}
