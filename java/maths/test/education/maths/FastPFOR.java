package education.maths;

import me.lemire.integercompression.IntWrapper;
import me.lemire.integercompression.differential.*;
import org.junit.Test;

import java.util.Arrays;

import static education.maths.SortedIntegers.INITIAL_DATA;

public class FastPFOR {
    @Test
    public void basicExample() {
        IntegratedIntegerCODEC codec =  new
                IntegratedComposition(
                new IntegratedBinaryPacking(),
                new IntegratedVariableByte());

        System.out.print("Raw:");
        int[] array = INITIAL_DATA.stream().mapToInt(Number::intValue).toArray();
        Arrays.stream(array).forEach(x -> System.out.printf("%d,", x));
        System.out.println();

        System.out.print("Compressed:");
        IntWrapper inputoffset = new IntWrapper(0);
        IntWrapper outputoffset = new IntWrapper(0);
        int[] compressed = new int[array.length+1024];
        codec.compress(array, inputoffset, array.length, compressed, outputoffset); // compressed array
        Arrays.stream(compressed).forEach(x -> System.out.printf("%d,", x));
        System.out.println();
    }

    @Test
    public void name() {
        IntegratedIntCompressor iic = new IntegratedIntCompressor();

        System.out.print("Raw:");
        int[] array = INITIAL_DATA.stream().mapToInt(Number::intValue).toArray();
        Arrays.stream(array).forEach(x -> System.out.printf("%d,", x));
        System.out.println();

        System.out.print("Compressed:");
        int[] compressed = iic.compress(array); // compressed array
        Arrays.stream(compressed).forEach(x -> System.out.printf("%d,", x));
        System.out.println();
    }
}
