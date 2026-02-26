package education.networking.udp;

import org.assertj.core.api.Assert;
import org.junit.jupiter.api.Test;

import static education.networking.udp.Constants.WAIT_TIME;

public class StreamerTest
{
    @Test
    public void listenerShouldHearStreamer() {
        Streamer streamer = new Streamer(5050, 10);
        Thread streamerThread = new Thread(streamer);

        Listener listener = new Listener(5050);
        Thread listenerThread = new Thread(listener);

        try {
            streamerThread.start();

            Thread.sleep(2 * WAIT_TIME);

            listenerThread.start();

            streamerThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            listenerThread.interrupt();
        }

        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            // Do nothing
        }
    }

    @Test
    public void listenersShouldHearStreamer()
    {
        education.networking.udp.Streamer streamer = new education.networking.udp.Streamer(5050, 10);
        Thread streamerThread = new Thread(streamer);

        Listener listener1 = new Listener(5050);
        Thread listenerThread1 = new Thread(listener1);

        Listener listener2 = new Listener(5050);
        Thread listenerThread2 = new Thread(listener2);

        try {
            streamerThread.start();

            Thread.sleep(2 * WAIT_TIME);

            listenerThread1.start();
            listenerThread2.start();

            streamerThread.join();
        }
        catch(InterruptedException ex)
        {
            throw new RuntimeException(ex);
        }
        finally
        {
            listenerThread1.interrupt();
            listenerThread2.interrupt();
        }

        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            // Do nothing
        }

        System.out.println("Finished test.");
    }
}