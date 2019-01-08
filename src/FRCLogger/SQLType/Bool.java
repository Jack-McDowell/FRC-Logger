package FRCLogger.SQLType;

/**
 * Represents a boolean value. Can accept boolean objects, strings matching true or false, or 0 or 1
 */
public class Bool implements Type {

    /**
     * Determines if s is a valid boolean. Returns valid if s is a java boolean, "true", "false", 0, or 1
     * @param s The object to check
     * @return Whether or not it is a valid Bool
     */
    @Override public boolean validate(Object s){
        if(s instanceof Boolean)
            return true;
        try {
            int i = Integer.parseInt(s.toString());
            if(i == 0 || i == 1)
                return true;
        } catch (Exception e){
            return s.toString().toLowerCase().contains("true") || s.toString().toLowerCase().contains("false");
        }
        return false;
    }

    /**
     * Formats an object to a boolean
     * @param o the object to format
     * @return "true" if o is true, "true", or 1, and returns false otherwise
     */
    @Override public String reformat(Object o){
        try {
            int i = Integer.parseInt(o.toString());
            if(i == 0 || i == 1)
                return (i == 1) ? "1" : "0";
        } catch (Exception e){
            return o.toString().toLowerCase().contains("true") ? "1" : "0";
        }
        return "0";
    }

    /**
     * Returns the SQL name for the boolean type
     * @return "bool"
     */
    @Override public String toString(){
        return "bool";
    }
}
