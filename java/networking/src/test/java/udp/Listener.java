package education.networking.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.MulticastSocket;

public class Listener implements Runnable
{
    final private int port;

    public Listener(int port)
    {
        this.port = port;
    }

    @Override
    public void run()
    {
        try
        {
            listen();
        }
        catch(IOException e)
        {
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Finished listening.");
    }

    private void listen() throws IOException
    {
        DatagramSocket ds = new MulticastSocket(this.port);
        byte[] input = new byte[65535];

        while (true)
        {
            DatagramPacket packet = new DatagramPacket(input, input.length);

            ds.receive(packet);

            String inputAsString = new String(input,0, packet.getLength());

            System.out.println(String.format("Listener(%d): %s",this.hashCode(),inputAsString));
        }
    }
}
