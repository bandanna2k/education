package education.common.cryptography;

public class FastHash
{
    static long a1 = 0x65d200ce55b19ad8L;
    static long b1 = 0x4f2162926e40c299L;
    static long c1 = 0x162dd799029970f8L;
    static long a2 = 0x68b665e6872bd1f4L;
    static long b2 = 0xb6cfcf9d79b51db2L;
    static long c2 = 0x7a2b92ae912898c2L;

    public static int hash32_1(long x) {
        int low = (int)x;
        int high = (int)(x >>> 32);
        return (int)((a1 * low + b1 * high + c1) >>> 32);
    }
    public static int hash32_2(long x) {
        int low = (int)x;
        int high = (int)(x >>> 32);
        return (int)((a2 * low + b2 * high + c2) >>> 32);
    }

    public static long hash64(long x) {
        int low = (int)x;
        int high = (int)(x >>> 32);
        return ((a1 * low + b1 * high + c1) >>> 32)
                | ((a2 * low + b2 * high + c2) & 0xFFFFFFFF00000000L);
    }

    public static long murmur64(long h) {
        h ^= h >>> 33;
        h *= 0xff51afd7ed558ccdL;
        h ^= h >>> 33;
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= h >>> 33;
        return h;
    }
}
