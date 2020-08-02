import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileManage {


    public String fileLoad(String file_name, String category, int certainLine, boolean isNumber) throws IOException {
        int counter = 0;
        String data = null;
        String line;
        if(isNumber){
            data = "0";
        }
        if (Files.exists(Paths.get(file_name))) {
            BufferedReader reader = Files.newBufferedReader(Paths.get(file_name));
            try {
                while ((line = reader.readLine()) != null) {
                    counter++;
                    if (counter == certainLine) {
                        String[] new_line = line.split(category + "|;");
                        data = new_line[1];
                        reader.close();
                        break;
                    }
                }

            } catch (Exception e) {
                System.out.println("Нужной строчки в файле нет!");
            }
        } else {
            System.out.println("Нужного файла нет.");
        }

        return data;
    }


    public File fileCreate(String file_name) {
        File new_file = new File(file_name);
        System.out.println("Создан новый файл по имени " + file_name);
        return new_file;
    }

    public void fileSave(String file_name, String category, String data, int certainLine,
                         boolean isNumber) throws IOException {
        File temp = new File(file_name + "Temp");
        Path sourceFilePath = Paths.get(temp.getAbsolutePath());
        Path targetFilePath = Paths.get(file_name);
        PrintWriter rewriter = new PrintWriter(Files.newBufferedWriter(sourceFilePath));
        BufferedReader reader = Files.newBufferedReader(targetFilePath);
        String line;
        int counter = 0;
        while ((line = reader.readLine()) != null || counter < certainLine) {
            counter++;
            if (counter == certainLine) {
                if (isNumber) {
                    line = category + Integer.parseInt(data) + ";";
                } else {
                    line = category + data + ";";
                }
            }
            rewriter.println(line);
        }
        rewriter.close();
        reader.close();
        System.out.println("Удаление с этой линией " + certainLine);
        Files.delete(targetFilePath);
        Files.move(sourceFilePath, targetFilePath);
    }

    public void listSave(String file_name, ArrayList <String> list) throws IOException {
        new File(file_name);
        PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Paths.get(file_name)));
        for (String girls : list)
            pw.println(girls);
        pw.close();
    }

    public List<String> listLoad(String file_name) {
        List<String> list = null;
        if (Files.exists(Paths.get(file_name))) {
            Scanner s = null;
            try {
                s = new Scanner(new File(file_name));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            list = new ArrayList<>();
            while (s.hasNext()) {
                list.add(s.next());
            }
            s.close();
        }
        return list;
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

}
