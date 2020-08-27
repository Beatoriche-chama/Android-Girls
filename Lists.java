import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lists implements Serializable {
    //private int energy, flowers, garbage, details, fuel;
    ArrayList<NewAndroid> androids = new ArrayList<>();
    ArrayList<EnergyGenerator> generators = new ArrayList<>();
    Map<String, Integer> resources = new HashMap<>();

    public int getNumberofVer(String regex) {
        int count = 0;

        for (NewAndroid k : androids) {
            String check = k.getVersion();
            if (check.matches(regex)) {
                count++;
            }
        }
        return count;
    }

    public int getJobs(String job) {
        int count = 0;
        for (NewAndroid k : androids) {
            String check = k.getJob();
            if (check.equals(job)) {
                count++;
            }
        }
        return count;
    }
/*
    public int giveEnergy() {
        //если не сломан
        return energy += 10;
    }

 */

    public int eatEnergy() {
        int girls = androids.size();
        System.out.println("Девочек сейчас ест " + girls);
        //поменять на то, сколько ест каждая девочка (аугментированные едят более 5 энергии в тик)
        int energy = getResource("energy");
        energy -= 5 * girls;
        setResource(energy, "energy");
        return energy;
    }

    public int getResource(String resourceName) {
        int res = 0;
        if(resources.containsKey(resourceName)){
            res = resources.get(resourceName);
        }
        return res;
    }

    public void setResource(int x, String resourceName) {
        resources.replace(resourceName, resources.get(resourceName), x);
    }

    private Lists() {

    }

    public static Lists getInstance() {
        return ListsHolder.listsInstance;
    }

    private static class ListsHolder {
        private static final Lists listsInstance = new Lists();
    }

    public ArrayList<NewAndroid> getGirlsList() {
        return androids;
    }

}
