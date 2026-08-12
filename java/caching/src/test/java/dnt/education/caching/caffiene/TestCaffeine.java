package dnt.education.caching.caffiene;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class TestCaffeine {

    @Test
    void shouldCache() {
        AtomicInteger loadCount = new AtomicInteger();

        LoadingCache<String, BigDecimal> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .build(new CacheLoader<>() {

                    final Map<String, BigDecimal> priceSource = new HashMap<>();
                    {
                        priceSource.put("AAPL", new BigDecimal("100.00"));
                        priceSource.put("GOOGL", new BigDecimal("150.00"));
                    }

                    @Override
                    public BigDecimal load(String symbol) {
                        loadCount.incrementAndGet();
                        return priceSource.get(symbol);
                    }
                });

        assertEquals(new BigDecimal("100.00"), cache.get("AAPL"));
        assertEquals(new BigDecimal("150.00"), cache.get("GOOGL"));
        assertEquals(new BigDecimal("100.00"), cache.get("AAPL"), "second get should hit the cache");

        assertEquals(2, loadCount.get(), "load should be invoked once per symbol, not per get");
    }

    @Test
    void shouldEvictByMaximumSize() {
        Cache<Integer, String> cache = Caffeine.newBuilder()
                .maximumSize(3)
                .build();

        for (int i = 0; i < 5; i++) {
            cache.put(i, "v" + i);
        }
        cache.cleanUp();

        final Map<Integer, String> map = cache.asMap();
        System.out.println(map);
        assertEquals(3, map.size(), "cache should be trimmed to maximum size");
    }

    @Test
    void shouldExpireAfterWrite() throws InterruptedException {
        Cache<String, String> cache = Caffeine.newBuilder()
                .expireAfterWrite(50, TimeUnit.MILLISECONDS)
                .build();
        cache.put("key", "value");

        assertNotNull(cache.getIfPresent("key"));
        Thread.sleep(150);
        assertNull(cache.getIfPresent("key"));
    }

    @Test
    void shouldTrackStats() {
        Cache<String, String> cache = Caffeine.newBuilder()
                .maximumSize(100)
                .recordStats()
                .build();

        cache.get("a", k -> "value-a");
        cache.get("a", k -> "value-a");
        cache.getIfPresent("missing");

        CacheStats stats = cache.stats();
        assertEquals(1, stats.hitCount());
        assertEquals(2, stats.missCount());
        assertEquals(1.0 / 3.0, stats.hitRate());
    }
}
