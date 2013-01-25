package org.thingml.timesync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeSynchronizer implements Runnable {
    
    /**************************************************************************
     *  constructor and link to the device
     * ************************************************************************/
    
    protected TimeSynchronizable device;
    
    public TimeSynchronizer(TimeSynchronizable device) {
        this.device = device;
    }
    
    /**************************************************************************
     *  Logging
     * ************************************************************************/
    
    protected ArrayList<ITimeSynchronizerLogger> loggers = new ArrayList<ITimeSynchronizerLogger>();
    
    public void addLogger(ITimeSynchronizerLogger l) {
        loggers.add(l);
    }
    
    public void removeLogger(ITimeSynchronizerLogger l) {
        loggers.remove(l);
    }
    
    public void removeAllLoggers() {
        loggers.clear();
    }
    
    /**************************************************************************
     *  Configuration and parameters of the Time Sync Algorithm
     * ************************************************************************/
    final private int updateRate = 250;  // Rate for ping in milliseconds
    final private int dTsMax = 275; // Maximum delta time for TsFilter
    final private int dTsMin = 225; // Minimum delta time for TsFilter
    final private long tsErrorMax = 50; // Maximum deviation for a calculated offset compared to regOffset
    private long refClockPrev = 0; // Previous fullClock value used to detect wraparound
    private long tsPrev = 0; // Previous TS value used for TsFilter
    
    final private int tmtArrSize = 10;  // Max number used to dimasion array for TMT
    private long[] tmtArr = new long[tmtArrSize]; // Time for transmitting the "RequestTimeInfo"
    final private int zeroOffsetAvgSize = 10;
    private int zeroOffsetAvgCount = zeroOffsetAvgSize;  
    private long zeroOffset = 0; // Calculated offset as a zero value. 
                                 // Changed using cuWrapIncrement when CU clock wraps
    final private long cuWrapIncrement = 0x100000000L; // Wraps at (2^30)*4

    private long regOffset = 0; // Regulator output offset
    private long errorSum  = 0; // Sum of error - used by integrator
    final private long kInt = 64; // 1/ki - Number of milliseconds to inc/dec
    private int  pingSeqNum = 2;
    
    protected int max_timestamp = 0x0FFF;   // 12 bits timestamp wrap-around
    protected int phase_frame = 1024; 
    protected int max_seq_number = 3; // 2 bits
    
    /**************************************************************************
     *  Utility functions
     * ************************************************************************/
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    
    public String currentTimeStamp() {
        return timestampFormat.format( Calendar.getInstance().getTime());
    }
   
    /**************************************************************************
    *  Time synchronization algorithm
    * ************************************************************************/
    
    public void start_timesync() {
        refClockPrev = 0;
        tsPrev = 0; // Previous TS value used for TsFilter
        for(int i=0; i<tmtArrSize; i++)
            tmtArr[i] = 0; // Time for transmitting the "RequestTimeInfo"
        zeroOffsetAvgCount = zeroOffsetAvgSize;  
        zeroOffset = 0; // Calculated offset as a zero value. 
        regOffset = 0; // Regulator output offset
        errorSum  = 0; // Sum of error - used by integrator
        start_ping();
        for (ITimeSynchronizerLogger l : loggers) l.timeSyncStart();
    }
    
    public void stop_timesync() {
        stop_ping();
        for (ITimeSynchronizerLogger l : loggers) l.timeSyncStop();
    }
    
    private void send_ping() {
        //System.out.println("Ping");
        pingSeqNum++;
        if(pingSeqNum >= tmtArrSize)
            pingSeqNum = 2;
        tmtArr[pingSeqNum] = System.currentTimeMillis();
        device.sendTimeRequest(pingSeqNum);
        
        //System.out.println("Ping" + pingSeqNum);
    }
    
    public long getRegOffset() {
        return regOffset;
    }
    
    /**************************************************************************
     *  Handling of the pong and calculation of the magic offset
     * ************************************************************************/
    
    public void receive_TimeResponse(int timeSyncSeqNum, long value) {
        if (timeSyncSeqNum >= tmtArrSize)
                timeSyncSeqNum = 0;  // Range check of sequence number. Should never occur
            
            long tmr = System.currentTimeMillis();
            long ts = value * 4;  // Convert to milliseconds
            long dTs = ts - tsPrev; // Time between last ts - Used by TsFilter
            tsPrev = ts;
            long delay = (tmr - tmtArr[timeSyncSeqNum] ) /2; // round trip delay
            long offset = - ts + tmtArr[timeSyncSeqNum] + delay; // instant offset between PC and sensor clocks
            long error = offset - regOffset;
            int  skipped = 0; // Not skipped
            //System.out.println("Rep seqNum = " + timeSyncSeqNum + " Req seqNum = "+ pingSeqNum);
            
            // Check is the ping sequence number is correct
            if (tmtArr[timeSyncSeqNum] != 0 && timeSyncSeqNum == pingSeqNum) {
                // Sequence number is correct
                
                // TsFilter - Check if dTs is within limits
                if ((dTs < dTsMax) && (dTs > dTsMin)) {
                    // dTs is ok
                    
                    // zeroOffset calculation
                    if( zeroOffsetAvgCount == 0) {
                        // MaxOffsetFilter - Check if error is within limits
                        if(error > tsErrorMax) {
                          System.out.println("Limit - error(+) = " + error);
                          //error = tsErrorMax;
                          skipped = 4;
                        }
                        if(error < -tsErrorMax) {
                          System.out.println("Limit - error(-) = " + error);
                          //error = -tsErrorMax;
                          skipped = 5;
                        }
                        if(skipped == 0) {
                            // Running integrator
                        
                            errorSum += error;
                            regOffset = zeroOffset + (errorSum / kInt);
                        }
                    } else {
                        // Calculating average
                        skipped = 3;
                        zeroOffset += offset;
                        if(zeroOffsetAvgCount == 1) {
                            // Got correct number of samples....calculate average
                            zeroOffset /= zeroOffsetAvgSize;
                            regOffset = zeroOffset; // Initialize regOffset to "zero"
                            System.out.println("TimeSync=>zeroOffset calculated");
                        }
                        zeroOffsetAvgCount--;
                    }
                        
                } else {
                    // Skip all samples with dTs having too high delay variation
                    skipped = 2;
                    System.out.println("Skip - dTS = " + dTs);
                }
//                // zeroOffset calculation
//                if( zeroOffsetAvgCount == 0) {
//                    // RejectionFilter - Check if error is within limits
//                    if(error > tsErrorMax) {
//                      System.out.println("Limit - error(+) = " + error);
//                      error = tsErrorMax;
//                      skipped = 4;
//                    }
//                    if(error < -tsErrorMax) {
//                      System.out.println("Limit - error(-) = " + error);
//                      error = -tsErrorMax;
//                      skipped = 5;
//                    }
//                    // Running integrator
//                    
//                    errorSum += error;
//                    regOffset = zeroOffset + (errorSum / kInt);
//                } else {
//                    // Calculating average
//                    skipped = 3;
//                    zeroOffset += offset;
//                    if(zeroOffsetAvgCount == 1) {
//                        // Got correct number of samples....calculate average
//                        zeroOffset /= zeroOffsetAvgSize;
//                        regOffset = zeroOffset; // Initialize regOffset to "zero"
//                        System.out.println("TimeSync=>zeroOffset calculated");
//                    }
//                    zeroOffsetAvgCount--;
//                }
            } else {
                skipped = 1;
                System.out.println("Skip - Rep seqNum = " + timeSyncSeqNum + " Req seqNum = "+ pingSeqNum);
            }
            
            for (ITimeSynchronizerLogger l : loggers) 
                l.timeSyncLog(currentTimeStamp() , ts , tmtArr[timeSyncSeqNum] , tmr , delay , offset , errorSum , zeroOffset , regOffset , skipped);
            
            if (skipped != 1) tmtArr[timeSyncSeqNum] = 0; // Reset tmt to filter away other clock updates if sequence was correct.
    }
    
    /**************************************************************************
     *  Translation of device timestanp into synchronized timestamp
     * ************************************************************************/
    
    /*
    public String cbTimeStamp(int t) {
        Calendar regOffsetTod = Calendar.getInstance();
        long regOffsetMsEpoc = ((t+refTime)*4) + timeSync.getRegOffset();
        regOffsetTod.setTimeInMillis(regOffsetMsEpoc);

        //return timestampFormat.format( Calendar.getInstance().getTime());
        return (t+refTime-cbStartTime)*4 + SEPARATOR + timestampFormat.format(regOffsetTod.getTime()) + SEPARATOR + regOffsetMsEpoc;
    }
    */
    
    public long getSynchronizedEpochTime(int timestamp) {
        
        if (regOffset == 0) return 0; // We are not yet synchronized
        
        return timestamp + this.getRegOffset();
    }
    
    /**************************************************************************
     *  Sending of the time requests at reqular interval
     * ************************************************************************/
    protected boolean stop_request = false;
    protected boolean running = false;
       
    public boolean isRunning() {
        return running;
    }
    
    protected void start_ping() {
        if (!running) {
           stop_request = false;
           new Thread(this).start(); 
        }
    }
    
    protected void stop_ping() {
        if (running) {
           stop_request = true;
        }
    }

    @Override
    public void run() {
        running = true;
        try {
            do {
            // initiate a ping every "period" milliseconds
            send_ping();
            Thread.sleep(updateRate);
            } while (!stop_request);
        } catch (InterruptedException ex) {
            Logger.getLogger(TimeSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            running = false;
        }
    }
}
