import java.util.*;
import java.util.function.Consumer;

public class Processing {
    Working w = new Working();

    public static void main(String[] args) {
        Processing p = new Processing();
        p.alchemyFuel((sum_fuel)-> System.out.println("Девочки сделали: " + sum_fuel + " баков топлива."));
    }
    //здесь девочки-алхимики перерабатывают цветочки -> топливо
    //девочки-механики перерабатывают мусор -> механические детали

    public void alchemyFuel (Consumer<Integer> callback) {
        String[] alchemist = {"Лямбдадельта", "Бернкастель", "Ника"};
        String[] flower_picker = {"Рика", "Сатоко", "Ханю"};
        List <Integer> flower = new ArrayList<>();
        w.flowerPicker((previous_flowers) -> flower.addAll(previous_flowers));
        Timer timer = new Timer();
        List<Integer> fuel_tanks = new ArrayList<>();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int flower_sum = w.sum(flower);
                int fuel = (flower_sum/ flower_picker.length) * alchemist.length;
                fuel_tanks.add(fuel);
                int sum_fuel = w.sum(fuel_tanks);
                //в счетчике убрать переработанные цветочки
                callback.accept(sum_fuel);
            }
        };
        timer.schedule(task, 0, 15000);
    }

    public void mechanicDetails (){

    }
}
