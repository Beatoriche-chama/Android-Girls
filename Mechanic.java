import java.io.IOException;

public class Mechanic {
    //здесь механики изготавливают детали
    GirlsList girlsList = new GirlsList();
    FileManage fileManage = new FileManage();

    public int mechanicDetails () throws IOException {
        int garbage_sum = fileManage.fileLoad("garbage_count");
        int details = fileManage.fileLoad("details_count");
        if (details > garbage_sum || garbage_sum < 1) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Мало цветов");
        }
        else {
            details += 1 * girlsList.mechanic.size();
            int now_garbage = garbage_sum - details;
            fileManage.fileSave("garbage_count", now_garbage);
            fileManage.fileSave("details_count", details);
        }
        return details;
    }

}
