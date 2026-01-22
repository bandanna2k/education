package education.maths;

public abstract class Summation {
    public static int sum(int value) {
        int result = 0;
        for (int i = 1; i <= value; i++) {
            result += i;
        }
        return result;
    }
}
