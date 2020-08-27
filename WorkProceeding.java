public class WorkProceeding {
    Lists data = Lists.getInstance();

    public int pickItem(int neetCount, String resource){
        int flowers_picked = neetCount;
        int resourceSum = data.getResource(resource);
        resourceSum += flowers_picked;
        data.setResource(resourceSum, resource);
        return resourceSum;
    }

    public int checkMe(int neetCount, String resource){
        System.out.println(neetCount);
        System.out.println(resource);
        return neetCount;
    }

    public int converseItem(int neetCount, String material, String product){
        int conversedSum = data.getResource(product);
        int source = data.getResource(material);
        if (source < 1) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Мало исходного ресурса");
        }
        else {
            int newConvercedItem = neetCount;
            conversedSum += newConvercedItem;
            source = source - newConvercedItem;
            data.setResource(conversedSum, product);
            data.setResource(source, material);
        }
        return conversedSum;
    }
}
