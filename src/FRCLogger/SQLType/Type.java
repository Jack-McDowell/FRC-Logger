package FRCLogger.SQLType;

/**
 * Type provides a method for typechecking values logged in the database.
 * An instance of Type is created for each column. That instance is responsible for assuring all values
 * inserted in to the column match the column's type in the database.
 */
public interface Type {

    /**
     * Determines if an object can typecheck with the given type.
     * @param s The object to check
     * @return Whether or not it can be of the given type
     */
    boolean validate(Object s);

    /**
     * Formats an object in to a string that can match a given type to an appropriate form. For example, 1 is a valid Bool, but
     * the database needs it to be either true or false, so this would take care of that
     * @param s The object to format
     * @return The formatted object
     */
    default String reformat(Object s){ return s.toString(); }
}
