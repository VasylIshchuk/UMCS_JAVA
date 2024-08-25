import java.io.*;

public class PlantUMLRunner {
    public static String pathJarUml;

    public static void setPathJarUml(String pathJarUml) {
        PlantUMLRunner.pathJarUml = pathJarUml;
    }

    public static void generateDiagram(String data, String path, String fileName) {
        File catalog = new File(path);
        catalog.mkdirs();
        String filepath = catalog.getPath() + '/' + fileName + ".txt";

        try (FileWriter fileWriter = new FileWriter(filepath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(data);
            bufferedWriter.close();
            //  Creates a ProcessBuilder object that configures the command to start a process.
            //  In this case, it sets up to run a Java program with a JAR file, where "pathJarUml" is
            //  the path to the JAR file and "filepath" is the path to the text file.
            ProcessBuilder processBuilder =
                    new ProcessBuilder("java", "-jar",
                            pathJarUml, filepath);
            //  processBuilder.command("java", "-jar", pathJarUml, filepath);

            //  Starts the process that executes the command.
            Process process = processBuilder.start();
            //  Blocks the execution of the current thread in your program, waiting for the process launched with ProcessBuilder to complete.
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
