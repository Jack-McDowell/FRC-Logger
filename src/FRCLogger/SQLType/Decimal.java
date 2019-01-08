package FRCLogger.SQLType;

/**
 * Represents a decimal value. Can accept doubles, floats, or strings consisting of a double/float
 */
public class Decimal implements Type {
    /**
     * Determines if an object can be a decimal
     * @param o The object to check
     * @return `true` if o is a float, a double, or a string containing only a float/double. Returns `false` otherwise
     */
    @Override public boolean validate(Object o){
        if(o instanceof java.lang.Double || o instanceof Float)
            return true;
        try {
            double i = java.lang.Double.parseDouble(o.toString());
        } catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * Returns the SQL datatype
     * @return "double"
     */
    @Override public String toString(){
        return "double";
    }
}
