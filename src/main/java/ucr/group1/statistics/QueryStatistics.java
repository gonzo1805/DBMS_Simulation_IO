package ucr.group1.statistics;
import ucr.group1.query.Query;

/**
 * Created by Daniel and Gonzalo on 9/2/2017.
 */
public class QueryStatistics {

    /**
     * Attributes
     */
    private int numberRejectedQueries;
    private int numberKilledQueries;
    private double averageLifespan;
    private int numberAverageLifespan;

    /**
     * Constructor
     */
    public QueryStatistics(){
        numberRejectedQueries = 0;
        numberKilledQueries = 0;
        averageLifespan = 0;
        numberAverageLifespan = 0;
    }

    public double getAvgLifespanOfQuery(){
        return averageLifespan;
    }

    public int getNumberOfRejectedQueries(){
        return numberRejectedQueries;
    }

    public int getNumberKilledQueries() {
        return numberKilledQueries;
    }

    public void aNewQueryIsRejected(){
        numberRejectedQueries++;
    }

    public void aQueryIsKilled() {
        numberKilledQueries++;
    }

    public int getNumberServedQueries() {
        return numberAverageLifespan;
    }

    /**
     * Add a finished query to the general stats of Average Lifespan
     *
     * @param q
     */
    public void addFinishedQuery(Query q){
        numberAverageLifespan++;
        double percentage = (1.0-(1.0/numberAverageLifespan));
        averageLifespan *= percentage;
        averageLifespan += (1.0 - percentage)*q.getLifespan();
    }
}