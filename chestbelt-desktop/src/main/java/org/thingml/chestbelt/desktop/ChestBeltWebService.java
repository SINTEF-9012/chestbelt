/**
 * Copyright (C) 2012 SINTEF <franck.fleurey@sintef.no>
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3, 29 June
 * 2007; you may not use this file except in compliance with the License. You
 * may obtain a copy of the License at
 *
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.chestbelt.desktop;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.thingml.chestbelt.driver.ChestBelt;
import org.thingml.chestbelt.driver.ChestBeltListener;

/**
 *
 * @author ffl
 */
public class ChestBeltWebService implements ChestBeltListener {

    private SimpleDateFormat timestampFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    private String SEPARATOR = "\t";
    //private int temperature = 0;

    protected String url;
    protected String Gateway;
    protected String Sensorstore;
    protected String Computer;
    protected boolean logging = false;

    protected boolean eCGEpoch = false;
    
    /* try {
    Thread.sleep(1000);
    }
    catch(InterruptedException ex) {
    Thread.currentThread().interrupt();
    }*/

    public boolean iseCGEpoch() {
        return eCGEpoch;
    }

    private ChestBelt belt;

    public ChestBeltWebService(String url, String Gateway, String Sensorstore, String Computer, ChestBelt belt) {
        this.belt = belt;
        this.url = url;
        this.Gateway = Gateway;
        this.Sensorstore = Sensorstore;
        this.Computer = Computer;
        this.eCGEpoch = false;

        belt.getSerialNumber();
    }

    public ChestBeltWebService(String url, String Gateway, String Sensorstore, String Computer, ChestBelt belt,  boolean eCGEpoch) {
        this.belt = belt;
        this.url = url;
        this.Gateway = Gateway;
        this.Sensorstore = Sensorstore;
        this.Computer = Computer;
        this.eCGEpoch = eCGEpoch;
    }

    public boolean isLogging() {
        return logging;
    }

    public void startLoggingInFolder(String url) {

        temperature = 0;
        heartrate = 0;
        logging = true;
    }

    public void startLogging() {
        startLoggingInFolder(url);
    }

    public void stopLogging() {

        if (logging) {
            logging = false;
        }
    }

    public String currentTimeStamp() {
        return "" + System.currentTimeMillis();
    }

    public String calculatedAndRawTimeStamp(int belt_timestamp) {
        return "" + belt.getEpochTimestamp(belt_timestamp) + SEPARATOR + belt_timestamp * 4;
    }

