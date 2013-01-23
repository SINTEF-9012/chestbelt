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
    protected GraphBuffer brmsa = new GraphBuffer(500);
    protected GraphBuffer brmsb = new GraphBuffer(500);
    
    protected ChestBelt belt;
    
    protected GraphBuffer bemgpc = new GraphBuffer(1000);
    protected GraphBuffer brmspc = new GraphBuffer(500);
    protected int rms_window = 200;
    protected int rms_period = 100;
    protected int rms_count = 0;
    
    public void computeRMS() {
        long rms = 0;
        int[] data = bemg.getGraphData();
        if (data[data.length-1] <0 || data[data.length-1] > 4096 ) return; // There is not enough data in the buffer
        int value;
        for (int i = 0; i<rms_window; i++) {
            value = data[data.length - 1 - i] - 2048;
            rms += value * value;
        }
        rms /= rms_window;
        int result = (int)Math.sqrt(rms);
        brmspc.insertData(result);
        bemgpc.insertData(data[data.length - 1 - rms_window/3]);
        bemgpc.insertData(data[data.length - 1 - 2*rms_window/3]);
    }
    
    
    /** Creates new form ECGGraphForm */
    public EMGGraphForm(ChestBelt b) {
        this.belt = b;
        if (b != null) b.addChestBeltListener(this);
        initComponents();
        ((GraphPanel)jPanel1).start();
        ((GraphPanel)jPanel2).start();
        ((GraphPanel)jPanel3).start();
        ((GraphPanel)jPanel4).start();
        ((GraphPanel)jPanel7).start();
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
        jPanel1 = new LineGraphPanel(brmspc, "RMS EMG (PC)", 0, 512, 128, new java.awt.Color(0, 204, 51));
        jPanel7 = new LineGraphPanel(bemgpc, "Raw EMG (PC RMS Sync)", 1024, 3072, 256, new java.awt.Color(0, 204, 51));
        jPanel4 = new LineGraphPanel(bemg, "Raw EMG Value", 0, 4096, 1024, new java.awt.Color(0, 204, 51));
        jPanel2 = new LineGraphPanel(brmsa, "EMG RMS Value (Belt Channel A)", 0, 2048, 512, new java.awt.Color(0, 204, 51));
        jPanel3 = new LineGraphPanel(brmsb, "EMG RMS Value (Belt Channel B)", 0, 2048, 512, new java.awt.Color(0, 204, 51));
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ChestBelt Heart Rate and ECG Graphs");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                EMGGraphForm.this.windowClosed(evt);
            }
        });

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.PAGE_AXIS));
        jPanel5.add(jPanel1);
        jPanel5.add(jPanel7);
        jPanel5.add(jPanel4);
        jPanel5.add(jPanel2);
        jPanel5.add(jPanel3);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);

        jLabel1.setText("RMS window:");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10", "25", "50", "75", "100", "150", "200", "250", "500", "1000" }));
        jComboBox1.setSelectedIndex(6);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel2.setText("RMS period:");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "10", "25", "50", "75", "100", "150", "200", "250", "500", "1000" }));
        jComboBox2.setSelectedIndex(4);
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel3.setText("RMS max:");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "128", "256", "512", "1024", "2048" }));
        jComboBox3.setSelectedIndex(2);
        jComboBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(145, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
    ((GraphPanel)jPanel7).stop();
}//GEN-LAST:event_windowClosed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        try {
            rms_window = Integer.parseInt(jComboBox1.getSelectedItem().toString());
            rms_count = 0;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        try {
            rms_period = Integer.parseInt(jComboBox2.getSelectedItem().toString());
            rms_count = 0;
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox3ActionPerformed
        try {
            int max = Integer.parseInt(jComboBox3.getSelectedItem().toString());
            ((GraphPanel)jPanel1).setYmax(max);
            ((GraphPanel)jPanel1).setYminor(max/4);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_jComboBox3ActionPerformed

    
    
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
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    // End of variables declaration//GEN-END:variables

    @Override
    public void connectionLost() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void eMGData(int value) {
        if (rms_count >= rms_period) {
            computeRMS();
            rms_count = 0;
        } else { rms_count++; }
        bemg.insertData(value);
    }

    @Override
    public void eMGSignalQuality(int value, int timestamp) {
        
    }

    @Override
    public void eMGRaw(int value, int timestamp) {
        if (rms_count >= rms_period) {
            computeRMS();
            rms_count = 0;
        } else { rms_count++; }
        bemg.insertData(value);
    }

    @Override
    public void eMGRMS(int channelA, int channelB, int timestamp) {
        brmsa.insertData(channelA);
        brmsb.insertData(channelB);
    }
}
