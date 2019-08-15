import java.util.LinkedList;

/**
 * Implementation of Least Recently Used cache eviction policy.
 */
public class LeastRecentlyUsedEvictionPolicy implements EvictionPolicy {
    private LinkedList<String> lruList = new LinkedList<>();

    /**
     * Checks if the address was already present in cache(LinkedList). If it was,
     * then it shifts the address node to the end of LinkedList. Otherwise adds
     * a new node at then end of LinkedList.
     * @param address address that was loaded in cache.
     */
    @Override
    public void load(String address) {
        final int index = lruList.indexOf(address);
        if (index == -1) {
            lruList.addLast(address);
        } else {
            lruList.remove(index);
            lruList.addLast(address);
        }
    }

    /**
     * Removes the least recently used element. It boils down to removing the first
     * node of the LinkedList.
     * @return the least recently used address.
     */
    @Override
    public String evict() {
        return lruList.removeFirst();
    }

    @Override
    public String toString(){
        return String.join("," , this.lruList);
    }
}
