/**
 * Copyright (C) 2012 SINTEF <steffen.dalgard@sintef.no>
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

import org.thingml.chestbelt.driver.ChestBelt;
import org.thingml.chestbelt.driver.ChestBeltListener;

/**
 *
 * @author steffend
 */
public class ChestBeltOscLogger  implements ChestBeltListener {

    private ChestBelt belt;
    private String probeName;
    private VirtualOscComm vOsc;
    private boolean logging = false;

    
    public ChestBeltOscLogger(String probeName, ChestBelt belt) {
        this.belt = belt;
        this.probeName = probeName;
        this.logging = false;
    }
    
    public void startLogging() {
           
           vOsc = new VirtualOscComm();
           vOsc.open_communication("127.0.0.1", this.probeName);
           logging = true;
    }
    
    public void stopLogging() {
        if (logging ) {
            logging = false;
            vOsc.close_communication();
            vOsc = null;
        }
    }
    
    
    @Override
    public void cUSerialNumber(long value, int timestamp) {
    }

    @Override
    public void cUFWRevision(String value, int timestamp) {
    }

    @Override
    public void batteryStatus(int value, int timestamp) {
    }

    @Override
    public void indication(int value, int timestamp) {
    }

    @Override
    public void status(int value, int timestamp) {
    }

    @Override
    public void messageOverrun(int value, int timestamp) {
    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
    }

    @Override
    public void heartRate(int value, int timestamp) {
    }

    @Override
    public void heartRateConfidence(int value, int timestamp) {
    }

    
    private int ecg_timestamp = 0;
    @Override
    public void eCGData(int value) {
        ecg_timestamp += 4;
        if (logging) {
            long ts = belt.getEpochTimestampFromMs(ecg_timestamp);
            vOsc.send_data(ts, value);
        }

    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        ecg_timestamp = timestamp*4;
        //System.out.println("ecgRaw" + logging);
        if (logging) {
            long ts = belt.getEpochTimestampFromMs(ecg_timestamp);
            vOsc.send_data(ts, value);
        }
    }
    
    @Override
    public void eMGData(int value) {
    }

    @Override
    public void eMGSignalQuality(int value, int timestamp) {
    }

    @Override
    public void eMGRaw(int value, int timestamp) {
    }

    @Override
    public void eMGRMS(int channelA, int channelB, int timestamp) {
    }

    @Override
    public void gyroPitch(int value, int timestamp) {
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
    }

    @Override
    public void accLateral(int value, int timestamp) {
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
    }

    @Override
    public void accVertical(int value, int timestamp) {
    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
    }

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
    }

    @Override
    public void skinTemperature(int value, int timestamp) {
    }

    @Override
    public void connectionLost() {
    }
    
    @Override
    public void referenceClockTimeSync(int timeSyncSeqNum, long value) {
        
    }
    
}

