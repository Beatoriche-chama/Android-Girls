import java.io.IOException;

public class NewAndroid {
    //здес делают новых андроидов-девочек
    Android_Helper android_helper = new Android_Helper();
    FileManage fileManage = new FileManage();

    public static void main(String[] args) {

    }

    public int makingNewGirl() throws IOException {
        int girls_count = fileManage.fileLoad("all_girls_count");
        int details_sum = fileManage.fileLoad("details_count");
        int newGirl = 10;
        if (details_sum >= 10) {
            int now_details = details_sum - newGirl;
            fileManage.fileSave("details_count", now_details);
            String girl = android_helper.giveMeName();
            System.out.println("Появилась новая девочка~, ее зовут " + girl);
            fileManage.girlSave("free_girls_names", girl);
            girls_count += girls_count;
            fileManage.fileSave("all_girls_count", girls_count);
        } else {
            System.out.println("Не хватает деталек");
        }
        return girls_count;
    }
}
