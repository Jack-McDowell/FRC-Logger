package FRCLogger.SQLType;

import java.text.SimpleDateFormat;

/**
 * Matches datetime objects. This is intended to be used only for the timestamp column
 */
public class DateTime implements Type {
    /**
     * Determines if an object matches a sql timestamp
     * @param o The objec to check
     * @return Whether or not it is a valid timestamp
     */
    @Override public boolean validate(Object o){
        try { new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parseObject(o.toString()); return true; } catch (Exception e) { return false; }
    }

    /**
     * Formats a timestamp by putting quotes around it
     * @param o The timestamp
     * @return The timestamp with quotes
     */
    @Override public String reformat(Object o){
        return "'" + o.toString() + "'";
    }

    /**
     * Returns the SQL datatype
     * @return "timestamp"
     */
    @Override public String toString(){
        return "timestamp";
    }
}
