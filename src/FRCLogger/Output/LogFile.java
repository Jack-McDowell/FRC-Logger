package FRCLogger.Output;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.stream.Stream;

public class LogFile implements OutputMethod {
    File folder;
    HashMap<String, FileWriter> writers = new HashMap<>();

    /**
     * Creates a new LogFile object which will store files in the specified directory. Files will be named the table name and be csv files.
     * @param filePath The directory in which the file should be declared
     */
    public LogFile(String filePath){
        folder = new File(filePath);
        folder.mkdir();
        if(!folder.exists())
            throw new Error("Folder could not be created");
        Stream.of(folder.listFiles()).forEach(f -> f.delete());
    }

    /**
     * Creates a file for the table and prints the headers.
     * @param tableName The name of the file. .csv will be appended to the end of this.
     * @param columnNames The names of the columns in the file
     * @param dataTypes Not used in this output form
     */
    @Override public void init(String tableName, String[] columnNames, String[] dataTypes){
        File f = new File(folder, tableName + ".csv");
        try {
            FileWriter bw = (new FileWriter(f));
            writers.put(tableName, bw);
            bw.write(Stream.of(columnNames).reduce((s1, s2) -> s1 + ", " + s2).get() + "\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Adds a log to the table
     * @param tableName The name of the table to add to
     * @param values The values to add
     * @param columnNames The names of the columns
     */
    @Override public void update(String tableName, String[] values, String[] columnNames){
        try {
            writers.get(tableName).write(Stream.of(values).reduce((s1, s2) -> s1 + ", " + s2).get() + "\n");
            writers.get(tableName).flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
