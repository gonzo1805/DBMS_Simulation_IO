package ucr.group1.module;

import ucr.group1.query.Query;

import java.util.concurrent.locks.Condition;

/**
 * Created by Gonzalo on 2/9/2017.
 */
public class Connection extends Module<String> {

    public Connection() {

    }

    public void entriesANewQuery(Query query) {

    }

    public void aQueryIsServed() {

    }

    public void rejectQuery(Query query) {

    }

    public Query aQueryFinished() {
        return null;
    }
}
