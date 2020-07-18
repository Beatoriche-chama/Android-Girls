import java.io.IOException;

public class Pick_Flowers {
    FileManage fileManage = new FileManage();

    public int flowerPicker() throws IOException {
        int flower_picked = fileManage.fileLoad("C:/Users/User/Documents/NyanData/flower_count");
        flower_picked += 1 * fileManage.fileLoad("C:/Users/User/Documents/NyanData/flower_girls_count");
        System.out.println("Текущая сумма цветов: " + flower_picked);
        fileManage.fileSave("flower_count", flower_picked);
        return flower_picked;
    }

}
