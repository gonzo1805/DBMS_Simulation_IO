package ucr.group1.ui;

/**
 * Class made only to display data on the UI
 * Created by Gonzalo and Daniel on 3/3/2017.
 */
public class Printer {

    /**
     * Attributes
     */
    int time;
    String event;
    String log;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    /**
     * Constructor
     *
     * @param time  The time at the query is processed
     * @param event The event that is processes
     * @param log   The line that is gonna be displayed on textArea
     */
    public Printer(int time, String event, String log) {
        this.time = time;
        this.event = event;
        this.log = log;
    }
}
