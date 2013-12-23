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
import static java.lang.Math.atan2;
import static java.lang.Math.round;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import org.thingml.chestbelt.driver.ChestBelt;
import org.thingml.chestbelt.driver.ChestBeltListener;

/**
 *
 * @author oyvsta
 */
public class OrientationCalculator implements ChestBeltListener {
    
    private int longitudinalAccelerationComponent; 
    private int lateralAccelerationComponent; 
    private int verticalAccelerationComponent; 
    
    private int phiDegInt;
    private int rhoDegInt;
    private int thetaDegInt;
    
    protected ChestBelt belt;
    private ArrayList<OrientationCalculatorListener> listeners = new ArrayList<OrientationCalculatorListener>();
    
    public OrientationCalculator(ChestBelt b){
        this.belt = b;
        if (b != null) b.addChestBeltListener(this);

        longitudinalAccelerationComponent = 0;
        lateralAccelerationComponent = 0;
        verticalAccelerationComponent = 0;
        phiDegInt = 0;
        rhoDegInt = 0;
        thetaDegInt = 0;
    }

    public void addOrientationCalculatorListener(OrientationCalculatorListener l) {
        listeners.add(l);
    }

    public void removeOrientationCalculatorListener(OrientationCalculatorListener l) {
        listeners.remove(l);
    }    
        
    @Override
    public void cUSerialNumber(long value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cUFWRevision(String value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void batteryStatus(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void indication(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void status(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void messageOverrun(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void heartRate(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void heartRateConfidence(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eCGData(int value) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eCGRaw(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eMGData(int value) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eMGSignalQuality(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eMGRaw(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eMGRMS(int channelA, int channelB, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gyroPitch(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gyroRoll(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void gyroYaw(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   @Override
    public void accLateral(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
        lateralAccelerationComponent = value;
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
        longitudinalAccelerationComponent = value;
        calculateOrientation();
        sendDataToListeners();
    }

    @Override
    public void accVertical(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
        verticalAccelerationComponent = value;
    }

    @Override
    public void rawActivityLevel(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void skinTemperature(int value, int timestamp) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void connectionLost() {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void referenceClockTimeSync(int timeSyncSeqNum, long value) {
        //        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    private void calculateOrientation(){
        // Implementation of equations on p. 4 of "Tilt Sensing Using Linear 
        // Accelerometers", Kimberly Tuck, AN3461, Rev. 2, 06/2007
        double x = longitudinalAccelerationComponent;
        double y = lateralAccelerationComponent;
        double z = verticalAccelerationComponent;
        
        
        // Calculate angle between longitudinal axis (normal to chest, pointing outwards)
        // and horizontal plane
        double phiRad;
        double phiDeg;
        phiRad = atan2(y,sqrt(x*x + z*z));
        phiDeg = round(Math.toDegrees(phiRad));
                
        
        // Calculate angle between lateral axis (extening outwards from right 
        // side of body) and the horizontal plane 
        double rhoRad;
        double rhoDeg;
        rhoRad = atan2(x,sqrt(y*y + z*z));
        rhoDeg = round(Math.toDegrees(rhoRad));
        
        // Calculate angle between vertical axis (extening through torso 
        // outwards from the top) and gravity vector
        double thetaRad;
        double thetaDeg;
        thetaRad = atan2(sqrt(y*y + x*x),z);
        thetaDeg = round(Math.toDegrees(thetaRad));
        
        phiDegInt = (int) phiDeg;
        rhoDegInt = (int) rhoDeg;
        thetaDegInt = (int) thetaDeg;
        //xbuffer.insertData((int) x);
        //ybuffer.insertData((int) y);
        //zbuffer.insertData((int) z);
    }
    
    private void sendDataToListeners(){
        int[] orientationArray =  {phiDegInt, rhoDegInt, thetaDegInt};
        
        for (OrientationCalculatorListener l : listeners) {
            l.orientation(orientationArray);
        }
    }
}
