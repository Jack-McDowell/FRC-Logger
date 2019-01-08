package FRCLogger.SQLType;

/**
 * Represents an integer value. Can accept longs, ints, shorts, bytes, or strings consisting of any of the above (in base 10)
 */
public class Int implements Type {
    /**
     * Determines if an object can be an Int
     * @param o The object to check
     * @return `true` if o is a long, int, short, byte, or string consisting of any of the above (in base 10). Returns `false` otherwise
     */
    @Override public boolean validate(Object o){
        if(o instanceof Integer || o instanceof Short || o instanceof Byte || o instanceof Long)
            return true;
        try {
            long i = Long.parseLong(toString().toLowerCase());
        } catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Returns the SQL datatype
     * @return "int"
     */
    @Override public String toString(){
        return "int";
    }
}
