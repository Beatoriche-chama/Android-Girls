import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class Pick_Flowers {
    BufferedWriter wr;

    GirlsList girlsList = new GirlsList();
    List<Integer> flowers = new ArrayList<>();
    Android_Helper android_helper = new Android_Helper();


    public static void main(String[] args) {
    }

    public int flowerPicker() throws IOException {
        //записывает в файл Лист собранных цветов
        int flower_picked = 0;
        if (Files.exists(Paths.get("flower_count.txt"))) {
            String flower_sum_string = new String(Files.readAllBytes(Paths.get("flower_count.txt")));
            flower_picked = Integer.parseInt(flower_sum_string);
            System.out.println("Из .txt взяли " + flower_picked + " цветов");
            flower_picked = (1 * girlsList.flower_girls.size()) + flower_picked;
            System.out.println("А теперь вместе с прошлыми стало: " + flower_picked + " цветов");

        }
        if (Files.notExists(Paths.get("flower_count.txt"))) {
            wr = new BufferedWriter(new FileWriter("flower_count.txt"));
            flower_picked = 1 * girlsList.flower_girls.size();

        }
        flowers = new ArrayList<>();
        flowers.add(flower_picked);
        int sum_flowers = android_helper.sum(flowers);
        System.out.println("Текущая сумма цветов: " + sum_flowers);
        FileWriter rewriter = new FileWriter("flower_count.txt", false);
        rewriter.write(Integer.toString(sum_flowers));
        rewriter.flush();
        return sum_flowers;
    }


}
