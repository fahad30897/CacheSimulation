import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of a Fully Associative cache.
 */
public class FullyAssociativeCache extends AssociativeCache {
    private final int blockPower;
    private final int capacity;
    private final Set<String> cache;

    /**
     * Constructs a fully associative cache instance.
     * @param cachePower log_2 (size of cache)
     * @param blockPower log_2 (size of each cache block/line)
     * @param evictionPolicy policy to decide which page to evict next
     */
    public FullyAssociativeCache(final int cachePower,
                                    final int blockPower,
                                    final EvictionPolicy evictionPolicy) {
        super(evictionPolicy);
        this.blockPower = blockPower;
        this.capacity = 1 << (cachePower - blockPower);
        cache = new HashSet<>(this.capacity);
    }

    /**
     * Constructs a fully associative cache instance.
     * @param cachePower log_2 (size of cache)
     * @param blockPower log_2 (size of each cache block/line)
     */
    public FullyAssociativeCache(final int cachePower, final int blockPower) {
        this(cachePower, blockPower, new LeastRecentlyUsedEvictionPolicy());
    }

    @Override
    protected String evict() {
        return evictionPolicy.evict();
    }

    @Override
    public boolean load(String address) {
        final String addressWithoutBlockOffset = address.substring(0, address.length() - blockPower);
        evictionPolicy.load(addressWithoutBlockOffset);
        if (cache.contains(addressWithoutBlockOffset)) {
            return true;
        } else {
            if (cache.size() >= capacity) {
                while (cache.size() >= capacity) cache.remove(evict());
            }
            cache.add(addressWithoutBlockOffset);
            return false;
        }
    }

    /**
     * Prints the contents of cache.
     */
    @Override
    public String toString() {
        if (cache.size() == 0) {
            return "Empty Cache";
        }
        return String.join(",", cache) ;//+ " Evict " + String.join("," , evictionPolicy.toString());
    }
}
