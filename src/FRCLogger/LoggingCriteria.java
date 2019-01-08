package FRCLogger;

/**
 * In criteria logging mode, the table should only log if certain criterion are met.
 * This interface is intended to be used to determine if the criterion are met.
 * If shouldLog returns true, then the criteria table using it will log.
 * This does not affect automatic or manual logging tables
 */
public interface LoggingCriteria {
    /**
     * Returns whether or not the associated table should log
     * @return whether or not the associated table should log
     */
    boolean shouldLog();
}
