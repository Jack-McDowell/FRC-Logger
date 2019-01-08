package FRCLogger;

/**
 * In automatic or criteria tables, values are not updated by the programmer each time it logs.
 * Instead, the table automatically retrieves the values from the logger. Instructions for
 * connecting loggables and a table are in table.java.
 *
 * Usage of loggables is still encouraged for manual mode, but not required.
 */
public interface Loggable {
    /**
     * Retrieves the value to log at any given time.
     * @return The value to log.
     */
    Object log();
}
