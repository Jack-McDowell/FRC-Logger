package FRCLogger;

/**
 * There are three types of tables: automatic, manual, and criteria tables.
 * When a table is created, its mode must be specified.
 */
public enum LoggingMode {
    /**
     * Automatic tables log values every tick, regardless of any condition or function calls.
     * In automatic mode, manually calling the log method is not disabled, but doing so will
     * generally result in the same log occurring twice in the database, as it's already called
     * each tick. For automatic tables, there must be a loggable object for each column in the table.
     */
    AUTOMATIC,

    /**
     * Manual tables log only when there is an explicit call to log in table.java.
     * For manual tables, the tick must be provided. In future versions, this may be eliminated, but
     * in order for rows to sync up in graphs, tick number must be present. The tick number can be
     * found by calling logger.getTick(). For manual tables, values can be either part of the function call,
     * or if none are supplied, values to log will be supplied from loggables.
     */
    MANUAL,

    /**
     * Criteria tables log values if and only if a certain criteria is met. This criteria must be set with
     * table.setLoggingCriteria(), or the table will fail to function. Just as with automatic, loggables must
     * be provided, and calling log explicitly is allowed but discouraged.
     */
    CRITERIA
}
