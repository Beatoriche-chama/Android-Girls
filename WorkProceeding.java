import java.util.ArrayList;
import java.util.List;

public class WorkProceeding {
    Lists data = Lists.getInstance();

    public ArrayList<ResourcesWrapper> giveItem(int workerCount, String resource) {
        ResourcesWrapper res = data.getRes(resource);
        int resourceSum = res.getValue() + workerCount;
        res.setValue(resourceSum);
        ArrayList <ResourcesWrapper> resources = new ArrayList<>();
        resources.add(res);
        return resources;
    }

    public ArrayList<ResourcesWrapper> converseItem(String product) {
        ArrayList<ResourcesWrapper> resourcesList = new ArrayList<>();
        //System.out.println(product + " ПРОДУКТЫ НА КОНВЕРСИЮ");
        String [] s = product.split(" ");
        for(String res : s){
            ResourcesWrapper productObj = data.getRes(res);
            List<ResourcesWrapper> sourceObjects = productObj.getSource();
            if (!data.isReadyToCraft(sourceObjects)) {
                System.out.println("Мало исходного ресурса");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                continue;
            } else {
                int workerNumber = data.getWorkersNumber(res, true);
                productObj.setValue(productObj.getValue() + workerNumber);
                for (ResourcesWrapper sourceRes : sourceObjects) {
                    sourceRes.setValue(sourceRes.getValue() - workerNumber);
                }
            }
            resourcesList.add(productObj);
        }

        return resourcesList;
    }



}
