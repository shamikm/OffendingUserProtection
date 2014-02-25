package us.sam.oip;
/**
 * @author shamik.majumdar
 */
public interface TrackUser {

    public void trackUserIp(String ip, String userId);
    public boolean isThisAOffendingUser(String ip, String userId);
}
