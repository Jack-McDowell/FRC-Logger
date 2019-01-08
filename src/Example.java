import FRCLogger.Logger;
import FRCLogger.LoggingMode;
import FRCLogger.Output.DatabaseConnection;
import FRCLogger.Output.LogFile;
import FRCLogger.SQLType.*;
import FRCLogger.Table;

public class Example {
    public static void main(String[] args){
        //Create the logger
        Logger logger = new Logger();

        //Create a table
        Table t = new Table(new DatabaseConnection("127.0.0.1:3306", "sys", "root", "root"),
                "Test", LoggingMode.CRITERIA,
                new String[]{"Word", "ABoolean", "AnInt", "ADecimal"},
                new Type[]{new Varchar(), new Bool(), new Int(), new Decimal()});

        //Add only add if the tick is a multiple of 6
        t.addLoggingCriteria(() -> logger.getTick() % 2 == 0);
        t.addLoggingCriteria(() -> logger.getTick() % 3 == 0);

        //Set the default loggables
        t.setLoggers(() -> "Yeah, I'm using the same word", () -> (int)(Math.random() + 0.5), () -> ((int)(20 * Math.random())), Math::random);

        //Add the table to the logger
        logger.addTable(t);

        //Add a second location to log
        logger.getTable("Test").addOutputMethod(new LogFile("./logs"));

        //Run 100 ticks
        for(int i = 0; i < 100; i++) {
            logger.tick();
            if(i == 50)
                //Change a loggable halfway through
                t.setLogger("Word", () -> "Oh huh, it changed");
        }
    }
}
