package us.sam.oip;

import us.sam.oip.utility.LRUCache;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author shamik.majumdar
 */
public class TrackUserInMemory implements TrackUser {

    private final Map<String,Long> youngCache;
    private final Map<String,Long> oldCache;
    private final Map<String,Long> permCache;

    private final ExecutorService taskExecutor;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

    private class MoveIpTask implements Runnable {
        private final Map<String,Long> sourceCache;
        private final Map<String,Long> destCache;
        private final long threshold;

        public MoveIpTask(Map<String,Long> sourceCache,Map<String,Long> destCache,long threshold) {
            this.sourceCache = sourceCache;
            this.destCache = destCache;
            this.threshold = threshold;
        }

        /**
         * Each time when it runs, it submits a task to the main task executor
         * to move some entries from source cache to destination cache, if the value
         * in the source cache is found greater than a threshold value
         */

        @Override
        public void run() {
            taskExecutor.submit(new Runnable() {
                @Override
                public void run() {

                    for (Map.Entry<String,Long> pair : sourceCache.entrySet()) {
                        long v = pair.getValue();
                        if (v > threshold) {
                            String key = pair.getKey();
                            long destVal = 0L;
                            if (destCache.containsKey(key)) {
                                destVal = destCache.get(key);
                            }
                            destCache.put(key,destVal+v);
                            sourceCache.put(key,0L);
                        }

                    }

                }
            });

        }
    }


    public TrackUserInMemory(TrackUserConfiguration configuration) {
        youngCache = Collections.synchronizedMap(new LRUCache<String,Long>(configuration.getMaxYoungEntries()));
        oldCache = Collections.synchronizedMap(new LRUCache<String, Long>(configuration.getMaxOldEntries()));
        permCache = Collections.synchronizedMap(new LRUCache<String, Long>(configuration.getMaxPermEntries()));

        taskExecutor = Executors.newSingleThreadExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new MoveIpTask(youngCache,oldCache,configuration.getYoungThreshold()),configuration.getOldPromotionCheckFrequency(),configuration.getOldPromotionCheckFrequency(),configuration.getOldPromotionCheckTimeUnit());
        scheduledExecutorService.scheduleAtFixedRate(new MoveIpTask(oldCache,permCache,configuration.getOldThreshold()),configuration.getPermPromotionCheckFrequency(),configuration.getPermPromotionCheckFrequency(), configuration.getPermPromotionCheckTimeUnit());

    }

    private String getKey(String ip, String userId) {
        return ip + userId;
    }

    //package private getter for test

    Map<String,Long> getYoungCache (){
        return Collections.unmodifiableMap(youngCache);
    }

    Map<String,Long> getOldCache (){
        return Collections.unmodifiableMap(oldCache);
    }

    Map<String,Long> getPermCache (){
        return Collections.unmodifiableMap(permCache);
    }

    @Override
    public boolean isThisAOffendingUser(String ip, String userId) {
        return permCache.containsKey(getKey(ip, userId));
    }

    @Override
    public void trackUserIp(String ip, String userId) {
        final String key = getKey(ip,userId);
        taskExecutor.submit(new Runnable() {
            @Override
            public void run() {
               long counter = 0L;
               if (youngCache.containsKey(key)) {
                    counter = youngCache.get(key);
               }
               counter++;
               youngCache.put(key,counter);
            }
        });
    }
}
