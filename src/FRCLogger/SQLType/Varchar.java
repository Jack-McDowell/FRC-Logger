package FRCLogger.SQLType;

/**
 * Represents a string of up to a maximum length. By default this is 255 characters, but this can be changed
 */
public class Varchar implements Type {
    int length;

    /**
     * Creates a varchar with the default length of 255
     */
    public Varchar(){
        length = 255;
    }

    /**
     * Creates a varchar with a specified length
     * @param length the length to create a varchar for
     */
    public Varchar(int length){
        this.length = length;
    }

    /**
     * Determines if an object can be a varchar of given length
     * @param s The object to check
     * @return `true` if the s.toString has fewer or equal characters than the maximum
     */
    @Override public boolean validate(Object s){
        return s.toString().length() <= length;
    }

    /**
     * Formats an object to have quotes surrounding its string representation
     * @param s The object to format
     * @return the formatted string
     */
    @Override public String reformat(Object s){
        return '"' + s.toString() + '"';
    }

    /**
     * Returns the sql datatype for the specified varchar
     * @return "varchar(length)"
     */
    @Override public String toString(){
        return "varchar(" + length + ")";
    }
}
