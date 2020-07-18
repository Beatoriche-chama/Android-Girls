import java.io.IOException;

public class EnergyGenerator {
    FileManage fileManage = new FileManage();

    public void giveEnergy() throws IOException {
        //если не сломан
        int energy = 5 + fileManage.fileLoad("C:/Users/User/Documents/NyanData/energy_count");
        fileManage.fileSave("C:/Users/User/Documents/NyanData/energy_count", energy);
    }

    public void eatFuel() throws IOException {
        //для более мощных топливных генераторов
        int sum_fuel = fileManage.fileLoad("C:/Users/User/Documents/NyanData/fuel_count");
        int now_fuel = fileManage.fileLoad("C:/Users/User/Documents/NyanData/energy_count") - sum_fuel;
        fileManage.fileSave("C:/Users/User/Documents/NyanData/fuel_count", now_fuel);
    }

    public int eatEnergy() throws IOException {
        //одна девочка раз в 10 секунд (прописано в GUI) ест 5 энергий!
        int girls = fileManage.fileLoad("C:/Users/User/Documents/NyanData/all_girls_count");
        System.out.println("Девочек сейчас ест " + girls);
        int energy = fileManage.fileLoad("C:/Users/User/Documents/NyanData/energy_count");
        System.out.println("Из файла взяли энергии " + energy);
        int now_energy = energy - 5 * girls;
        fileManage.fileSave("C:/Users/User/Documents/NyanData/energy_count", now_energy);
        System.out.println("Девочка покушала. Осталось " + now_energy +
                " энергии.");
        return now_energy;
    }

}
