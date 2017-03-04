package ucr.group1.query;
import ucr.group1.generator.Generator;
import static ucr.group1.query.QueryLabel.*;

/**
 * Created by Daniel and Gonzalo on 8/2/2017.
 */


public class QueryType {

    /**
     * Attributes
     */
    private QueryLabel qType;
    private int priority;
    private boolean readOnly;

    /**
     * Builds a new QueryType with a random type
     *
     * @param generator A pointer to a Generator
     */
    public QueryType(Generator generator) {
        double randomNumber = generator.getProbability();
        if(randomNumber < 0.33){
            qType = JOIN;
            priority = 3;
            readOnly = true;
        }
        else if(randomNumber < 0.65){
            qType = SELECT;
            priority = 4;
            readOnly = true;
        }
        else if(randomNumber < 0.93){
            qType = UPDATE;
            priority = 2;
            readOnly = false;
        }
        else{
            qType = DDL;
            priority = 1;
            readOnly = false;
        }
    }

    /**
     * Builds a new QueryType with a specified type
     *
     * @param label The type of the QueryType to build
     */
    public QueryType(QueryLabel label) {
        qType = label;
        switch (label) {
            case JOIN:
                priority = 3;
                readOnly = true;
                break;
            case SELECT:
                priority = 4;
                readOnly = true;
                break;
            case UPDATE:
                priority = 2;
                readOnly = false;
                break;
            case DDL:
                priority = 1;
                readOnly = false;
                break;
        }
    }


    /************************************************ GETTERS *********************************************************/

    /**
     * @return The type of the queryType
     */
    public QueryLabel getType() {
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
