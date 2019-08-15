import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
    Implementation of Set Associative Cache
 */
public class SetAssociativeCache extends  AssociativeCache {
    private final int blockPower;
    private final int capacity;
    private final int cachePower;
    private final int setCapacity;

//    private final Set<String> cache;
    /**
     * Maps Set Address to a FullyAssociativeCache. Each set can be considered an independent fully associative cache
     */
    private final Map<String , FullyAssociativeCache> cache;
    private final int setPower;
    /**
     * Constructs a fully associative cache instance.
     * @param cachePower log_2 (size of cache)
     * @param blockPower log_2 (size of each cache block/line)
     * @param setPower number of sets in cache(n-way set associative)
     * @param evictionPolicy policy to decide which page to evict next
     */
    public SetAssociativeCache(final int cachePower,
                               final int blockPower,
                               final int setPower,
                               final EvictionPolicy evictionPolicy) {
        super(evictionPolicy);
        this.blockPower = blockPower;
        this.cachePower = cachePower;
        this.setPower = setPower;
        this.capacity = 1 << (cachePower - blockPower);
        this.setCapacity = 1 << (cachePower - blockPower - setPower);
//        FullyAssociativeCache fullyAssociativeCache = new FullyAssociativeCache( , blockPower,evictionPolicy);
        cache = new HashMap<>(this.capacity);
    }

    /**
     * Constructs a fully associative cache instance.
     * @param cachePower log_2 (size of cache)
     * @param setPower number of sets in cache(n-way set associative)
     * @param blockPower log_2 (size of each cache block/line)
     */
    public SetAssociativeCache(final int cachePower, final int blockPower, final int setPower) {
        this(cachePower, blockPower,setPower,new LeastRecentlyUsedEvictionPolicy());
    }

    /**
     * Not Used
     * @return
     */
    @Override
    protected String evict() {
        return evictionPolicy.evict();
    }

    @Override
    public boolean load(String address) {
        final String blockOffset = address.substring(address.length() - blockPower );

        final String setAddress = address.substring(address.length() - setPower - blockPower , address.length() - blockPower);
        final String addressWithoutSet = new StringBuilder(address.substring(0 , address.length() - blockPower - setPower)).append(blockOffset).toString();
//        System.out.println("Address " + address + " Set address "  + setAddress + " addressWithoutSet " + addressWithoutSet);

//        evictionPolicy.load(addressWithoutBlockOffset);
        if (cache.containsKey(setAddress)) {
            return cache.get(setAddress).load(addressWithoutSet);

        } else {
//            if (cache >= capacity) {
//                while (cache.size() >= capacity) cache.remove(evict());
//            }
            try {

                cache.put(setAddress, new FullyAssociativeCache(this.cachePower - this.setPower, this.blockPower, this.evictionPolicy.getClass().getConstructor().newInstance()));
                cache.get(setAddress).load(addressWithoutSet);
            } catch (NoSuchMethodException | InvocationTargetException |IllegalAccessException|InstantiationException   e) {
                e.printStackTrace();
            }


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
        StringBuilder builder = new StringBuilder();
        cache.forEach((e,v) -> builder.append(e).append(" ==> ").append( v).append("\n"));
        return builder.toString();
    }
}
