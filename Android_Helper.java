import java.util.Random;

public class Android_Helper {

    public String giveMeName(){
        String [] names = {"Лили", "Ханако", "Рин", "Эми", "Шизуне", "Миша"};
        int idx = new Random().nextInt(names.length);
        String random_name = (names[idx]);
        return random_name;
    }

    public String giveMeInfo(){
        String [] info = {"Любит петь", "Хочет спать весь день~", "Мечтает о звездах"};
        int idx = new Random().nextInt(info.length);
        String random_info = (info[idx]);
        return random_info;
    }

    public int giveMeicon(){
        Random rand = new Random();
        int randomNum = rand.nextInt((5 - 1) + 1) + 1;
        return randomNum;
    }
}
