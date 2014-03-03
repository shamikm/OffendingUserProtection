package us.sam.oip;

import org.testng.Assert;
import org.testng.annotations.Test;
import us.sam.oip.utility.LRUCache;

import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author shamik.majumdar
 */
public class TrackUserInMemoryTest {

    private TrackUserInMemory createTrackUserInMemory() {
        TrackUserConfiguration configuration = new TrackUserConfiguration();
        configuration.setOldPromotionCheckFrequency(1);
        configuration.setOldPromotionCheckTimeUnit(TimeUnit.SECONDS);
        configuration.setPermPromotionCheckFrequency(2);
        configuration.setPermPromotionCheckTimeUnit(TimeUnit.SECONDS);
        return  new TrackUserInMemory(configuration);
    }

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
        TrackUserInMemory trackUser = createTrackUserInMemory();
        for (int i =0; i < 20; i++) {
            trackUser.trackUserIp("127.0.0.1","x");
            Thread.sleep(200);
        }
        Assert.assertEquals(trackUser.isThisAOffendingUser("127.0.0.1", "x"), true);
        Assert.assertEquals(trackUser.getPermCache().size(),1);
    }

    @Test
    public void testUserTrackFromThreads() throws InterruptedException {
        final TrackUserInMemory trackUserInMemory = createTrackUserInMemory();
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch waitingLatch = new CountDownLatch(100);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Runnable task = new Runnable() {
            private final SecureRandom random = new SecureRandom();
            @Override
            public void run() {
                try {
                    latch.await();
                    for (int i=0; i < 100; i++){
                        //add for 100 times
                        trackUserInMemory.trackUserIp("127.0.0.1", String.valueOf(random.nextLong()));
                        trackUserInMemory.trackUserIp("127.0.0.1", "X");
                    }
                    waitingLatch.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

            }
        };
        for (int i=0;i<100;i++)
            executorService.submit(task);

        latch.countDown();   //open the flood gate
        waitingLatch.await(); //wait for all threads are done
        executorService.shutdown();
        Thread.sleep(5000L);
        Assert.assertEquals(trackUserInMemory.getYoungCache().size(),5000);
        Assert.assertEquals(trackUserInMemory.getPermCache().size(),1);
    }
}
