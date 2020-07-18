import java.io.IOException;

public class Alchemy {
    FileManage fileManage = new FileManage();

    public int alchemyFuel() throws IOException {
        int flower_sum = fileManage.fileLoad("C:/Users/User/Documents/NyanData/flower_count");
        int fuel = fileManage.fileLoad("C:/Users/User/Documents/NyanData/fuel_count");
        if (fuel > flower_sum || flower_sum < 1) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Мало цветов");
        }
        else {
            fuel += 1 * fileManage.fileLoad("C:/Users/User/Documents/NyanData/alchemy_girls_count");
            int now_flowers = flower_sum - fuel;
            fileManage.fileSave("C:/Users/User/Documents/NyanData/flower_count", now_flowers);
            fileManage.fileSave("C:/Users/User/Documents/NyanData/fuel_count", fuel);
            System.out.println("После топлива сумма цветов: " + now_flowers);
        }
        return fuel;
    }

}


