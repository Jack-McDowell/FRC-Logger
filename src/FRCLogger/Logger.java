package FRCLogger;

import FRCLogger.Output.LogFile;
import FRCLogger.Output.OutputMethod;
import FRCLogger.SQLType.Type;

import java.util.HashMap;

/**
 * Driver class for the logging system.
 */
public class Logger {
    int tick = -1;
    HashMap<String, HashMap<String, Type>> implicitTables = new HashMap<>();
    HashMap<String, HashMap<String, Loggable>> implicitTableLoggers = new HashMap<>();
    HashMap<String, OutputMethod> implicitTableOutput = new HashMap<>();
    HashMap<String, Table> tables = new HashMap<>();
    OutputMethod defaultOutput;
    public Logger(OutputMethod defaultOutput){
        this.defaultOutput = defaultOutput;
    }
    public Logger(){
        this.defaultOutput = new LogFile("./logs");
    }
    public int getTick(){ return tick; }

    /**
     * Creates a new column in a specified table. If the table does not exist, it is created.
     * Note that this only functions before the first call of tick, as most forms of output
     * cannot have more columns added once logging has begun. Additionally, if a table and its
     * columns are explicitly defined by invoking the constructor of a table object, this cannot
     * be used to add columns to it.
     *
     * Note that this is not the intended method for logging, and as such, it is not fully fleshed
     * out or user friendly yet. Many features will require a call to getTable and methods from the
     * table object returned.
     *
     * TODO: Add support for adding columns to explicitly defined tables
     * TODO: Add support for adding columns to multiple tables at one time
     * @param tableName Name of the table to add a column to (and create, if necessary)
     * @param columnName The name of the column to add
     * @param columnType The datatype of the column to add
     * @param logger the source of data for the column to add
     */
    public void addColumn(String tableName, String columnName, Type columnType, Loggable logger){
        assert tick != -1 : "Cannot add columns after logging has begun";
        if(!implicitTables.containsKey(tableName)) {
            implicitTables.put(tableName, new HashMap<>());
            implicitTableLoggers.put(tableName, new HashMap<>());
        }
        implicitTables.get(tableName).put(columnName, columnType);
        implicitTableLoggers.get(tableName).put(columnName, logger);
    }

    /**
     * Sets the default output method of a table defined using addColumn above. This does not function
     * with tables defined by invoking the table constructor. It must be called before the first tick
     * call, or else it will fail
     * @param tableName The table to set the output for.
     * @param outputMethod The output method for the table
     */
    public void setTableOutput(String tableName, OutputMethod outputMethod){
        implicitTableOutput.put(tableName, outputMethod);
    }

    /**
     * Defines final tables based on columns added using addColumn if it is the first call to tick.
     * Each call increments the tick count (so only call it once per tick!).
     * Each call then loops through the tables and makes each automatic or conditional table meeting
     * its conditional criteria and calls log on each.
     */
    public void tick(){
        if(tick == -1){
            for(String s : implicitTables.keySet()){
                tables.put(s, new Table(implicitTableOutput.containsKey(s) ? implicitTableOutput.get(s) : defaultOutput, s,
                        LoggingMode.MANUAL, implicitTables.get(s).keySet().toArray(new String[0]), implicitTables.get(s).values().toArray(new Type[0])));
                for(String s1 : implicitTableLoggers.get(s).keySet())
                    tables.get(s).setLogger(s1, implicitTableLoggers.get(s).get(s1));
            }
        }

        tick++;
        for(Table t : tables.values()){
            if(t.shouldAutolog())
                t.log(tick);
        }
    }

    /**
     * Adds a table
     * @param t The table to add
     */
    public void addTable(Table t){
        tables.put(t.getName(), t);
    }

    /**
     * Gets a table specified by its name
     * @param tableName The name of the table to get
     * @return The table object
     */
    public Table getTable(String tableName){
        return tables.get(tableName);
    }
}
