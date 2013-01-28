package org.thingml.timesync;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeSynchronizer implements Runnable {

    /**
     * ************************************************************************
     * constructor and link to the device
     * ***********************************************************************
     */
    protected TimeSynchronizable device;

    public TimeSynchronizer(TimeSynchronizable device) {
        this.device = device;
    }
    /**
     * ************************************************************************
     * Logging
     * ***********************************************************************
     */
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
    /**
     * ************************************************************************
     * Configuration and parameters of the Time Sync Algorithm
     * ***********************************************************************
     */
    final private int updateRate = 250;  // Rate for ping in milliseconds
    final private int dTsMax = 275; // Maximum delta time for TsFilter
    final private int dTsMin = 225; // Minimum delta time for TsFilter
    final private int dTsDeltaMax = 25; // delta time for TsFilter
    final private long tsErrorMax = 50; // Maximum deviation for a calculated offset compared to regOffset
    // private long refClockPrev = 0; // Previous fullClock value used to detect wraparound
    private long tsPrev = 0; // Previous TS value used for TsFilter
    private long tmrPrev = 0; // Previous TS value used for TsFilter
    private long tmtPrev = 0; // Previous TS value used for TsFilter
    final private int tmtArrSize = 10;  // Max number used to dimasion array for TMT
    private long[] tmtArr = new long[tmtArrSize]; // Time for transmitting the "RequestTimeInfo"
    final private int zeroOffsetAvgSize = 10;
    private int zeroOffsetAvgCount = zeroOffsetAvgSize;
    private long zeroOffset = 0; // Calculated offset as a zero value. 
    // Changed using cuWrapIncrement when CU clock wraps
    //final private long cuWrapIncrement = 0x100000000L; // Wraps at (2^30)*4
    private long regOffset = 0; // Regulator output offset
    private long errorSum = 0; // Sum of error - used by integrator
    final private long kInt = 64; // 1/ki - Number of milliseconds to inc/dec
    private int pingSeqNum = 2;
    protected int max_timestamp = 0x0FFF;   // 12 bits timestamp wrap-around
    protected int phase_frame = 1024;
    protected int max_seq_number = 3; // 2 bits
    /**
     * ************************************************************************
     * Utility functions
     * ***********************************************************************
     */
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss.SSS");

    public String currentTimeStamp() {
        return timestampFormat.format(Calendar.getInstance().getTime());
    }

    /**
     * ************************************************************************
     * Time synchronization algorithm
     * ***********************************************************************
     */
    public void start_timesync() {
        //refClockPrev = 0;
        state = INIT;
        tsPrev = 0; // Previous TS value used for TsFilter
        tmrPrev = 0;
        tmtPrev = 0;
        for (int i = 0; i < tmtArrSize; i++) {
            tmtArr[i] = 0; // Time for transmitting the "RequestTimeInfo"
        }
        zeroOffsetAvgCount = zeroOffsetAvgSize;
        zeroOffset = 0; // Calculated offset as a zero value. 
        regOffset = 0; // Regulator output offset
        errorSum = 0; // Sum of error - used by integrator
        start_ping();
        for (ITimeSynchronizerLogger l : loggers) {
            l.timeSyncStart();
        }
    }

    public void stop_timesync() {
        stop_ping();
        for (ITimeSynchronizerLogger l : loggers) {
            l.timeSyncStop();
        }
    }

    private void send_ping() {
        //System.out.println("Ping");
        pingSeqNum++;
        if (pingSeqNum >= tmtArrSize) {
            pingSeqNum = 2;
        }
        tmtArr[pingSeqNum] = System.currentTimeMillis();
        device.sendTimeRequest(pingSeqNum);

        //System.out.println("Ping" + pingSeqNum);
    }

    public long getRegOffset() {
        return regOffset;
    }
    /**
     * ************************************************************************
     * Handling of the pong and calculation of the magic offset
     * ***********************************************************************
     */
    private static int INIT = 0;
    private static int READY = 1;
    //private static int BEFORE_WRAP = 2;
    //private static int AFTER_WRAP = 3;
    private int state = INIT;

    public void receive_TimeResponse(int timeSyncSeqNum, long value) {

        //--------------------------------------
        // 1) Validate the ping sequence number
        //--------------------------------------
        if (timeSyncSeqNum >= tmtArrSize) {
            timeSyncSeqNum = 0;  // Range check of sequence number. Should never occur
        }
        if (tmtArr[timeSyncSeqNum] == 0 || timeSyncSeqNum != pingSeqNum) {// The the ping sequence number is not correct
            System.out.println("Skip - Rep seqNum = " + timeSyncSeqNum + " Req seqNum = " + pingSeqNum);
            return;
        }

        //---------------------------------------------
        // 2) Collect all timing data (TMT, TMR and TS)
        //---------------------------------------------
        long tmr = System.currentTimeMillis();
        long tmt = tmtArr[timeSyncSeqNum];
        tmtArr[timeSyncSeqNum] = 0; // Reset tmt to filter away other clock updates if sequence was correct.
        long ts = value;  // Convert to milliseconds
        int dTmr = (int) (tmr - tmrPrev); // time between the 2 last Pongs
        int dTmt = (int) (tmt - tmtPrev); // Time between the 2 last Pings
        int dTs = (int) (ts - tsPrev); // Time between last ts - Used by TsFilter

        for (ITimeSynchronizerLogger l : loggers) {
            l.timeSyncPong((int) (tmr - tmt), (int) dTmt, (int) dTmr, (int) dTs);
        }

        tmrPrev = tmr;
        tmtPrev = tmt;
        tsPrev = ts;

        //---------------------------------------------------------
        // 3) Filter abnormal delays - dTs too different from dTmr
        //---------------------------------------------------------
        if (dTs < dTmt - dTsDeltaMax || dTs > dTmt + dTsDeltaMax) {
            System.out.println("Skip by dTs Filter - dTS = " + dTs);
            return;
        }

        //---------------------------------------------------------
        // 4) Init the Zero offset in initialization phase
        //---------------------------------------------------------

        int delay = (int)(tmr - tmt) / 2; // round trip delay
        long offset = -ts + tmt + delay; // instant offset between PC and sensor clocks

        if (state == INIT) {
            zeroOffset += offset;
            if (zeroOffsetAvgCount == 1) {
                // Got correct number of samples....calculate average
                zeroOffset /= zeroOffsetAvgSize;
                regOffset = zeroOffset; // Initialize regOffset to "zero"
                System.out.println("TimeSync=>zeroOffset calculated");
                state = READY;
            }
            zeroOffsetAvgCount--;
            return;
        }

        //---------------------------------------------------------
        // 5) Running the regulator
        //---------------------------------------------------------
        
        long error = offset - regOffset;
        
        if (state == READY) {
            if (error > tsErrorMax) {
                System.out.println("Limit - error(+) = " + error);
            }
            else if (error < -tsErrorMax) {
                System.out.println("Limit - error(-) = " + error);
            }
            else {
                // Running integrator
                errorSum += error;
                regOffset = zeroOffset + (errorSum / kInt);
            }

        }

        for (ITimeSynchronizerLogger l : loggers) {
            l.timeSyncLog(currentTimeStamp(), ts, tmt, tmr, delay, offset, errorSum, zeroOffset, regOffset, state);
        }

    }

    /**
     * ************************************************************************
     * Translation of device timestanp into synchronized timestamp
     * ***********************************************************************
     */
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

        if (regOffset == 0) {
            return 0; // We are not yet synchronized
        }
        return timestamp + this.getRegOffset();
    }
    /**
     * ************************************************************************
     * Sending of the time requests at reqular interval
     * ***********************************************************************
     */
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
        } finally {
            running = false;
        }
    }
}
