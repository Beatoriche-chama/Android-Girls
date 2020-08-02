import java.util.ArrayList;

public class Alchemy {
    private int fuel;
    Pick_Flowers pick_flowers = Pick_Flowers.getInstance();

    private Alchemy(){

    }

    public int alchemyFuel(ArrayList <NewAndroid> alchemy_gurlz) {
        int flower_sum = pick_flowers.getFlowers();
        if (fuel > flower_sum || flower_sum < 1) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Мало цветов");
        }
        else {
            int new_fuel_tank = alchemy_gurlz.size();
            fuel += new_fuel_tank;
            int now_flowers = flower_sum - new_fuel_tank;
            pick_flowers.setFlowers(now_flowers);
            System.out.println("После топлива сумма цветов: " + now_flowers);
        }
        return fuel;
    }

    public static Alchemy getInstance() {
        return Alchemy_Holder.alchemyInstance;
    }

    private static class Alchemy_Holder{
        private static final Alchemy alchemyInstance = new Alchemy();
    }

    public int getFuel() {
        return fuel;
    }

    public void setFuel(int new_sum) {
        fuel = new_sum;
    }

}
