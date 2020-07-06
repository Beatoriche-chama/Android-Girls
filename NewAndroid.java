public class NewAndroid {
    //здес делают новых андроидов-девочек

    Processing p = new Processing();
    Working w = new Working();
    Android_Helper android_helper = new Android_Helper();
    GirlsList girlsList = new GirlsList();

    public static void main(String[] args){

    }

    public void makingNewGirl(){
        int details_sum = w.sum(p.getDetailsList());
        int newGirl = 10;
        if (details_sum >=10) {
            int now_details = details_sum - newGirl;
            p.details_stack.clear();
            p.details_stack.add(now_details);
            String girl = android_helper.giveMeName();
            System.out.println("Появилась новая девочка~, ее зовут " + girl);
            girlsList.all_girls.add(girl);
        }
        else{System.out.println("Не хватает деталек");}

    }
}
