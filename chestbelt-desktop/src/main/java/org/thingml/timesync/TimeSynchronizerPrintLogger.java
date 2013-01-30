package org.thingml.timesync;

/**
 *
 * @author ffl
 */
public class TimeSynchronizerPrintLogger implements ITimeSynchronizerLogger {

    private String SEPARATOR = "\t";
    
    @Override
    public void timeSyncLog(String time, long ts, long tmt, long tmr, long delay, long offs, long errorSum, long zeroOffset, long regOffsMs, int skipped) {
        System.out.println("TimeSync:" + time + SEPARATOR + ts + SEPARATOR + tmt + SEPARATOR + tmr + SEPARATOR + delay + SEPARATOR + offs + SEPARATOR + errorSum + SEPARATOR + zeroOffset + SEPARATOR + regOffsMs + SEPARATOR + skipped);
    }

    @Override
    public void timeSyncStart() {
        System.out.println("TimeSync: START.");
    }

    @Override
    public void timeSyncStop() {
        System.out.println("TimeSync: STOP.");
    }

    @Override
    public void timeSyncPong(int delay, int dtt, int dtr, int dts) {
        
    }
    
    @Override
    public void timeSyncReady() {
    }

    @Override
    public void timeSyncWrongSequence(int pingSeqNum, int pongSeqNum) {
    }

    @Override
    public void timeSyncDtsFilter(int dts) {
    }
    
     @Override
    public void timeSyncErrorFilter(int error) {
    }
    
}
