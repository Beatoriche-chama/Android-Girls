import java.io.Serializable;

public class NewAndroid implements Serializable {
    //здесь делают новых андроидов-девочек
    private int iconId, energyEat;
    private String name, version, info, job;
    private boolean isHelper;
    Android_Helper android_helper = new Android_Helper();
    Lists girlsLists = Lists.getInstance();

    public NewAndroid(boolean isHelper){
        this.iconId = android_helper.giveMeicon();
        this.name = android_helper.giveMeName();
        this.version = "n337";
        this.info = android_helper.giveMeInfo();
        this.job = "free";
        this.energyEat = 5;
        this.isHelper = isHelper;
        girlsLists.androids.add(this);
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

    public String getJob() {
        return job;
    }

    public int getEnergyEat(){
        return energyEat;
    }

    public boolean getIsHelper(){
        return isHelper;
    }

    public void setJob(String job_name){
        job = job_name;
    }
    private void upgradeGirl(){
        //upgrade объект-девочку: версия меняется с n337 до более крутой
        //версия добавляет бонус +10
        //требует больше энергии
    }


}
