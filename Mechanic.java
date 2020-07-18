import java.io.IOException;

public class Mechanic {
    FileManage fileManage = new FileManage();

    public int mechanicDetails () throws IOException {
        int garbage_sum = fileManage.fileLoad("C:/Users/User/Documents/NyanData/garbage_count");
        int details = fileManage.fileLoad("C:/Users/User/Documents/NyanData/details_count");
        if (details > garbage_sum || garbage_sum < 1) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Мало мусора");
        }
        else {
            details += 1 * fileManage.fileLoad("C:/Users/User/Documents/NyanData/mechanic_girls_count");
            int now_garbage = garbage_sum - details;
            fileManage.fileSave("C:/Users/User/Documents/NyanData/garbage_count", now_garbage);
            fileManage.fileSave("C:/Users/User/Documents/NyanData/details_count", details);
        }
        return details;
    }

}
