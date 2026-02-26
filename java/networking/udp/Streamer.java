package education.networking.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static education.networking.udp.Constants.WAIT_TIME;

public class Streamer implements Runnable
{
    final private int port;
    private int countOfStreams;

    public Streamer(int port, int countOfStreams)
    {
        this.port = port;
        this.countOfStreams = countOfStreams;
    }

    @Override
    public void run()
    {
        try
        {
            streamValues();
        }
        catch(IOException ex)
        {
            System.out.println("Error: " + ex.getMessage());
        }
        catch(InterruptedException ex)
        {
            System.out.println("Warning: Thread interrupted." );
        }
        System.out.println("Finished streaming.");
    }

    private void streamValues() throws IOException, InterruptedException {
        DatagramSocket ds = new DatagramSocket();
        InetAddress ip = InetAddress.getLocalHost();
        ip = InetAddress.getByName("255.255.255.255");
        byte output[];

        while (countOfStreams >= 0) {

            String outputAsString = String.valueOf(countOfStreams);

            output = outputAsString.getBytes();

            DatagramPacket packet = new DatagramPacket(output, output.length, ip, port);

            ds.send(packet);

            Thread.sleep(WAIT_TIME);

            countOfStreams--;
        }
    }
}
