import java.io.IOException;

public class NewAndroid {
    //здес делают новых андроидов-девочек
    Android_Helper android_helper = new Android_Helper();
    FileManage fileManage = new FileManage();

    public static void main(String[] args) {

    }

    public int makingNewGirl() throws IOException {
        int girls_count = fileManage.fileLoad("C:/Users/User/Documents/NyanData/all_girls_count");
        int details_sum = fileManage.fileLoad("C:/Users/User/Documents/NyanData/details_count");
        int newGirl = 10;
        if (details_sum >= 10) {
            int now_details = details_sum - newGirl;
            fileManage.fileSave("C:/Users/User/Documents/NyanData/details_count", now_details);
            String girl = android_helper.giveMeName();
            System.out.println("Появилась новая девочка~, ее зовут " + girl);
            fileManage.girlSave("C:/Users/User/Documents/NyanData/free_girls_names", girl);
            girls_count += girls_count;
            fileManage.fileSave("C:/Users/User/Documents/NyanData/all_girls_count", girls_count);
        } else {
            System.out.println("Не хватает деталек");
        }
        return girls_count;
    }
}
