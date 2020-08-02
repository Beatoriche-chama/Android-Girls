import java.util.ArrayList;

public class Mechanic {
    private int details;
    Pick_Garbage pick_garbage = Pick_Garbage.getInstance();

    private Mechanic(){

    }

    public int mechanicDetails (ArrayList <NewAndroid> mechanic_gurlz){
        int garbage_sum = pick_garbage.getGarbage();
        if (garbage_sum < 1) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Мало мусора");
        }
        else {
            int new_detail = mechanic_gurlz.size();
            details += new_detail;
            int now_garbage = garbage_sum - new_detail;
            pick_garbage.setGarbage(now_garbage);
        }
        return details;
    }

    public static Mechanic getInstance() {
        return Mechanic_Holder.mechanicInstance;
    }

    private static class Mechanic_Holder{
        private static final Mechanic mechanicInstance = new Mechanic();
    }

    public int getDetails() {
        return details;
    }

    public void setDetails(int new_sum) {
        details = new_sum;
    }

}
