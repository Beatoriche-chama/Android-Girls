import java.util.ArrayList;

public class Pick_Flowers {

    private int flowers;
    private Pick_Flowers() {

    }

    public int flowerPicker(ArrayList<NewAndroid> flower_picker){
        int flowers_picked = flower_picker.size();
        flowers += flowers_picked;
        return flowers;
    }
    public static Pick_Flowers getInstance() {
        return Pick_Flowers_Holder.flowersInstance;
    }

    private static class Pick_Flowers_Holder{
        private static final Pick_Flowers flowersInstance = new Pick_Flowers();
    }

    public int getFlowers() {
        return flowers;
    }

    public void setFlowers(int new_sum) {
        flowers = new_sum;
    }

}
