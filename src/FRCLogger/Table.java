package FRCLogger;

import FRCLogger.Output.OutputManager;
import FRCLogger.Output.OutputMethod;
import FRCLogger.SQLType.DateTime;
import FRCLogger.SQLType.Int;
import FRCLogger.SQLType.Type;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Code interface for a logging table.
 */
public class Table {
    OutputManager table;
    String name;
    Loggable[] loggables;
    LoggingMode loggingMode;
    ArrayList<LoggingCriteria> criterion = new ArrayList<>();

    /**
     * Creates a new table.
     * @param connection The method that the table should use to log. More can be added later.
     * @param tableName The name of the table to be created. Must consist of letters, numbers, and underscores
     * @param loggingMode The logging mode of the table. Options are CRITERIA, AUTOMATIC, and MANUAL. See LoggingMode.java for more descriptions of each
     * @param columnNames The names of the columns in the table. Tick number and timestamp are already included. You don't have to do that... Don't do that
     * @param dataTypes The type of value to goes in each of the columns. dataTypes[i] should match up with columnNames[i]
     */
    public Table(OutputMethod connection, String tableName, LoggingMode loggingMode, String[] columnNames, Type[] dataTypes){
        this.loggingMode = loggingMode;
        name = tableName;
        String[] columns = new String[columnNames.length + 2];
        Type[] types = new Type[columnNames.length + 2];
        System.arraycopy(columnNames, 0, columns, 2, columnNames.length);
        System.arraycopy(dataTypes, 0, types, 2, dataTypes.length);
        columns[0] = "Timestamp";
        columns[1] = "Tick_Number";
        types[0] = new DateTime();
        types[1] = new Int();
        if(!tableName.matches("(a-z|A-Z|0-9|_)+"))
            throw new Error("Invalid table name " + tableName);
        table = new OutputManager(connection, tableName, columns, types);
    }

    /**
     * Sets the loggers for the table. This is REQUIRED for automatic or criteria logging modes. logs[i].log() should return a value that matches
     * dataTypes[i] and will be placed in columns[i]
     * @param logs The loggable objects to get logs from. See Loggable.java for more
     */
    public void setLoggers(Loggable... logs){
        if(logs.length == table.columnCount() - 2){
            loggables = logs;
        } else throw new Error("Unable to set Loggables: Loggables do not match table");
    }

    /**
     * Sets the logger for a specific column
     * @param columnName The column to set the logger for
     * @param log the source for logs from the specified column
     */
    public void setLogger(String columnName, Loggable log){
        int index = table.columnIndex(columnName);
        if(index < 2)
            throw new Error("Tried to set logger in invalid column " + columnName);
        loggables[index - 2] = log;
    }

    /**
     * Sets the criteria under which the table should the log the values obtained from the loggables.
     * Only needed or used when logging mode is criteria. Note that having multiple criteria functions
     * as an AND. That is, all criteria must be met for a table to log.
     * @param criteria The criteria under which the table should log values.
     */
    public void addLoggingCriteria(LoggingCriteria criteria){
        criterion.add(criteria);
    }

    /**
     * Called by Logger.java. This logs the values retrieved from the loggables. Can also be called explicitly
     * @param tick The tick number
     */
    public void log(int tick) {
        try {
            Object[] logValues = new Object[loggables.length];
            int i = 0;
            for (Loggable l : loggables)
                logValues[i++] = l.log();
            log(tick, logValues);
        } catch (Exception e) {
            System.err.println("Error occurred from automatic logging. Check your loggables!");
            e.printStackTrace();
        }
    }

    /**
     * Logs values in the OutputManager.
     * Can be called manually, but should only really be done when logging mode is set to MANUAL. It's automatically called when
     * it should be in automatic and criteria modes.
     * This will match the arguments to their respective data types. See Type.java for more
     * @param tick The tick number
     * @param args The arguments to log. These can be any data type.
     */
    public void log(int tick, Object... args){
        String[] logValues = new String[table.columnCount()];
        logValues[0] = table.getDataType(0).reformat(new Timestamp(System.currentTimeMillis()));
        logValues[1] = table.getDataType(1).reformat(tick);
        if(args.length == 0){
            assert loggables != null && loggables.length == table.columnCount() - 2 : "Unable to log: Loggables do not match table";
            for (int i = 0; i < loggables.length; i++) {
                Object log = loggables[i].log();
                assert table.getDataType(i + 2).validate(log) : "Unable to log: " + log.toString() + " does not conform to type " + table.getDataType(i + 2).toString()
                        + ". Maybe the loggable is returning the wrong value?";
                logValues[i + 2] = table.sanitize(table.getDataType(i + 2).reformat(loggables[i].log()));
            }
        }
        else {
            assert args.length == table.columnCount() - 2 : "Unable to log: log arguments do not match table";
            for (int i = 0; i < loggables.length; i++) {
                Object log = loggables[i].log();
                assert table.getDataType(i + 2).validate(log) : "Unable to log: " + log.toString() + " does not conform to type " + table.getDataType(i + 2).toString()
                        + ". Maybe the wrong value was passed in to the function?";
                logValues[i + 2] = table.sanitize(table.getDataType(i + 2).reformat(loggables[i].log()));
            }
        }
        table.log(logValues);
    }

    /**
     * Returns true if the logger should trigger an automatic log for this table
     * @return whether or not the logger should trigger an automatic log
     */
    public boolean shouldAutolog(){
        return (loggingMode == LoggingMode.CRITERIA && criterion.stream().mapToInt(c -> c.shouldLog() ? 1 : 0).sum() == criterion.size()) || loggingMode == LoggingMode.AUTOMATIC;
    }

    /**
     * Adds an output method
     * @param outputMethod The output method to add
     */
    public void addOutputMethod(OutputMethod outputMethod){
        table.addOutputMethod(outputMethod);
    }

    /**
     * Returns the name of the table
     * @return the name of the table
     */
    public String getName(){
        return name;
    }
}
