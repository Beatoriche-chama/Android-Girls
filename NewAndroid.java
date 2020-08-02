public class NewAndroid {
    //здесь делают новых андроидов-девочек
    private int iconId;
    private String name, version, info;
    Android_Helper android_helper = new Android_Helper();
    GirlsLists girlsLists = GirlsLists.getInstance();
    Mechanic mechanic = Mechanic.getInstance();

    public NewAndroid(){
        this.iconId = android_helper.giveMeicon();
        this.name = android_helper.giveMeName();
        this.version = "n337";
        this.info = android_helper.giveMeInfo();
        makingNewGirl();
    }

    public int getIconId(){
        return iconId;
    }

    public String getName(){
        return name;
    }

    public String getVersion(){
        return version;
    }

    public String getInfo(){
        return info;
    }



    private void upgradeGirl(){
        //upgrade объект-девочку: версия меняется с n337 до более крутой
        //версия добавляет бонус +10
    }

    public void makingNewGirl() {
        int girls_count = girlsLists.getAll_girls();
        int details_sum = mechanic.getDetails();
        int newGirl = 10;
        if (details_sum >= 10) {
            int now_details = details_sum - newGirl;
            mechanic.setDetails(now_details);
            girls_count += girls_count;
            girlsLists.setAll_girls(girls_count);
        } else {
            System.out.println("Не хватает деталек");
        }

    }
}
