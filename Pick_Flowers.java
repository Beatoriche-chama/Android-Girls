import java.io.IOException;

public class Pick_Flowers {
    FileManage fileManage = new FileManage();
    GirlsList girlsList = new GirlsList();

    public static void main(String[] args) {
    }

    public int flowerPicker() throws IOException {
        int flower_picked = fileManage.fileLoad("flower_count");
        flower_picked += 1 * girlsList.flower_girls.size();
        System.out.println("Текущая сумма цветов: " + flower_picked);
        fileManage.fileSave("flower_count", flower_picked);
        return flower_picked;
    }

}
