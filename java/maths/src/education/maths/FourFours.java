package education.maths;

import java.util.*;

import static education.maths.Factorial.factorial;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class FourFours
{
    public static void main(String[] args) {
        new FourFours().go();
    }


    final Map<Integer, List<String>> fourFours = new TreeMap<>();

    private void go() {

        double recurring4 = (4.0 / 9.0) * 1.00000001;
        double recurringRoot4 = (Math.sqrt(4.0) / 9.0) * 1.00000001;

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
        put(31, factorial(4) + ((factorial(4) + 4) / 4), "(4! + ((4! + 4 / 4)");
        put(32, 4 * 4 * (4 / sqrt(4)), "(4 * 4) + (4/√4)");
        put(33,((int)((4 -.4) / .4)) + factorial(4), "(4 - .4) / .4) + 4!");
        put(34, (4 * 4 * sqrt(4)) + sqrt(4), "(4 * 4 * √4) + √4");
        put(35,((int)((4 +.4) / .4)) + factorial(4), "(4 + .4) / .4) + 4!");
        put(36, ((4 * 4) + sqrt(4)) * sqrt(4), "((4 * 4) + √4) * √4");

        put(40, 44 - 4 - 4 + 4, "44 - 4 - 4 + 4");
        put(41, 44 - sqrt(4) - (4/4), "44 - √4 - (4/4)");
        put(42, 44 - (4/sqrt(4)), "44 - (4/√4)");
        put(43, 44 - (4/4), "44 - (4/4)");
        put(44, 44 * (4/4), "44 * (4/4)");
        put(45, 44 + (4/4), "44 + (4/4)");
        put(46, 44 + (4/sqrt(4)), "44 + (4/√4)");
        put(47, (factorial(4) * sqrt(4)) - (4/4), "(4! * √4) - (4/4)");
        put(48, (factorial(4) * sqrt(4)) * (4/4), "(4! * √4) * (4/4)");
        put(49, (factorial(4) * sqrt(4)) + (4/4), "(4! * √4) + (4/4)");

        put(47, (int)(((factorial(4) / inverse(4)) - sqrt(4)) / sqrt(4)), "((4! / 4′) - √4) / √4");
        put(48, (factorial(4) * sqrt(4)) * (4/4), "(4! * √4) * (4/4)");
        put(49, (int)(((factorial(4) / inverse(4)) + sqrt(4)) / sqrt(4)), "");
        put(50, (factorial(4) * sqrt(4)) + 4 - sqrt(4), "((4! / 4′) + √4) / √4");

        put(52, (factorial(4) * sqrt(4)) + sqrt(4) + sqrt(4), "(4! * √4) + √4 + √4");

        put(54, (factorial(4) * sqrt(4)) + sqrt(4) + 4, "(4! * √4) + √4 + 4");

        put(56, (factorial(4) * sqrt(4)) + 4 + 4, "(4! * √4) + 4 + 4");

        put(58, ((factorial(4) + 4) * sqrt(4)) + sqrt(4), "((4! + 4) * √4) + √4");

        put(60, ((factorial(4) + 4) * sqrt(4)) + 4, "((4! + 4) * √4) + 4");

        put(62, (4*4*4)-sqrt(4), "(4 * 4 * 4) - √4");

        put(64, 4*4*(sqrt(4)+sqrt(4)), "(4 * 4 * (√4 + √4))");

        put(66, (4*4*4)+sqrt(4), "(4 * 4 * 4) + √4");

        put(68, (factorial(4) * 4) - factorial(4) - 4, "(4! * 4) - (4! - 4)");

        put(70, (factorial(4) * 4) - factorial(4) - sqrt(4), "(4! * 4) - (4! - √4)");

        put(74, (factorial(4) * 4) - factorial(4) + sqrt(4), "(4! * 4) - (4! + √4)");
        put(75, (int)((factorial(4) / (.4 + .4)) / .4), "((4! / .4) / (.4 + .4)) / .4");
        put(76, (factorial(4) * 4) - factorial(4) + 4, "(4! * 4) - (4! + 4)");

        put(78, ((factorial(4) - 4) * 4) - sqrt(4), "((4! - 4) * 4) - √4");

        put(82, ((factorial(4) - 4) * 4) + sqrt(4), "((4! - 4) * 4) + √4");
        //        put(81, (int) (((4.0 / recurring4) / recurring4) * 4.0), "((4 / .4r) / .4r) * 4");
        put(84, ((factorial(4) - 4) * 4) + 4, "((4! - 4) * 4) + 4");
//        put(85, (int)((4 * 4) + (4 / 4) / recurringRoot4), "(4 * 4) + (4 / 4)");
//        put(86, ((factorial(4)) * 4), "((4! - 4) * 4) + 4");

        put(88, (factorial(4) * 4) - 4 - 4, "(4! * 4) - 4 - 4");

        put(90, (factorial(4) * 4) - 4 - sqrt(4), "(4! * 4) - 4 - √4");

        put(92, (factorial(4) * 4) - sqrt(4) - sqrt(4), "(4! * 4) - √4 - √4");

        put(94, (factorial(4) * 4) - (4/sqrt(4)), "(4! * 4) - (4/√4)");
        put(95, (factorial(4) * 4) - (4/4), "(4! * 4) - (4/4)");
        put(96, (factorial(4) * 4) - 4 + 4, "(4! * 4) - 4 + 4");
        put(97, (factorial(4) * 4) + (4/4), "(4! * 4) + (4/4)");
        put(98, (factorial(4) * 4) + (4/sqrt(4)), "(4! * 4) + (4/√4)");
        put(100, (factorial(4) + (4/4)) * 4, "(4! + (4/4) * 4");

        put(112, (int) ((Math.pow(4.0, 4.0) - 4.0) * recurring4), "((4 ^ 4) - 4) * .4r");

        fourFours.forEach((value, list) -> {
            list.forEach(calculation -> System.out.printf("%d\t%s%n", value, calculation));
        });
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

    private static double inverse(double x)
    {
        return 1.0 / x;
    }
}
