import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Alchemy{

    GirlsList girlsList = new GirlsList();
    List<Integer> fuel_tanks = new ArrayList<>();
    Android_Helper android_helper = new Android_Helper();

    public static void main(String[] args) {

    }

    public int alchemyFuel(){
        //считывает с файла Лист собранных листов и переписывает его с новой суммой
        String flower_sum_string = null;
        try {
            flower_sum_string = new String(Files.readAllBytes(Paths.get("flower_count.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int flower_sum = Integer.parseInt(flower_sum_string);
        int fuel = (flower_sum / girlsList.flower_girls.size()) * girlsList.alchemy_girls.size();
        fuel_tanks.add(fuel);
        int now_flowers = flower_sum - fuel;

        try {
            FileWriter rewriter = new FileWriter("flower_count.txt", false);
            rewriter.write(Integer.toString(now_flowers));
            rewriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return android_helper.sum(fuel_tanks);
    }

}


