package education.syntax;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

public class ArraysTest
{
    @Test
    public void shouldConvertListToStaticArray2()
    {
        final List<Integer> dynamicList = new ArrayList<>(asList(1,2,3));
        System.out.println(dynamicList);

        final int[] staticArray = dynamicList.stream().mapToInt(Integer::intValue).toArray();
        System.out.println(Arrays.toString(staticArray));
    }
    @Test
    public void shouldConvertListToStaticArray()
    {
        final List<Integer> dynamicList = new ArrayList<>(asList(1,2,3));
        System.out.println(dynamicList);

        final int[] staticArray = new int[dynamicList.size()] ;
        for (int i = 0; i < staticArray.length; i++)
        {
            staticArray[i] = dynamicList.get(i);
        }
        System.out.println(Arrays.toString(staticArray));
    }
}
