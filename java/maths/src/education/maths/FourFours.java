package education.maths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static education.maths.Factorial.factorial;

public class FourFours
{
    public static void main(String[] args) {
        new FourFours().go();
    }

    final Map<Integer, List<String>> fourFours = new HashMap<>();

    private void go() {
        fourFours.put(factorial(4) + sqrt(4) + sqrt(4) + sqrt(4), List.of("factorial(4) + sqrt(4) + sqrt(4) + sqrt(4)"));

        fourFours.forEach((value, list) -> {
            list.forEach(calculation -> System.out.printf("%d\t%s", value, calculation));
        });
    }

    private static int sqrt(int n) {
        return (int) Math.sqrt(n);
    }
}
