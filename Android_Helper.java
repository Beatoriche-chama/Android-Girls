
import java.util.List;
import java.util.Random;

public class Android_Helper {
    public static void main(String[] args) {

    }

    public int sum(List<Integer> list) {
        int sum = 0;
        for (int i : list)
            sum = sum + i;
        return sum;
    }

    public String giveMeName(){
        String [] names = {"Лили", "Ханако", "Рин", "Эми", "Шизуне", "Миша"};
        int idx = new Random().nextInt(names.length);
        String random_name = (names[idx]);
        return random_name;
    }



    public String giveMejob(){
        String [] jobs = {"Механик", "Собиратель запчастей", "Алхимик", "Собиратель цветочков", "Монахиня"};
        int idx = new Random().nextInt(jobs.length);
        String random_job = (jobs[idx]);
        return random_job;
    }

    public int giveMeicon(){
        Random rand = new Random();
        int randomNum = rand.nextInt((5 - 1) + 1) + 1;
        return randomNum;
    }
}
