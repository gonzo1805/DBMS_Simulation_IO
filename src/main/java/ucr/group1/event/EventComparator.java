package ucr.group1.event;

import java.util.Comparator;

/**
 * Created by Gonzalo on 2/11/2017.
 */
public class EventComparator implements Comparator<Event> {

    public int compare(Event o1, Event o2) {
        if (o1.getTime() == o2.getTime()) {
            return 0;
        } else if (o1.getTime() > o2.getTime()) {
            return 1;
        }
        return -1;
    }
}
