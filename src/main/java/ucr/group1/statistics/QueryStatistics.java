package ucr.group1.statistics;
import ucr.group1.query.Query;

/**
 * Created by Daniel on 9/2/2017.
 */
public class QueryStatistics {

    private int numberRejectedQueries;
    private int numberKilledQueries;
    private double averageLifespan;
    private int numberAverageLifespan;

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

    public void addFinishedQuery(Query q){
        numberAverageLifespan++;
        double percentage = (1-(1/numberAverageLifespan));
        averageLifespan *= percentage;
        averageLifespan += (1 - percentage)*q.getLifespan();
    }
}