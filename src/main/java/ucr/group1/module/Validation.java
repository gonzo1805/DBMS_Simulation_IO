package ucr.group1.module;

import ucr.group1.query.Query;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Validation extends Module<String> {

    public void entriesANewQuery(Query query) {

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
