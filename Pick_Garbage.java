import java.io.IOException;

public class Pick_Garbage {
    //здесь девочки собирают мусор
    FileManage fileManage = new FileManage();

    public int garbagePicker() throws IOException {
        int garbage_picked = fileManage.fileLoad("C:/Users/User/Documents/NyanData/garbage_count");
        garbage_picked += 1 * fileManage.fileLoad("C:/Users/User/Documents/NyanData/garbagers_girls_count");
        fileManage.fileSave("garbage_count", garbage_picked);
        return garbage_picked;
    }
}
