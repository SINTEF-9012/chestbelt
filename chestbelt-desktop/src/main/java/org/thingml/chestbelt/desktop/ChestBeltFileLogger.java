/**
 * Copyright (C) 2012 SINTEF <franck.fleurey@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June 2007;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.chestbelt.desktop;

import org.thingml.chestbelt.driver.ChestBeltListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ffl
 */
public class ChestBeltFileLogger implements ChestBeltListener {
    
    private SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private String SEPARATOR = "\t";
    
    protected File folder;
    protected boolean logging = false;
    protected boolean request_start = false;
    protected long startTime = 0;
    protected long cbStartTime = 0;
    protected PrintWriter log;
    protected PrintWriter ecg;
    protected PrintWriter imu;
    protected PrintWriter phi;
    
    public ChestBeltFileLogger(File folder) {
        this.folder = folder;
        //numFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
        //imuFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.US));
    }
    
    public boolean isLogging() {
        return logging;
    }
    
    public void startLogging() {
       String sName = createSessionName(); 
       File sFolder = new File(folder, sName);
       sFolder.mkdir();
       try {
           log = new PrintWriter(new FileWriter(new File(sFolder, "Esusm_log.csv")));
           log.println("# This file contains one line per message received from the Chest Belt.");
           ecg = new PrintWriter(new FileWriter(new File(sFolder, "Esusm_ecg.csv")));
           ecg.println("# ECG Data, Raw 12bits ADC values, 250Hz.");
           imu = new PrintWriter(new FileWriter(new File(sFolder, "Esusm_imu.csv")));
           imu.println("Time" + SEPARATOR + "Time (ms)" + SEPARATOR + "Timestamp" + SEPARATOR + "AX" + SEPARATOR + "AY" + SEPARATOR + "AZ" + SEPARATOR + "GX" + SEPARATOR + "GY" + SEPARATOR + "GZ");
           phi = new PrintWriter(new FileWriter(new File(sFolder, "Esusm_phi.csv")));
           phi.println("Time" + SEPARATOR + "Time (ms)" + SEPARATOR + "Timestamp" + SEPARATOR + "Heart Rate (BPM)" + SEPARATOR + "Temperature (°C)");
           
       } catch (IOException ex) {
           Logger.getLogger(ChestBeltFileLogger.class.getName()).log(Level.SEVERE, null, ex);
       }
       temperature = 0;
       request_start = true;
    }
    
    public void stopLogging() {
        if (logging || request_start) {
            logging = false;
            request_start = false;
            log.close();
            ecg.close();
            imu.close();
            phi.close();
            log = null;
            ecg = null;
            imu = null;
            phi = null;
        }
    }
    
    public String createSessionName() {
        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return timestampFormat.format( Calendar.getInstance().getTime());
    }
    
    public String currentTimeStamp() {
        //return timestampFormat.format( Calendar.getInstance().getTime());
        return timestampFormat.format( Calendar.getInstance().getTime()) + SEPARATOR + (System.currentTimeMillis()-startTime);
    }
    
    public long cbTimeStamp(int t) {
        //return timestampFormat.format( Calendar.getInstance().getTime());
        return (t+refTime-cbStartTime)*4;
    }

    @Override
    public void cUSerialNumber(long value, int timestamp) {
        if (logging) log.println("[SerialNumber]" + SEPARATOR + currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void cUFWRevision(String value, int timestamp) {
        if (logging) log.println("[FWRevision]" + SEPARATOR + currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void batteryStatus(int value, int timestamp) {
        if (logging) log.println("[Battery]" + SEPARATOR + currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void indication(int value, int timestamp) {
        if (logging) log.println("[Indication]" + SEPARATOR + currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void status(int value, int timestamp) {
        if (logging) log.println("[Status]" + SEPARATOR + currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void messageOverrun(int value, int timestamp) {
        if (logging) log.println("[MsgOverrun]" + SEPARATOR + currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + value);
    }
    
    protected long refTime = 0;
    protected boolean inSeconds = true;

    @Override
    public void referenceClockTime(long value, boolean seconds) {
        refTime = value;
        inSeconds = seconds;
        if (request_start) {
            request_start = false;
            startTime = System.currentTimeMillis();
            ecgtimestamp = 0;
            cbStartTime = refTime;
            logging = true;
        }
        if (logging) log.println("[RefClock]" + SEPARATOR + currentTimeStamp() + SEPARATOR + value);
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        if (logging) log.println("[FullClock]" + SEPARATOR + currentTimeStamp() + SEPARATOR + value);
    }

    private DecimalFormat numFormat = new DecimalFormat("##.0");
    @Override
    public void heartRate(int value, int timestamp) {
        double hr = value/10.0;
        double temp = temperature/10.0;
        if (logging) phi.println(currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + numFormat.format(hr) + SEPARATOR + numFormat.format(temp));
    }

    @Override
    public void heartRateConfidence(int value, int timestamp) {
        if (logging) log.println("[HeartRateConfidence]" + SEPARATOR + currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + value);
    }

    private long ecgtimestamp = 0;
    @Override
    public void eCGData(int value) {
        if (logging) {
            ecg.println("" + /*currentTimeStamp() + SEPARATOR +*/ ecgtimestamp + SEPARATOR + value);
        }
        ecgtimestamp += 4; // 4 ms increment
    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        if (logging) log.println("[ECGSignalQuality]" + SEPARATOR + currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void gyroPitch(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accLateral(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void accVertical(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private DecimalFormat imuFormat = new DecimalFormat("0.00000");
    
    protected String A(int v) {
        return imuFormat.format(v * 0.004);
    }
    protected String G(int v) {
        return imuFormat.format(v * 0.069565);
    }
    

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        if (logging) {
            imu.println(currentTimeStamp() + SEPARATOR + cbTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
        }
    }

    private int temperature = 0;
    @Override
    public void skinTemperature(int value, int timestamp) {
        temperature = value;
    }

    @Override
    public void connectionLost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
