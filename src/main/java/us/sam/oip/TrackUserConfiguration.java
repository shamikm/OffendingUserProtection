package us.sam.oip;

import java.util.concurrent.TimeUnit;

/**
 * @author shamik.majumdar
 */
public class TrackUserConfiguration {
    private int maxYoungEntries = 5000;
    private int maxOldEntries  = 1000;
    private int maxPermEntries = 500;
    private int youngThreshold = 2;
    private int oldThreshold = 5;
    private int oldPromotionCheckFrequency = 2;
    private TimeUnit oldPromotionCheckTimeUnit = TimeUnit.MINUTES;
    private int permPromotionCheckFrequency = 5;
    private TimeUnit permPromotionCheckTimeUnit = TimeUnit.MINUTES;

    public int getMaxOldEntries() {
        return maxOldEntries;
    }

    public void setMaxOldEntries(int maxOldEntries) {
        this.maxOldEntries = maxOldEntries;
    }

    public int getMaxPermEntries() {
        return maxPermEntries;
    }

    public void setMaxPermEntries(int maxPermEntries) {
        this.maxPermEntries = maxPermEntries;
    }

    public int getMaxYoungEntries() {
        return maxYoungEntries;
    }

    public void setMaxYoungEntries(int maxYoungEntries) {
        this.maxYoungEntries = maxYoungEntries;
    }

    public int getOldPromotionCheckFrequency() {
        return oldPromotionCheckFrequency;
    }

    public void setOldPromotionCheckFrequency(int oldPromotionCheckFrequency) {
        this.oldPromotionCheckFrequency = oldPromotionCheckFrequency;
    }

    public TimeUnit getOldPromotionCheckTimeUnit() {
        return oldPromotionCheckTimeUnit;
    }

    public void setOldPromotionCheckTimeUnit(TimeUnit oldPromotionCheckTimeUnit) {
        this.oldPromotionCheckTimeUnit = oldPromotionCheckTimeUnit;
    }

    public int getOldThreshold() {
        return oldThreshold;
    }

    public void setOldThreshold(int oldThreshold) {
        this.oldThreshold = oldThreshold;
    }

    public int getPermPromotionCheckFrequency() {
        return permPromotionCheckFrequency;
    }

    public void setPermPromotionCheckFrequency(int permPromotionCheckFrequency) {
        this.permPromotionCheckFrequency = permPromotionCheckFrequency;
    }

    public TimeUnit getPermPromotionCheckTimeUnit() {
        return permPromotionCheckTimeUnit;
    }

    public void setPermPromotionCheckTimeUnit(TimeUnit permPromotionCheckTimeUnit) {
        this.permPromotionCheckTimeUnit = permPromotionCheckTimeUnit;
    }

    public int getYoungThreshold() {
        return youngThreshold;
    }

    public void setYoungThreshold(int youngThreshold) {
        this.youngThreshold = youngThreshold;
    }
}
