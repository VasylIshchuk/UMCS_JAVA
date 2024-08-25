import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ICDCodeTabularOptimizedForMemory implements  ICDCodeTabular{
    private Map<String,String> description = new HashMap<>();
//    Reduced memory usage because processing occurs line by line. There is no need to load the entire file into memory.
    @Override
    public String getDescription( String code ) {
        File file = new File("icd10.txt");
        try (FileReader fileReader = new FileReader(file);
             BufferedReader icd10 = new BufferedReader(fileReader)) {
            String line;
            while(description.isEmpty() && (line = icd10.readLine()) != null) {
                String[] splitedLine = line.trim()
                        .split(" ", 2);
                if (splitedLine[0].equals(code)) {
                    description.put(splitedLine[0], splitedLine[1]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  description.get(code);
    }
}
