package education.maths.bytes;

public class ByteArrayHelper
{
    public static String toHex(byte[] a)
    {
        final StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
        {
            sb.append(String.format("%02x ", b));
        }
        return sb.toString();
    }
}
