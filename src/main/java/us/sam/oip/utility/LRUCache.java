package us.sam.oip.utility;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author shamik.majumdar
 */
public class LRUCache<Key,Value> extends LinkedHashMap<Key,Value> {
    private final int maxEntries;

    public LRUCache(int maxEntries) {
        super(maxEntries+1,0.75f,true);
        this.maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<Key, Value> eldest) {
        return super.size() > maxEntries;
    }
}
