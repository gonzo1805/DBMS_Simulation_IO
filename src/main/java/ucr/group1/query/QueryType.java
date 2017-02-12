package ucr.group1.query;
import ucr.group1.generator.Generator;

/**
 * Created by Daniel on 8/2/2017.
 */


public class QueryType {
    private type qType;
    private int priority;
    private boolean readOnly;

    public enum type {
        DDL,
        UPDATE,
        JOIN,
        SELECT;
    }

    public QueryType(Generator gener){
        double randomNumber = gener.getProbability();
        if(randomNumber < 0.33){
            qType = type.JOIN;
            priority = 3;
            readOnly = true;
        }
        else if(randomNumber < 0.65){
            qType = type.SELECT;
            priority = 4;
            readOnly = true;
        }
        else if(randomNumber < 0.93){
            qType = type.UPDATE;
            priority = 2;
            readOnly = false;
        }
        else{
            qType = type.DDL;
            priority = 1;
            readOnly = false;
        }
    }

    public type getType(){ return qType; }

    public int getPriority(){
        return priority;
    }

    public boolean getReadOnly(){
        return readOnly;
    }

}
