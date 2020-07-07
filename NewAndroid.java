public class NewAndroid {
    //здес делают новых андроидов-девочек

   Mechanic mechanic = new Mechanic();
    Android_Helper android_helper = new Android_Helper();
    GirlsList girlsList = new GirlsList();

    public static void main(String[] args){

    }

    public void makingNewGirl(){
        int details_sum = android_helper.sum(mechanic.getDetailsList());
        int newGirl = 10;
        if (details_sum >=10) {
            int now_details = details_sum - newGirl;
            mechanic.details_stack.clear();
            mechanic.details_stack.add(now_details);
            String girl = android_helper.giveMeName();
            System.out.println("Появилась новая девочка~, ее зовут " + girl);
            girlsList.all_girls.add(girl);
        }
        else{System.out.println("Не хватает деталек");}

    }
}
