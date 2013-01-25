/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.timesync;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    
}
