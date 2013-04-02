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

/*
 * ECGGraphForm.java
 *
 * Created on 1 juil. 2012, 16:48:35
 */
package org.thingml.chestbelt.desktop;

import org.thingml.chestbelt.driver.ChestBelt;
import org.thingml.chestbelt.driver.ChestBeltListener;
import java.awt.Color;
import org.thingml.rtcharts.swing.*;

/**
 *
 * @author franck
 */
public class EMGGraphForm extends javax.swing.JFrame implements ChestBeltListener {

    
    protected GraphBuffer bemg = new GraphBuffer(1500);
    protected GraphBuffer bemgrate = new GraphBuffer(500);
    protected GraphBuffer brmsa = new GraphBuffer(500);
    protected GraphBuffer brmsb = new GraphBuffer(500);
     
    protected ChestBelt belt;
    
    /** Creates new form ECGGraphForm */
    public EMGGraphForm(ChestBelt b) {
        this.belt = b;
        if (b != null) b.addChestBeltListener(this);
        initComponents();
        ((GraphPanel)jPanel1).start();
        ((GraphPanel)jPanel2).start();
        ((GraphPanel)jPanel3).start();
        ((GraphPanel)jPanel4).start();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new LineGraphPanel(bemg, "Raw EMG Value", 0, 4096, 1024, new java.awt.Color(0, 204, 51));
        jPanel2 = new LineGraphPanel(brmsa, "EMG RMS Value (Belt Channel A)", 0, 4096, 512, new java.awt.Color(0, 204, 51));
        jPanel3 = new LineGraphPanel(brmsb, "EMG RMS Value (Belt Channel B)", 0, 4096, 512, new java.awt.Color(0, 204, 51));
        jPanel1 = new BarGraphPanel(bemgrate, "Raw ECG Rate (# val between timestamps)", 0, 200, 50, new java.awt.Color(0, 204, 51));
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jTextFieldTS = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ChestBelt EMG Graphs");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                EMGGraphForm.this.windowClosed(evt);
            }
        });

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.PAGE_AXIS));
        jPanel5.add(jPanel4);
        jPanel5.add(jPanel2);
        jPanel5.add(jPanel3);
        jPanel5.add(jPanel1);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        jLabel4.setText("Timestamp:");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(455, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextFieldTS, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextFieldTS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );

        getContentPane().add(jPanel6, java.awt.BorderLayout.NORTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void windowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosed
    if (belt != null) belt.removeChestBeltListener(this);
    ((GraphPanel)jPanel1).stop();
    ((GraphPanel)jPanel2).stop();
    ((GraphPanel)jPanel3).stop();
    ((GraphPanel)jPanel4).stop();
}//GEN-LAST:event_windowClosed

    
    
    @Override
    public void cUSerialNumber(long value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cUFWRevision(String value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void batteryStatus(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void indication(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void status(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void messageOverrun(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void referenceClockTime(long value, boolean seconds) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void fullClockTimeSync(long value, boolean seconds) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void heartRate(int value, int timestamp) {
        
    }

    @Override
    public void heartRateConfidence(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eCGData(int value) {
        
    }

    @Override
    public void eCGSignalQuality(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {

    }

    @Override
    public void skinTemperature(int value, int timestamp) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextField jTextFieldTS;
    // End of variables declaration//GEN-END:variables

    @Override
    public void connectionLost() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    protected int emg_count = 0;
    
    @Override
    public void eMGData(int value) {
        bemg.insertData(value);
        emg_count++;
        //System.out.print(".");
    }

    @Override
    public void eMGSignalQuality(int value, int timestamp) {
        
    }

    @Override
    public void eMGRaw(int value, int timestamp) {
        bemg.insertData(value);
        emg_count++;
        jTextFieldTS.setText(""+timestamp);
        bemgrate.insertData(emg_count);
        emg_count = 0;
        //System.out.println("X");
    }

    @Override
    public void eMGRMS(int channelA, int channelB, int timestamp) {
        brmsa.insertData(channelA);
        brmsb.insertData(channelB);
    }
    
    @Override
    public void referenceClockTimeSync(int timeSyncSeqNum, long value) {
        
    }
    
}
