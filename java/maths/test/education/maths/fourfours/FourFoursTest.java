package education.maths.fourfours;

import org.junit.jupiter.api.Test;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static org.assertj.core.api.Assertions.assertThat;

/*
4% = 0.04
sqr(4) = 16 ???
cube(4) = 64 ???
sqrt(4) = 2
4! = 24
Gamma (4) = 6 ???
.4 = 0.4
4.4 = 4.25
reoccuring(4) = 4/9
 */
public class FourFoursTest {
    @Test
    void shouldCalculate01() {
        assertThat(44 / 44).isEqualTo(1);
    }

    @Test
    void shouldCalculate02() {
        assertThat((4 / 4) + (4 / 4)).isEqualTo(2);
    }

    @Test
    void shouldCalculate03() {
        assertThat(sqrt(4) + pow(4, (4 - 4))).isEqualTo(3);
    }

    @Test
    void shouldCalculate05() {
        assertThat(sqrt(4) + sqrt(4) + (4 / 4)).isEqualTo(5);
    }

    @Test
    void shouldCalculate06() {
        assertThat(((4 + 4) / 4) + 4).isEqualTo(6);
    }

    @Test
    void shouldCalculate07() {
        assertThat((44 / 4) - 4).isEqualTo(7);
    }

    @Test
    void shouldCalculate08() {
        assertThat(sqrt(4) + sqrt(4) + sqrt(4) + sqrt(4)).isEqualTo(8);
    }

    @Test
    void shouldCalculate09() {
        assertThat(pow(sqrt(4) + (4/4), sqrt(4))).isEqualTo(9);
    }

    @Test
    void shouldCalculate10() {
        assertThat(4 + sqrt(4) + sqrt(4) + sqrt(4)).isEqualTo(10);
    }

    @Test
    void shouldCalculate11() {
        assertThat((((factorial4() + 4) / 4) + 4)).isEqualTo(11);
    }

    @Test
    void shouldCalculate12() {
        assertThat(factorial4() - (4 * 4) + 4).isEqualTo(12);
    }

    @Test
    void shouldCalculate13() {
        assertThat(((factorial4() + sqrt(4)) * sqrt(4)) / 4).isEqualTo(13);
    }

    @Test
    void shouldCalculate14() {
        assertThat(sqrt(4) + 4 + 4 + 4).isEqualTo(14);
    }

    @Test
    void shouldCalculate15() {
        assertThat((44 / 4) + 4).isEqualTo(15);
    }

    @Test
    void shouldCalculate16() {
        assertThat(4 + 4 + 4 + 4).isEqualTo(16);
        assertThat((4 * 4) * (4 / 4)).isEqualTo(16);
        assertThat((pow(4, 4)) / (4 * 4)).isEqualTo(16);
        assertThat(pow((4 + 4) / 4, 4)).isEqualTo(16);
    }

    @Test
    void shouldCalculate17() {
        assertThat((4*4)+(4/4)).isEqualTo(17);
    }

    @Test
    void shouldCalculate18() {
        assertThat((((factorial4() + 4) / sqrt(4)) + 4)).isEqualTo(18);
    }

    @Test
    void shouldCalculate19() {
        assertThat(factorial4() - 4 - (4 / 4)).isEqualTo(19);
    }

    @Test
    void shouldCalculate20() {
        assertThat(factorial4() + 4 - (4 + 4)).isEqualTo(20);
        assertThat(factorial4() + 4 - (4 + 4)).isEqualTo(20);
    }

    private static long factorial4() {
        return factorial(4);
    }

    private static long factorial(long value) {
        long result = 1;
        for (int i = 1; i <= value; i++) {
            result *= i;
        }
        return result;
    }
}
