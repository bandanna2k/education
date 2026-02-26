package education.networking.udp;

import org.junit.Test;

import java.net.*;
import java.util.List;

public class SubnetMaskAddressesTest
{
    List<InterfaceAddress> getSubnetMaskAddresses() throws UnknownHostException, SocketException
    {
        InetAddress localHost = Inet4Address.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);

        return networkInterface.getInterfaceAddresses();
    }

    private static String intToIP(int ipAddress)
    {
        String result = String.format("%d.%d.%d.%d",
                (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));

        return result;
    }

    @Test
    public void showSubnetMaskAddresses() throws SocketException, UnknownHostException
    {
        for (InterfaceAddress ia : getSubnetMaskAddresses())
        {
            System.out.println(String.format("Interface address:%s, Subnet Mask: %s, Broadcast %s",
                    ia,
                    intToIP(ia.getNetworkPrefixLength()),
                    ia.getBroadcast()));
        }
    }
}
