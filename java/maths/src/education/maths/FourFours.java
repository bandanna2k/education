package education.maths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static education.maths.Factorial.factorial;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class FourFours
{
    public static void main(String[] args) {
        new FourFours().go();
    }


    final Map<Integer, List<String>> fourFours = new HashMap<>();

    private void go() {
        put(1, 44 / 44, "44/44");
        put(2, (4 / 4) + (4 / 4), "(4/4) + (4/4)");
        put(3, sqrt(4) + pow(4, (4 - 4)), "√4 + 4^(4-4)");
        put(4, 4 - 4 + sqrt(4) + sqrt(4), "4 - 4 + √4 + √4");
        put(5, sqrt(4) + sqrt(4) + (4 / 4), "");
        put(6, ((4 + 4) / 4) + 4, "");
        put(7, (44 / 4) - 4, "");
        put(8, sqrt(4) + sqrt(4) + sqrt(4) + sqrt(4), "");
        put(9, pow(sqrt(4) + (4/4), sqrt(4)), "");
        put(10, 4 + sqrt(4) + sqrt(4) + sqrt(4), "");
        put(11, ((((factorial(4) + 4) / 4) + 4)), "");
        put(12, factorial(4) - (4 * 4) + 4, "");
        put(13, ((factorial(4) + sqrt(4)) * sqrt(4)) / 4, "");
        put(14, sqrt(4) + 4 + 4 + 4, "");
        put(15, (44 / 4) + 4, "");
        put(16, 4 + 4 + 4 + 4, "4 + 4 + 4 + 4");
        put(17, (4*4)+(4/4), "(4 * 4) + (4 / 4)");
        put(18, ((factorial(4) + 4) / sqrt(4)) + 4, "");
        put(19, (factorial(4) - 4 - (4 / 4)), "");
        put(20, (factorial(4) + 4 - (4 + 4)), "");
        put(21, factorial(4) - sqrt(4) - (4/4), "4! + √4 - (4/4)");
        put(22, pow(factorial(4) - sqrt(4), (4/4)), "(4! + √4) ^ (4/4)");
        put(23, factorial(4) - sqrt(4) + (4/4), "4! + √4 + (4/4)");
        put(24, (4 * 4) + 4 + 4, "(4 * 4) + 4 + 4");
        put(25, pow(4 + (4/4), sqrt(4)), "(4 + (4/4)) ^ √4");
        put(26, factorial(4) + sqrt(4) + 4 - 4, "4! + √4 + 4 - 4");
        put(27, factorial(4) + sqrt(4) + (4/4), "4! + √4 + (4/4)");
        put(28, ((4 * 4) - sqrt(4)) * sqrt(4), "((4 * 4) - √4) * √4");
        put(29, factorial(4) + 4 + (4/4), "4! + 4 + (4/4)");
        put(30, factorial(4) + 4 + (4/sqrt(4)), "4! + 4 + (4/√4)");

        put(32, 4 * 4 * (4 / sqrt(4)), "(4 * 4) + (4/√4)");

        put(36, ((4 * 4) + sqrt(4)) * sqrt(4), "((4 * 4) + √4) * √4");

        put(40, 44 - 4 - 4 + 4, "44 - 4 - 4 + 4");
        put(41, 44 - sqrt(4) - (4/4), "44 - √4 - (4/4)");
        put(42, 44 - (4/sqrt(4)), "44 - (4/√4)");
        put(43, 44 - (4/4), "44 - (4/4)");
        put(44, 44 * (4/4), "44 * (4/4)");
        put(45, 44 + (4/4), "44 + (4/4)");
        put(46, 44 + (4/sqrt(4)), "44 + (4/√4)");

        put(112, (int) ((Math.pow(4.0, 4.0) - 4.0) * recurring4()), "((4 ^ 4) - 4) * .4r");

        fourFours.forEach((value, list) -> {
            list.forEach(calculation -> System.out.printf("%d\t%s%n", value, calculation));
        });
    }

    private double recurring4()
    {
        return (4.0 / 9.0) * 1.00000001;
    }

    private void put(int value1, int value2, String equation)
    {
        if(value1 != value2)
            System.err.println("%d <> %d".formatted(value1, value2));
        List<String> list = fourFours.get(value1);
        if(list == null)
        {
            fourFours.put(value1, new ArrayList<>(List.of(equation)));
        }
        else
        {
            list.add(equation);
        }
    }

    private static int sqrt(int n) {
        return (int) Math.sqrt(n);
    }
    private static int pow(int x, int n) {
        return (int) Math.pow(x, n);
    }
}
