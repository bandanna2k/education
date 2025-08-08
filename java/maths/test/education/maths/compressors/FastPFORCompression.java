package education.maths.compressors;

import me.lemire.integercompression.differential.IntegratedIntCompressor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class FastPFORCompression implements Compressor
{
    private final IntegratedIntCompressor iic = new IntegratedIntCompressor();

    @Override
    public byte[] compress(Set<Integer> input)
    {
        int[] array = input.stream().mapToInt(i -> i).toArray();
        int[] compressed = iic.compress(array);
        return integersToBytes(compressed);
    }

    @Override
    public Set<Integer> inflate(byte[] compressed)
    {
        int[] ints = convertByteArrayToIntArray(compressed);
        return Arrays.stream(iic.uncompress(ints)).boxed().collect(Collectors.toSet());
    }

    private static byte[] integersToBytes(int[] values)
    {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             DataOutputStream dos = new DataOutputStream(baos))
        {
            for (int value : values)
            {
                dos.writeInt(value);
            }
            return baos.toByteArray();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static int[] convertByteArrayToIntArray(byte[] byteArray)
    {
        assert (byteArray.length % 4 == 0);

        int[] intArray = new int[byteArray.length / 4];
        for (int i = 0; i < intArray.length; i++)
        {
            int byteIndex = i * 4;

            // Combine 4 bytes into one int (big-endian byte order)
            intArray[i] = ((byteArray[byteIndex] & 0xFF) << 24) |
                    ((byteArray[byteIndex + 1] & 0xFF) << 16) |
                    ((byteArray[byteIndex + 2] & 0xFF) << 8) |
                    (byteArray[byteIndex + 3] & 0xFF);
        }
        return intArray;
    }
}