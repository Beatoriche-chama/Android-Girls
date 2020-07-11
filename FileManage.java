import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileManage {

    public int fileLoad(String file_name) throws IOException {
        int amount = 0;
        if (Files.exists(Paths.get(file_name))) {
            String sum = new String(Files.readAllBytes(Paths.get(file_name)));
            amount = Integer.parseInt(sum);
            System.out.println("Из файла взяли " + amount + " чего-то");

        }

        if (Files.notExists(Paths.get(file_name))) {
            new BufferedWriter(new FileWriter(file_name));
            System.out.println("Создан новый файл по имени " + file_name);
        }
        return amount;
    }

    public void fileSave(String file_name, int sum) throws IOException {
        FileWriter rewriter = new FileWriter(file_name, false);
        rewriter.write(Integer.toString(sum));
        rewriter.flush();
    }

}