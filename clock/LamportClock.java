package clock;

public class LamportClock {

    private long time = 0;

    public synchronized long localEvent() {
        time++;
        return time;
    }

    public synchronized long sendEvent() {
        time++;
        return time;
    }

    public synchronized long receiveEvent(long receivedTime) {
        time = Math.max(time, receivedTime) + 1;
        return time;
    }

    public synchronized long getTime() {
        return time;
    }
}