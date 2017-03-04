package ucr.group1.module;

import ucr.group1.query.Query;

import java.util.Comparator;

/**
 * Created by Daniel and Gonzalo on 11/2/2017.
 */
public class QueriesExecutionModuleComparator implements Comparator<Query> {

    public int compare(Query o1, Query o2) {
        return o1.getPriority() - o2.getPriority();
    }
}
