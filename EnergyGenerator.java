import java.io.IOException;

public class EnergyGenerator {
    FileManage fileManage = new FileManage();

    public void giveEnergy() throws IOException {
        //если не сломан
        int energy = 5 + fileManage.fileLoad("energy_count");
        fileManage.fileSave("energy_count", energy);
    }

    public void eatFuel() throws IOException {
        //для более мощных топливных генераторов
        int sum_fuel = fileManage.fileLoad("fuel_count");
        int now_fuel = fileManage.fileLoad("energy_count") - sum_fuel;
        fileManage.fileSave("fuel_count", now_fuel);
    }

    public int eatEnergy() throws IOException {
        //одна девочка раз в 10 секунд (прописано в GUI) ест 5 энергий!
        int girls = fileManage.fileLoad("all_girls_count");
        System.out.println("Девочек сейчас ест " + girls);
        int energy = fileManage.fileLoad("energy_count");
        System.out.println("Из файла взяли энергии " + energy);
        int now_energy = energy - 5 * girls;
        fileManage.fileSave("energy_count", now_energy);
        System.out.println("Девочка покушала. Осталось " + now_energy +
                " энергии.");
        return now_energy;
    }

}
