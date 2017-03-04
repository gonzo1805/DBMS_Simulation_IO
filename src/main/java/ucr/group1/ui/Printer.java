package ucr.group1.ui;

/**
 * Created by Gonzalo on 3/3/2017.
 */
public class Printer {
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

    public Printer(int time, String event, String log) {
        this.time = time;
        this.event = event;
        this.log = log;
    }
}
