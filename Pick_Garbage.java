import java.util.ArrayList;

public class Pick_Garbage {
    private int garbage;

    private Pick_Garbage(){

    }

    public int garbagePicker(ArrayList <NewAndroid> garbage_picker) {
        int garbage_picked = garbage_picker.size();
        garbage += garbage_picked;
        return garbage;
    }

    public static Pick_Garbage getInstance() {
        return Pick_Garbage.Pick_Garbage_Holder.garbageInstance;
    }

    private static class Pick_Garbage_Holder{
        private static final Pick_Garbage garbageInstance = new Pick_Garbage();
    }

    public int getGarbage() {
        return garbage;
    }

    public void setGarbage(int new_sum) {
        garbage = new_sum;
    }
}
