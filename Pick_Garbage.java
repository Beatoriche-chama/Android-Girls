import java.io.IOException;

public class Pick_Garbage {
    //здесь девочки собирают мусор
    GirlsList girlsList = new GirlsList();
    FileManage fileManage = new FileManage();

    public static void main(String[] args) {
    }

    public int garbagePicker() throws IOException {
        fileManage.fileLoad("garbage_count");
        int garbage_picked = 1 * girlsList.garbagers.size();
        fileManage.fileSave("garbage_count", garbage_picked);
        return garbage_picked;
    }
}
