package clock;

public class PhysicalClock {

    private long offset;

    public PhysicalClock(long offset) {
        this.offset = offset;
    }

    public synchronized long getTime() {
        return System.currentTimeMillis() + offset;
    }

    public synchronized void adjust(long adjustment) {
        offset += adjustment;
    }

    public synchronized long getOffset() {
        return offset;
    }
}