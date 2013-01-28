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
public class TimeSynchronizerFileLogger implements ITimeSynchronizerLogger {

     /**************************************************************************
     *  Logging of the Time Synchronization
     * ************************************************************************/
    //protected File folder;
    protected boolean logging = false;
    protected PrintWriter log;
    
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private String SEPARATOR = "\t";
    
    public String createSessionName() {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return "TS-" + timestampFormat.format( Calendar.getInstance().getTime());
    }
    
    private void start_logging(File folder) {
       String sName = createSessionName(); 
       File sFolder = new File(folder, sName);
       sFolder.mkdir();
        try {
           log = new PrintWriter(new FileWriter(new File(sFolder, "Chestbelt_time.txt")));
           log.println("Time" + SEPARATOR + "TS" + SEPARATOR + "TMT" + SEPARATOR + "TMR" + SEPARATOR + "delay" + SEPARATOR + "offs" + SEPARATOR + "errorSum" + SEPARATOR + "zeroOffset" + SEPARATOR + "regOffsMs"+ SEPARATOR + "skipped");
           logging = true;
       } catch (IOException ex) {
           Logger.getLogger(org.thingml.chestbelt.desktop.ChestBeltFileLogger.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
    private void stop_logging() {
        if (logging) {
            logging = false;
            log.close();
            log = null;
        }
    }
    
    @Override
    public void timeSyncLog(String time, long ts, long tmt, long tmr, long delay, long offs, long errorSum, long zeroOffset, long regOffsMs, int skipped) {
        if (logging) log.println(time + SEPARATOR + ts + SEPARATOR + tmt + SEPARATOR + tmr + SEPARATOR + delay + SEPARATOR + offs + SEPARATOR + errorSum + SEPARATOR + zeroOffset + SEPARATOR + regOffsMs + SEPARATOR + skipped);
    }

    @Override
    public void timeSyncStart() {
        
    }

    @Override
    public void timeSyncStop() {
        
    }

    @Override
    public void timeSyncPong(int delay, int dtt, int dtr, int dts) {
        
    }
    
}
