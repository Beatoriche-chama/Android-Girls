public class EnergyGenerator {
    GirlsLists girlsLists = GirlsLists.getInstance();
    Alchemy alchemy = Alchemy.getInstance();
    private int energy;

    private EnergyGenerator(){

    }

    public static EnergyGenerator getInstance(){
        return EnergyGeneratorHolder.energyGeneratorInstance;
    }

    private static class EnergyGeneratorHolder{
        private static final EnergyGenerator energyGeneratorInstance = new EnergyGenerator();
    }

    public int giveEnergy() {
        //если не сломан
        return energy += 10;
    }

    public void eatFuel(){
        //для более мощных топливных генераторов
        int sum_fuel = alchemy.getFuel();
        alchemy.setFuel(energy - sum_fuel);
    }

    public int eatEnergy() {
        int girls = girlsLists.getAll_girls();
        System.out.println("Девочек сейчас ест " + girls);
        return energy -= 5 * girls;
    }

    public int getEnergy(){
        return energy;
    }

    public void setEnergy(int x){
        energy = x;
    }

}
