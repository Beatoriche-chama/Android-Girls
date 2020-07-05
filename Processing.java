import java.util.*;

public class Processing{
    List <String> alchemy_girls = Arrays.asList("Лямбдадельта", "Бернкастель", "Ника");
    List <String> flower_girls = Arrays.asList("Рика", "Сатоко", "Ханю");
    List<Integer> fuel_tanks = new ArrayList<>();
    Working w = new Working();

    public Processing(){
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                alchemyFuel(flower_girls);
                mechanicDetails();
            }
        };
        timer.schedule(task, 0, 15000);
    }

    public static void main(String[] args) {
        Processing p = new Processing();
    }

    public int alchemyFuel(List<String> flower_girls){
        int flower_sum = w.flowerPicker(flower_girls);
        int fuel = (flower_sum/ flower_girls.size()) * alchemy_girls.size();
        fuel_tanks.add(fuel);
        System.out.println("Топлива: " + w.sum(fuel_tanks));
        return w.sum(fuel_tanks);
    }

    public int mechanicDetails (){
        int details = 0;
        return details;
    }
}
