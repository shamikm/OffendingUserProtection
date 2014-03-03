package us.sam.oip;

import org.testng.Assert;
import org.testng.annotations.Test;
import us.sam.oip.utility.LRUCache;

import java.util.concurrent.TimeUnit;

/**
 * @author shamik.majumdar
 */
public class TrackUserInMemoryTest {

    @Test
    public void testLRU(){
        LRUCache<String,Integer> lruCache = new LRUCache<>(2);
        //lets add two entries
        lruCache.put("first",1);
        lruCache.put("second",1);
        lruCache.put("third",1);

        Assert.assertEquals(lruCache.size(),2);

    }

    @Test
    public void testSimpleUserTrackCase() throws InterruptedException {
        TrackUserConfiguration configuration = new TrackUserConfiguration();
        configuration.setOldPromotionCheckFrequency(1);
        configuration.setOldPromotionCheckTimeUnit(TimeUnit.SECONDS);
        configuration.setPermPromotionCheckFrequency(2);
        configuration.setPermPromotionCheckTimeUnit(TimeUnit.SECONDS);

        TrackUserInMemory trackUser = new TrackUserInMemory(configuration);
        for (int i =0; i < 20; i++) {
            trackUser.trackUserIp("127.0.0.1","x");
            Thread.sleep(200);
        }
        Assert.assertEquals(trackUser.isThisAOffendingUser("127.0.0.1","x"),true);
        Assert.assertEquals(trackUser.getPermCache().size(),1);
    }
}
