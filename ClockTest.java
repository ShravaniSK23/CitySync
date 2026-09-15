import clock.PhysicalClock;
import clock.LamportClock;

public class ClockTest {

    public static void main(String[] args) {

        // Physical clock with a 5-second offset
        PhysicalClock physicalClock =
                new PhysicalClock(5000);

        System.out.println(
                "Physical Clock: "
                + physicalClock.getTime()
        );

        // Lamport logical clock
        LamportClock lamportClock =
                new LamportClock();

        System.out.println(
                "Initial Lamport Clock: "
                + lamportClock.getTime()
        );

        System.out.println(
                "Local Event: "
                + lamportClock.localEvent()
        );

        System.out.println(
                "Send Event: "
                + lamportClock.sendEvent()
        );

        System.out.println(
                "Receive Event (timestamp 10): "
                + lamportClock.receiveEvent(10)
        );
    }
}