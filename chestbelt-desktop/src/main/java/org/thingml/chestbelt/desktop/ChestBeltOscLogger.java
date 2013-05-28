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
    private VirtualOscComm vOscEcg;
    private VirtualOscComm vOscAccX;
    private VirtualOscComm vOscAccY;
    private VirtualOscComm vOscAccZ;
    private VirtualOscComm vOscGyrX;
    private VirtualOscComm vOscGyrY;
    private VirtualOscComm vOscGyrZ;
    private boolean logging = false;

    
    public ChestBeltOscLogger(String probeName, ChestBelt belt) {
        this.belt = belt;
        this.probeName = probeName;
        this.logging = false;
    }
    
    public void startLogging() {
           
           vOscEcg = new VirtualOscComm();
           vOscEcg.open_communication("127.0.0.1", this.probeName + ".Ecg");
           vOscAccX = new VirtualOscComm();
           vOscAccX.open_communication("127.0.0.1", this.probeName + ".AccX");
           vOscAccY = new VirtualOscComm();
           vOscAccY.open_communication("127.0.0.1", this.probeName + ".AccY");
           vOscAccZ = new VirtualOscComm();
           vOscAccZ.open_communication("127.0.0.1", this.probeName + ".AccZ");
           vOscGyrX = new VirtualOscComm();
           vOscGyrX.open_communication("127.0.0.1", this.probeName + ".GyrX");
           vOscGyrY = new VirtualOscComm();
           vOscGyrY.open_communication("127.0.0.1", this.probeName + ".GyrY");
           vOscGyrZ = new VirtualOscComm();
           vOscGyrZ.open_communication("127.0.0.1", this.probeName + ".GyrZ");
           logging = true;
    }
    
    public void stopLogging() {
        if (logging ) {
            logging = false;
            vOscEcg.close_communication();
            vOscEcg = null;
            vOscAccX.close_communication();
            vOscAccX = null;
            vOscAccY.close_communication();
            vOscAccY = null;
            vOscAccZ.close_communication();
            vOscAccZ = null;
            vOscGyrX.close_communication();
            vOscGyrX = null;
            vOscGyrY.close_communication();
            vOscGyrY = null;
            vOscGyrZ.close_communication();
            vOscGyrZ = null;
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
            vOscEcg.send_ts_data(ts, value);
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
            vOscEcg.send_ts_data(ts, value);
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

    protected float A(float v) {
        return (v * 0.004f);
    }
    protected float G(float v) {
        return (v * 0.069565f);
    }
    
    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        if (logging) {
            long ts = belt.getEpochTimestamp(timestamp);
            vOscAccX.send_ts_data(ts, A(ax));
            vOscAccY.send_ts_data(ts, A(ay));
            vOscAccZ.send_ts_data(ts, A(az));
            vOscGyrX.send_ts_data(ts, G(gx));
            vOscGyrY.send_ts_data(ts, G(gy));
            vOscGyrZ.send_ts_data(ts, G(gz));
        }
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

