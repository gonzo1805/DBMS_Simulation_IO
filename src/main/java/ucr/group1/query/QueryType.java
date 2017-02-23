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

    /**
     * Builds a new QueryType with a random type
     *
     * @param generator A pointer to a Generator
     */
    public QueryType(Generator generator) {
        double randomNumber = generator.getProbability();
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

    /**
     * Builds a new QueryType with a specified type
     *
     * @param type The type of the QueryType to build
     */
    public QueryType(type type) {
        switch (type) {
            case JOIN:
                qType = type.JOIN;
                priority = 3;
                readOnly = true;
                break;
            case SELECT:
                qType = type.SELECT;
                priority = 4;
                readOnly = true;
                break;
            case UPDATE:
                qType = type.UPDATE;
                priority = 2;
                readOnly = false;
                break;
            case DDL:
                qType = type.DDL;
                priority = 1;
                readOnly = false;
                break;
        }
    }


    /************************************************ GETTERS *********************************************************/

    /**
     * @return The type of the queryType
     */
    public type getType() {
        return qType;
    }

    /**
     * @return The priority of the queryType
     */
    public int getPriority(){
        return priority;
    }

    /**
     * @return True if and only if the is a read only queryType
     */
    public boolean getReadOnly(){
        return readOnly;
    }
}
