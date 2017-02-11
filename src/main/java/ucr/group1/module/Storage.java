package ucr.group1.module;

import ucr.group1.query.Query;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Storage extends Module<String> {

    public double entriesANewQuery(Query query) {
        return 1;
    }

    public void aQueryIsServed() {

    }

    public void rejectQuery(Query query) {

    }

    public Query aQueryFinished() {
        return null;
    }

    public boolean confirmAliveQuery(Query query) {
        return false;
    }
}
