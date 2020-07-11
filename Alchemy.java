import java.io.IOException;

public class Alchemy {

    GirlsList girlsList = new GirlsList();
    FileManage fileManage = new FileManage();

    public static void main(String[] args) {

    }

    public int alchemyFuel() throws IOException {
        int flower_sum = fileManage.fileLoad("flower_count");
        int fuel = fileManage.fileLoad("fuel_count");
        if (fuel > flower_sum || flower_sum < 1) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Мало цветов");
        }
        else {
            fuel += 1 * girlsList.alchemy_girls.size();
            int now_flowers = flower_sum - fuel;
            fileManage.fileSave("flower_count", now_flowers);
            fileManage.fileSave("fuel_count", fuel);
            System.out.println("После топлива сумма цветов: " + now_flowers);
        }
        return fuel;
    }

}
