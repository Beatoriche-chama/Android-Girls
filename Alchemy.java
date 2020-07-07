import java.util.*;

public class Alchemy{
    //здесь алхимики экстрактят топливо
    GirlsList girlsList = new GirlsList();
    List<Integer> fuel_tanks = new ArrayList<>();
    Pick_Flowers w = new Pick_Flowers();
    Android_Helper android_helper = new Android_Helper();

    public static void main(String[] args) {

    }

    public Alchemy(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override public void run() {
               alchemyFuel();
            }
        }, 0, 15000);
    }

    public int alchemyFuel(){
        int flower_sum = android_helper.sum(w.flowers);
        System.out.println(flower_sum);
        int fuel = (flower_sum / girlsList.flower_girls.size()) * girlsList.alchemy_girls.size();
        fuel_tanks.add(fuel);
        int now_flowers = flower_sum - fuel;
        w.flowers = new ArrayList<>();
        w.flowers.add(now_flowers);
        return android_helper.sum(fuel_tanks);
    }

    public List <Integer> getFuelTanksList(){ return fuel_tanks; }
}


