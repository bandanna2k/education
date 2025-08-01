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

    public static byte[] intToByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }

    public static int bytesToInt(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }
}
