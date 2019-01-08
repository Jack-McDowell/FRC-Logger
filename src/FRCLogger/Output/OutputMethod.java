package FRCLogger.Output;

/**
 * A template for output methods for logging.
 * Default output methods for logging to a file and a database are included.
 */
public interface OutputMethod {
    /**
     * Should do whatever is necessary to initialize a logging table.
     * The name of the table is specified in tableName, the column names are
     * specified in columnNames, and the datatypes of the columns are specified
     * in dataTypes.
     * @param tableName The name of the table to initialize.
     * @param columnNames The names of the columns in the table to initialize.
     * @param dataTypes The datatypes of the columns in the table to initialize.
     */
    void init(String tableName, String[] columnNames, String[] dataTypes);

    /**
     * Should do whatever is necessary to log a new entry in a table.
     * The name of the table is specified in tableName, the column names are
     * specified in columnNames, and the values to log are specified
     * in values.
     * @param tableName The name of the table to log in.
     * @param values The values to log.
     * @param columnNames The columns under which the values should be logged.
     */
    void update(String tableName, String[] values, String[] columnNames);
}
