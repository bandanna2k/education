package education.benchmarks.jmh.absolute;

public abstract class Absolute
{
    public static long abs(long value) {
        // Create a mask: -1 (all 1's in binary) for negative, 0 for positive
        // >> 63 for long (64-bit), shifts sign bit to all positions
        long mask = value >> 63;

        // XOR with mask: flips bits if negative, no change if positive
        // Then subtract mask: -(-1) = +1 for negatives, -(0) = 0 for positives
        return (value ^ mask) - mask;
    }
}
