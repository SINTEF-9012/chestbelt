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
    
    private double phiDeg;
    private double rhoDeg;
    private double thetaDeg;
    
    // Short moving average filter
    private final MovingAverageFilter phiDegAvg = new MovingAverageFilter(4);
    private final MovingAverageFilter rhoDegAvg = new MovingAverageFilter(4);
    private final MovingAverageFilter thetaDegAvg = new MovingAverageFilter(4);
    
    // Long moving average filter
    private final MovingAverageFilter phiDegAvgLong = new MovingAverageFilter(33);
    private final MovingAverageFilter rhoDegAvgLong = new MovingAverageFilter(33);
    private final MovingAverageFilter thetaDegAvgLong = new MovingAverageFilter(33);
    
    private long previousTimeMillis;
    
    private int timestamp;
    
    protected ChestBelt belt;
    private ArrayList<OrientationCalculatorListener> listeners = new ArrayList<OrientationCalculatorListener>();
    
    public OrientationCalculator(ChestBelt b){
        this.belt = b;
        if (b != null) b.addChestBeltListener(this);
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
        lateralAccelerationComponent = value;
    }

    @Override
    public void accLongitudinal(int value, int timestamp) {
        longitudinalAccelerationComponent = value;
        this.timestamp = timestamp;
        calculateOrientation();
        updateAverage();
        sendDataToListeners();
    }

    @Override
    public void accVertical(int value, int timestamp) {
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
        
        // Calculate angle between lateral axis (extening outwards from right 
        // side of body) and the horizontal plane 
        double phiRad;
        phiRad = atan2(y,sqrt(x*x + z*z));
        phiDeg = Math.toDegrees(phiRad);
                      
        // Calculate angle between longitudinal axis (normal to chest, pointing outwards)
        // and horizontal plane
        double rhoRad;
        rhoRad = atan2(x,sqrt(y*y + z*z));
        rhoDeg = Math.toDegrees(rhoRad);
        
        // Calculate angle between vertical axis (extening through torso 
        // outwards from the top) and gravity vector
        double thetaRad;
        thetaRad = atan2(sqrt(y*y + x*x),z);
        thetaDeg = Math.toDegrees(thetaRad);
        
    }
    
    private void updateAverage(){
        // The short moving average
        phiDegAvg.add(phiDeg);
        rhoDegAvg.add(rhoDeg);
        thetaDegAvg.add(thetaDeg);
        
        // The long moving average
        phiDegAvgLong.add(phiDeg);
        rhoDegAvgLong.add(rhoDeg);
        thetaDegAvgLong.add(thetaDeg);
    }
            
    
    private void sendDataToListeners(){
        int[] orientationArray =  {(int) round(phiDeg), (int) round(rhoDeg), (int) round(thetaDeg)};
        int[] avgOrientationArray = new int[3];
        int[] avgOrientationLongArray = new int[3];
                
        for (OrientationCalculatorListener l : listeners) {
            l.orientation(orientationArray,timestamp);
        }
        
        // send data to logger no faster than once per second
        long currentTimeMillis = System.currentTimeMillis();
        if(currentTimeMillis-previousTimeMillis>999){
            avgOrientationArray[0] = (int) round(phiDegAvg.getAverage());
            avgOrientationArray[1] = (int) round(rhoDegAvg.getAverage());
            avgOrientationArray[2] = (int) round(thetaDegAvg.getAverage());
            avgOrientationLongArray[0] = (int) round(phiDegAvgLong.getAverage());
            avgOrientationLongArray[1] = (int) round(rhoDegAvgLong.getAverage());
            avgOrientationLongArray[2] = (int) round(thetaDegAvgLong.getAverage());
            for (OrientationCalculatorListener l : listeners) {
                l.logOrientation( avgOrientationArray, avgOrientationLongArray, timestamp );
            }
            previousTimeMillis = currentTimeMillis;
        }          
            
            
        
    }
}
