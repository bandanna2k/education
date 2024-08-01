package education.syntax;

import org.junit.Test;

import java.security.SecureRandom;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;

public class OptionalsTest
{
    @Test
    public void shouldTestIfThenElse() {
        final Random random = new SecureRandom();
        final Optional<String> string = random.nextBoolean() ? Optional.of("Test") : Optional.empty();

        // Old school
        System.out.println(string.isPresent() ? string.get() : "Not found");

        // Use attached static class
        OptionalEx.ifPresent(string, System.out::println).orElse(() -> System.out.println("Not found"));
    }

    @Test
    public void testFold()
    {
        final Optional<String> present = Optional.of("Squash");
        final Optional<String> notPresent = Optional.empty();
        present.ifPresentOrElse(System.out::println, () -> System.out.println("Present contains no value."));
        notPresent.ifPresentOrElse(System.out::println, () -> System.out.println("Not-present contains no value."));
    }

    public static class OptionalEx {
        private final boolean isPresent;

        private OptionalEx(boolean isPresent) {
            this.isPresent = isPresent;
        }

        public void orElse(Runnable runner) {
            if (!isPresent) {
                runner.run();
            }
        }

        public static <T> OptionalEx ifPresent(Optional<T> opt, Consumer<? super T> consumer) {
            if (opt.isPresent()) {
                consumer.accept(opt.get());
                return new OptionalEx(true);
            }
            return new OptionalEx(false);
        }
    }
}
