package ucr.group1.event;

import java.util.Comparator;

/**
 * Created by Gonzalo and Daniel on 2/11/2017.
 */
public class EventComparator implements Comparator<Event> {

    /**
     * Compare the 2 Events by the time attribute of both
     *
     * @param o1 the first event
     * @param o2 the second event
     * @return the which event occurs earlier
     */
    public int compare(Event o1, Event o2) {
        if (o1.getTime() == o2.getTime()) {
            return 0;
        } else if (o1.getTime() > o2.getTime()) {
            return 1;
        }
        return -1;
    }
}
