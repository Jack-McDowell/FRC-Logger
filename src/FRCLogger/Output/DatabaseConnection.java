package FRCLogger.Output;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DatabaseConnection implements OutputMethod {
    public String location;
    public String username;
    public String password;
    Connection connection;
    int failures = 0;

    /**
     * Creates a connection to a postgres database.
     * @param ip Network location of a database. Of the format "xx.xx.xx.xx:yyyyy"
     * @param dbName Name of the database
     * @param username Username used to access database
     * @param password Password used to access database
     */
    public DatabaseConnection(String ip, String dbName, String username, String password){
        this.location = "jdbc:postgresql://" + ip + "/" + dbName;
        this.username = username;
        this.password = password;
        try {
            connection = DriverManager.getConnection(location, username, password);
        } catch(Exception e){
            e.printStackTrace();
            failures = 1000;
        }
    }

    /**
     * Initialize a table in the database. Deletes any prior table with the same name.
     * Note: this method is meant to only be an internal library call
     * @param tableName Name of the table. Should only consist of numbers, letters, and underscores
     * @param columnNames Names of the columns in the table
     * @param dataTypes Datatypes for columns in the table.
     */
    @Override public void init(String tableName, String[] columnNames, String[] dataTypes){
        for(String columnName : columnNames)
            if(!columnName.matches("[a-zA-Z0-9_]+"))
                throw new Error("Table could not be created! Invalid column name: " + columnName);
        runQuery("DROP TABLE IF EXISTS " + tableName + ";");
        runQuery("CREATE TABLE " + tableName + "(" + IntStream.range(0, columnNames.length).mapToObj(i -> columnNames[i] + " " + dataTypes[i]).reduce((s1, s2) -> s1 + ", " + s2).get() + ");");
    }

    /**
     * Inserts a new log in to the table
     * @param tableName The table to insert the log in to
     * @param values The values to log
     * @param columnNames The columns in which each value belongs
     */
    @Override public void update(String tableName, String[] values, String[] columnNames){
        String query = "INSERT INTO " + tableName + " (" + Stream.of(columnNames).reduce((s1, s2) -> s1 + ", " + s2).get() + ") VALUES (" + Stream.of(values).reduce((s1, s2) -> s1 + ", " + s2).get() + ");";
        runQuery(query);
    }

    /**
     * Runs a SQL query on the connected database. If 10 failures in a row occur or database creation fails, this does nothing.
     * @param query The query to run
     */
    public void runQuery(String query){
        System.out.println(query);
        try {
            if(failures > 10)
                return;
            connection.createStatement().execute(query);
            failures = 0;
        } catch(Exception e){
            failures++;
            e.printStackTrace();
        }
    }
}
