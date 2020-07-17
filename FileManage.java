import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManage {

    public int fileLoad(String file_name) throws IOException {
        int amount = 0;
        if (Files.exists(Paths.get(file_name))) {
            String sum = new String(Files.readAllBytes(Paths.get(file_name)));
            amount = Integer.parseInt(sum);
        }

        if (Files.notExists(Paths.get(file_name))) {
            fileCreate(file_name);
            fileSave(file_name, 0);
        }
        return amount;
    }

    public File fileCreate(String file_name) {
        File new_file = new File(file_name);
        System.out.println("Создан новый файл по имени " + file_name);
        return new_file;
    }

    public void fileSave(String file_name, int sum) throws IOException {
        FileWriter rewriter = new FileWriter(file_name, false);
        rewriter.write(Integer.toString(sum));
        rewriter.flush();
    }

    public void fileDelete(String file_name) throws IOException {
        Files.deleteIfExists(Paths.get(file_name));
    }

    public void girlSave(String file_name, String girl_name) throws IOException {
        FileWriter fileWriter = new FileWriter(file_name, true);
        BufferedWriter rewriter = new BufferedWriter(fileWriter);
        rewriter.write(girl_name);
        rewriter.newLine();
        rewriter.flush();
    }

    public List<String> girlsLoad(String file_name) {
        Scanner s = null;
        try {
            s = new Scanner(new File(file_name));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        List<String> list = new ArrayList<>();
        while (s.hasNext()) {
            list.add(s.next());
        }
        s.close();
        return list;
    }

    public void girlDelete(String file_name, String certain_name) throws IOException {
        File file = new File(file_name);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        FileWriter fileWriter = new FileWriter(file_name, false);
        BufferedWriter rewriter = new BufferedWriter(fileWriter);

        String lineToRemove = certain_name;
        String currentLine;
        int stop_indicator = 0;
        while ((currentLine = reader.readLine()) != null) {
            String trimmedLine = currentLine.trim();
            if (trimmedLine.equals(lineToRemove) && stop_indicator < 1) {
                stop_indicator = 1;
                continue;
            }
            rewriter.write(currentLine + System.getProperty("line.separator"));
        }
        rewriter.close();
        reader.close();
    }

}