    @Override
    public void cUSerialNumber(long value, int timestamp) {
        //if (logging) log.println("[SerialNumber]" + SEPARATOR + currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void cUFWRevision(String value, int timestamp) {
        // if (logging) log.println("[FWRevision]" + SEPARATOR + currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }
    
    private int battery = 0;
    @Override
    public void batteryStatus(int value, int timestamp) {//////////////////////////////
        battery = value;
        

        //if (logging) log.println("[Battery]" + SEPARATOR + currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }

    private int activity = 0;
    private int posture = 0;

    @Override
    public void indication(int value, int timestamp) {
        if (value >= 1 && value <= 6) {
         posture = value;
        }
        else if (value >=10 && value <=13) {
         activity = value;    
        }
        //if (logging) log.println("[Indication]" + SEPARATOR + currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void status(int value, int timestamp) {
        //if (logging) log.println("[Status]" + SEPARATOR + currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void messageOverrun(int value, int timestamp) {
        // if (logging) log.println("[MsgOverrun]" + SEPARATOR + currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }

    //protected long refTime = 0;
    //protected boolean inSeconds = true;
    @Override
    public void referenceClockTime(long value, boolean seconds) {
        // This function needs to be rewritten. 
        //   Do we need the old clock system?
        //   Apart from the old clock there is only the handling of "logging" and printing to log.
        //refTime = value;
        //inSeconds = seconds;
        //if (request_start) {
        //    request_start = false;
        //    startTime = System.currentTimeMillis();
        // Removed to avoid interference with old time signals. ecg_timestamp = 0;
        // Removed to avoid interference with old time signals. emg_timestamp = 0;
        //    cbStartTime = refTime;
        //    logging = true;
        //}
        //if (logging) log.println("[RefClock]" + SEPARATOR + currentTimeStamp() + SEPARATOR + value);
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        // if (logging) log.println("[FullClock]" + SEPARATOR + currentTimeStamp() + SEPARATOR + value);
    }

    private int heartrate = 0;
    private int chronos = 0;
    
    
    @Override
    public void heartRate(int value, int timestamp) {
        heartrate = value;
        chronos = timestamp;
        
        //Send date??
        /*SimpleDateFormat timestampFormat = new SimpleDateFormat("HH-mm-ss");
        String currentTime = timestampFormat.format( Calendar.getInstance().getTime());*/
        
        
        String HRstr = String.valueOf(heartrate/10);
        String TempStr = String.valueOf(temperature/10);
        String ActivityStr = String.valueOf(activity);
        String BatteryStr = String.valueOf(battery);
        String PostureStr = String.valueOf(posture);
        String ChronosStr = String.valueOf(chronos);
        
        SensorXmlClass SensorXml = new SensorXmlClass(HRstr, TempStr, ActivityStr, BatteryStr, PostureStr, ChronosStr);
        
        String urlParameters = "<IntegrationServiceMessage xmlns=\"http://Imatis.Fundamentum.IntegrationService.IntegrationServiceMessage\">\n"                  
            + "               <Adapter>sintef-chestbelt</Adapter>\n"
            + "                 <Message><![CDATA[<Message>\n" 
            + "                 <Gateway>\n"
            + "                    <GatewayId>" + Gateway + "</GatewayId>\n"         
            + "                    <Connected>1</Connected>\n"                    
            + "                 </Gateway>\n" 
            +                   SensorXml.getXml() 
            + "</Message>]]></Message>\n"
            + "</IntegrationServiceMessage>";
        
        

        try {

            URL obj = new URL(url);
            HttpURLConnection con1 = (HttpURLConnection) obj.openConnection();
            
            String urlParametersupdated = urlParameters.replace("HRstr", HRstr);
            
            //add reuqest header
            con1.setRequestMethod("POST");
            con1.setRequestProperty("Content-Type", "text/xml");
            con1.setRequestProperty("Accept", "text/xml");

            con1.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con1.getOutputStream());
            wr.writeBytes(urlParametersupdated);
            wr.flush();
            wr.close();

            int responseCode = con1.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParametersupdated);
            System.out.println("Response Code : " + responseCode);
            
            //test
 
            //

        } catch (MalformedURLException ex) {
            Logger.getLogger(ChestBeltWebService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ProtocolException ex) {
            Logger.getLogger(ChestBeltWebService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChestBeltWebService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void heartRateConfidence(int value, int timestamp) {
        // if (logging) log.println("[HeartRateConfidence]" + SEPARATOR + currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }

    private int ecg_timestamp = 0;

    @Override
    public void eCGData(int value) {
        ecg_timestamp += 4;
        /*        if (logging) {
         if(!eCGEpoch) {
         // This can be used to log without timestamp for each sample to keep the file smaller.
         ecg.println(value);
         } else {
         // This can be used to log the timestamp for each sample but it makes the file really big.
         long ts = belt.getEpochTimestampFromMs(ecg_timestamp);
         ecg.println(value + SEPARATOR + currentTimeStamp() + SEPARATOR + ts + SEPARATOR + ecg_timestamp + SEPARATOR + 0);
         }
         }*/

    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        // if (logging) log.println("[ECGSignalQuality]" + SEPARATOR + currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + value);
    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        ecg_timestamp = timestamp * 4;
        /* if (logging) {
         long ts = belt.getEpochTimestampFromMs(ecg_timestamp);
         ecg.println(value + SEPARATOR + currentTimeStamp() + SEPARATOR + ts + SEPARATOR + ecg_timestamp + SEPARATOR + 1);
         }*/
    }

    //int ax, ay, az, gx, gy, gz;

    /*    private void imu_data_reset() {
     //System.out.println("reset");
     ax = Integer.MIN_VALUE;
     ay = Integer.MIN_VALUE;
     az = Integer.MIN_VALUE;
     gx = Integer.MIN_VALUE;
     gy = Integer.MIN_VALUE;
     gz = Integer.MIN_VALUE;
     }*/
    /*private boolean imu_data_ready() {
     return ax != Integer.MIN_VALUE && ay != Integer.MIN_VALUE && az != Integer.MIN_VALUE &&
     gx != Integer.MIN_VALUE && gy != Integer.MIN_VALUE && gz != Integer.MIN_VALUE;
     }*/
    @Override
    public void gyroPitch(int value, int timestamp) {
        /* if (logging) {
         //System.out.println("gy");
         if (gy == Integer.MIN_VALUE) {
         gy = value;
         if (imu_data_ready()) {
         imu.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
         imu_data_reset();
         }
         }
         else {
         imu_data_reset();
         gy = value;
         }
         }*/
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
        /* if (logging) {
         //System.out.println("gx");
         if (gx == Integer.MIN_VALUE) {
         gx = value;
         if (imu_data_ready()) {
         imu.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
         imu_data_reset();
         }
         }
         else {
         imu_data_reset();
         gx = value;
         }
         }*/
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
        /*if (logging) {
         //System.out.println("gz");
         if (gz == Integer.MIN_VALUE) {
         gz = value;
         if (imu_data_ready()) {
         imu.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
         imu_data_reset();
         }
         }
         else {
         imu_data_reset();
         gz = value;
         }
         }*/
    }

    @Override
    public void accLateral(int value, int timestamp) {
        /*if (logging) {
         //System.out.println("ay");
         if (ay == Integer.MIN_VALUE) {
         ay = value;
         if (imu_data_ready()) {
         imu.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
         imu_data_reset();
         }
         }
         else {
         imu_data_reset();
         ay = value;
         }
         }*/
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        /*if (logging) {
         //System.out.println("az");
         if (az == Integer.MIN_VALUE) {
         az = value;
         if (imu_data_ready()) {
         imu.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
         imu_data_reset();
         }
         }
         else {
         imu_data_reset();
         az = value;
         }
         }*/
    }

    @Override
    public void accVertical(int value, int timestamp) {
        /*if (logging) {
         //System.out.println("ax");
         if (ax == Integer.MIN_VALUE) {
         ax = value;
         if (imu_data_ready()) {
         imu.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
         imu_data_reset();
         }
         }
         else {
         imu_data_reset();
         ax = value;
         }
         }*/
    }
    
        
    @Override
    public void rawActivityLevel(int value, int timestamp) {//////////////////////////////////////////
        

        //throw new UnsupportedOperationException("Not supported yet.");
    }

   // private DecimalFormat imuFormat = new DecimalFormat("0.00000");
    /* protected String A(int v) {
     return imuFormat.format(v * 0.0039); // Changed from 0.004
     }
     protected String G(int v) {
     return imuFormat.format(v * 0.07); // Changed from 0.069565
     }*/
    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        /*if (logging) {
         imu.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + A(ax) + SEPARATOR + A(ay) + SEPARATOR + A(az) + SEPARATOR + G(gx) + SEPARATOR + G(gy) + SEPARATOR + G(gz));
         }*/
    }

    //private DecimalFormat numFormat = new DecimalFormat("##.0");
    private int temperature = 0;

    @Override
    public void skinTemperature(int value, int timestamp) {//////////////////////////////////////////
        temperature = value;
        //double hr = heartrate / 10.0;
        //double temp = temperature / 10.0;
        
        /*if (logging) phi.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + numFormat.format(hr) + SEPARATOR + numFormat.format(temp));*/
    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void eMGData(int value) {
        emg_timestamp += 1;
        /*if (logging) {
         if (!eCGEpoch) {
         // This can be used to log without timestamp for each sample to keep the file smaller.
         emg.println(value);
         } else {
         // This can be used to log the timestamp for each sample but it makes the file really big.
         long ts = belt.getEpochTimestampFromMs(emg_timestamp);
         emg.println(value + SEPARATOR + currentTimeStamp() + SEPARATOR + ts + SEPARATOR + emg_timestamp + SEPARATOR + 0);
         }
         }*/
    }

    private int emg_timestamp = 0;

    @Override
    public void eMGSignalQuality(int value, int timestamp) {

    }

    /**
     *
     * @param value
     * @param timestamp
     */
    @Override
    public void eMGRaw(int value, int timestamp) {
        emg_timestamp = timestamp * 4;
        /*if (logging) {
         long ts = belt.getEpochTimestampFromMs(emg_timestamp);
         emg.println(value + SEPARATOR + currentTimeStamp() + SEPARATOR + ts + SEPARATOR + emg_timestamp + SEPARATOR + 1);
         }*/
    }

    //int rmsCount = 0;
    @Override
    public void eMGRMS(int channelA, int channelB, int timestamp) {
        /*if (logging) {
         rms.println(currentTimeStamp() + SEPARATOR + calculatedAndRawTimeStamp(timestamp) + SEPARATOR + channelA + SEPARATOR + channelB);
         }*/
        //System.err.println("eMGRMS(" + name + ") #" + rmsCount++ + logging);
    }

    @Override
    public void referenceClockTimeSync(int timeSyncSeqNum, long value) {

    }

    /*    @Override
    public void logOrientation(int[] orientationRaw, int[] orientationFiltered, int timestamp) {
    int phiDeg = orientationRaw[0];
    int rhoDeg = orientationRaw[1];
    int phiDegFilt = orientationFiltered[0];
    int rhoDegFilt = orientationFiltered[1];*/
    
    /*double hr = heartrate/10.0;
    double temp = temperature/10.0;
    
    SimpleDateFormat timestampFormat = new SimpleDateFormat("HH-mm-ss");
    String currentTime = timestampFormat.format( Calendar.getInstance().getTime());
    
    long currentTimeMillis = System.currentTimeMillis();
    double timeSinceStart = (currentTimeMillis-startTimeMillis)/1000;*/
        /*if (logging) {
        orientation.println(currentTime + SEPARATOR + timeSinceStart + SEPARATOR + hr + SEPARATOR + activityLevel + SEPARATOR + temp + SEPARATOR + rhoDegFilt + SEPARATOR + phiDegFilt + SEPARATOR + rhoDeg + SEPARATOR + phiDeg);
        }*/
    //}
    
    /*  private int posture;
    @Override
    public void orientation(int[] value, int timestamp) {
    posture = value;
    }
    */
}


//http://visitest.imatiscloud.com/Imatis/WebServices/External/IntegrationService/IntegrationService.svc/web/Send
            /*            "<IntegrationServiceMessage xmlns=\"http://Imatis.Fundamentum.IntegrationService.IntegrationServiceMessage\">\n"
            + "                 <Adapter>dsjtest</Adapter>\n"
            + "                 <Message><![CDATA[<Message>\n"
            + "                <Gateway>\n"
            + "                    <GatewayId>99887766</GatewayId>\n"
            + "                    <Connected>1</Connected>\n"
            + "                </Gateway>\n"
            + "                <Sensors>\n"
            + "                    <Sensor>\n"
            + "                        <TimeStamp>18.06.2014 16:43:49,61</TimeStamp>\n"
            + "                        <SensorId>SENSOR-1001</SensorId>\n"
            + "                        <ComputerId>SENSOR-1001</ComputerId>\n"
            + "                            <Values>\n"
            + "                                <Value>\n"
            + "                                    <Name>SpO2-D</Name>\n"
            + "                                    <Val>99</Val>\n"
            + "                                </Value>\n"
            + "                                <Value>\n"
            + "                                    <Name>HR-D</Name>\n"
            + "                                    <Val>HRstr</Val>\n"
            + "                                </Value>\n"
            + "                                <Value>\n"
            + "                                    <Name>Sensor Disconnect</Name>\n"
            + "                                    <Val>0</Val>\n"
            + "                                </Value>\n"
            + "                                <Value>\n"
            + "                                    <Name>Sensor Alarm</Name>\n"
            + "                                    <Val>0</Val>\n"
            + "                                </Value>\n"
            + "                            </Values>\n"
            + "                    </Sensor>\n"
            + "                    <Sensor>\n"
            + "                        <Date>2014-02-10T12:09:32</Date>\n"
            + "                        <CompanyCode>2</CompanyCode>\n"
            + "                        <CompanyName>The Test Company</CompanyName>\n"
            + "                        <UnitCode>1360</UnitCode>\n"
            + "                        <UnitSerial>1326000180</UnitSerial>\n"
            + "                        <UnitName>1326000180</UnitName>\n"
            + "                        <AlarmCode>10</AlarmCode>\n"
            + "                        <AlarmType>ChangedToNormal</AlarmType>\n"
            + "                        <TagSerial/>\n"
            + "                        <TagName/>\n"
            + "                        <Battery>68</Battery>\n"
            + "                        <Latitude>56.04809939</Latitude>\n"
            + "                        <Longitude>9.95033166</Longitude>\n"
            + "                        <Radius>1334</Radius>\n"
            + "                        <Gps>0</Gps>\n"
            + "                    </Sensor>\n"
            + "                </Sensors>\n"
            + "</Message>]]></Message>\n"
            + "</IntegrationServiceMessage>";*/