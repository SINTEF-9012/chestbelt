/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.thingml.timesync;

import java.awt.Color;
import org.thingml.rtcharts.swing.*;

/**
 *
 * @author ffl
 */
public class TimeSyncErrorFrame extends javax.swing.JFrame implements ITimeSynchronizerLogger {

    protected DataBuffer bxyerr = new DataBuffer(2, 100);
    
    protected GraphBuffer berr = new GraphBuffer(100);
    protected GraphBuffer bdelay = new GraphBuffer(100);
    
    protected GraphBuffer bdrop = new GraphBuffer(100);
   
    protected TimeSynchronizer ts = null;
    
    /**
     * Creates new form TimeSynchronizerFrame
     */
    public TimeSyncErrorFrame(TimeSynchronizer ts) {
        initComponents();
        this.ts = ts;
        ts.addLogger(this);
        ((XYGraphPanel)jPanel2).start();
        ((GraphPanel)jPanel3).start();
        ((GraphPanel)jPanel4).start();
        ((GraphPanel)jPanel5).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new XYGraphPanel(bxyerr, "Error vs Delay (ms)", 0, 150, 25, -50, 50, 10, Color.red);
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new BarGraphPanel(bdelay, "Calculated delay (ms)", 0, 200, 50, Color.red);
        jPanel4 = new BarGraphPanel(berr, "Error", -40, 40, 20, Color.red);
        jPanel5 = new BarGraphPanel(bdrop, "Packets droped by Dts Filter", 0, 500, 150, Color.red);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        jPanel1.setLayout(new java.awt.GridLayout(2, 0));
        jPanel1.add(jPanel2);

        jPanel6.setLayout(new javax.swing.BoxLayout(jPanel6, javax.swing.BoxLayout.PAGE_AXIS));
        jPanel6.add(jPanel3);
        jPanel6.add(jPanel4);
        jPanel6.add(jPanel5);

        jPanel1.add(jPanel6);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
          if (ts != null) {
              ts.removeLogger(this);
          }
          ((XYGraphPanel)jPanel2).stop();
          ((GraphPanel)jPanel3).stop();
          ((GraphPanel)jPanel4).stop();
          ((GraphPanel)jPanel5).stop();
    }//GEN-LAST:event_formWindowClosed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    // End of variables declaration//GEN-END:variables

    @Override
    public void timeSyncStart() {
        
    }

    @Override
    public void timeSyncStop() {
        
    }

    @Override
    public void timeSyncLog(String time, long ts, long tmt, long tmr, long delay, long offs, long errorSum, long zeroOffset, long regOffsMs, int skipped) {
        int del = (int) delay;
        int err = (int) (offs - regOffsMs);
        bxyerr.appendDataRow(new int[] {del, err});
        berr.insertData(err);
        bdelay.insertData(del);
        bdrop.insertData(0);
    }

    @Override
    public void timeSyncPong(int delay, int dtt, int dtr, int dts) {
        
    }

    @Override
    public void timeSyncReady() {
    }

    @Override
    public void timeSyncWrongSequence(int pingSeqNum, int pongSeqNum) {
    }

    @Override
    public void timeSyncDtsFilter(int dts) {
        bdrop.insertData(dts);
    }

    @Override
    public void timeSyncErrorFilter(int error) {
    }
}
