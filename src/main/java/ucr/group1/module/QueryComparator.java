package ucr.group1.module;

import ucr.group1.query.Query;

import java.util.Comparator;

/**
 * Created by Gonzalo and Daniel on 2/11/2017.
 */
public class QueryComparator implements Comparator<Query> {

    public int compare(Query o1, Query o2) {
        if (o1.getDepartureTime() == o2.getDepartureTime()) {
            return 0;
        } else if (o1.getDepartureTime() > o2.getDepartureTime()) {
            return 1;
        }
        return -1;
    }
}
