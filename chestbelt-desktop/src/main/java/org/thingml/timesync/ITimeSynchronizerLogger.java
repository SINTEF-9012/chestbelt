
package org.thingml.timesync;


public interface ITimeSynchronizerLogger {
    public void timeSyncStart();
    public void timeSyncStop();   
    public void timeSyncPong(int delay, int dtt, int dtr, int dts);
    
    public void timeSyncLog(String time, long ts, long tmt, long tmr, long delay, long offs, long errorSum, long zeroOffset, long regOffsMs, int skipped);
    
}
