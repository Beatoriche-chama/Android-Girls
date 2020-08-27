mport java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileManage {

    public static void main (String [] args) throws IOException, ClassNotFoundException {

        FileManage fileManage = new FileManage();
        ArrayList<NewAndroid> oldGirls = new ArrayList<>();
        NewAndroid helper = new NewAndroid(true);
        oldGirls.add(helper);
        NewAndroid anotherGirl = new NewAndroid(false);
        oldGirls.add(anotherGirl);
        System.out.println(helper.getInfo());
        System.out.println(anotherGirl.getInfo());
        fileManage.objectsSave("C:/Users/User/Downloads/Meow/NewFile", oldGirls);
        ArrayList<NewAndroid> newGirls;
        newGirls = (ArrayList<NewAndroid>)(Object) fileManage.objectsLoad(
                "C:/Users/User/Downloads/Meow/NewFile");
        System.out.println(newGirls.get(0).getInfo());
        System.out.println(newGirls.get(1).getInfo());



    }

    public void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (null != files) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        deleteDirectory(files[i]);
                    } else {
                        files[i].delete();
                    }
                }
            }
        }
        directory.delete();
    }

    public void objectsSave(String fileName, ArrayList<?> list) throws IOException {
        File f = new File(fileName);
        FileOutputStream fos = new FileOutputStream(f);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        for (Object s : list) {
            oos.writeObject(s);
            oos.flush();
        }
        oos.close();
        fos.close();
    }

    public ArrayList<Object> objectsLoad(String fileName) throws IOException, ClassNotFoundException {
        File f = new File(fileName);
        FileInputStream fis = new FileInputStream(f);
        ObjectInputStream input = new ObjectInputStream(fis);
        ArrayList <Object> objects = new ArrayList<>();
        boolean cont = true;
        while (cont) {
            try {
                Object obj = input.readObject();
                if (obj != null) {
                    System.out.println(obj);
                    objects.add(obj);
                } else {
                    cont = false;
                }
            }catch (EOFException e){
                System.out.println("End of file reached");
                break;
            }
        }
        input.close();
        fis.close();
        return objects;
    }

    public void mapSave(String fileName, Map<?, ?> map) throws IOException {
        Properties properties = new Properties();
        properties.putAll(map);
        FileOutputStream fos = new FileOutputStream(fileName);
        properties.store(fos, null);
        fos.close();
    }

    public Map<Object, Object> mapLoad(String fileName) throws IOException {
        Properties properties = new Properties();
        FileInputStream fis = new FileInputStream(fileName);
        properties.load(fis);
        return new HashMap<>(properties);
    }

}
