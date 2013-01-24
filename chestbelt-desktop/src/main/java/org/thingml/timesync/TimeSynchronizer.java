package org.thingml.timesync;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeSynchronizer implements Runnable {
    
    protected TimeSynchronizable device;
    
     // Parameters of the time syncronization
    protected int period = 250;             // Send a time request every 250 ms
    protected int max_timestamp = 0x0FFF;   // 12 bits timestamp wrap-around
    protected int phase_frame = 1024; 
    protected int max_seq_number = 3; // 2 bits
    
    // Data for the time synchronization
    protected long offset = 0;
    
    public TimeSynchronizer(TimeSynchronizable device) {
        this.device = device;
    }
    
    /**************************************************************************
     *  Handling of the pong and calculation of the magic offset
     * ************************************************************************/
    
    public void receive_TimeResponse(int seq_num, int timestamp) {
        // TODO: We got a pong. The smart part of the code goes here.
    }
   
    /**************************************************************************
     *  Translation of device timestanp into synchronized timestamp
     * ************************************************************************/
    
    public long getSynchronizedEpochTime(int timestamp) {
        // TODO: Smart part of the code goes here.
        return offset + timestamp;
    }
    
    /**************************************************************************
     *  Sending of the time requests at reqular interval
     * ************************************************************************/
    protected boolean stop_request = false;
    protected boolean running = false;
    protected int sequence_number = 0;
    
    public boolean isRunning() {
        return running;
    }
    
    protected int next_seq_number() {
        sequence_number++;
        if (sequence_number>max_seq_number)sequence_number = 0;
        return sequence_number;
    }
    
    public void start() {
        if (!running) {
           stop_request = false;
           new Thread(this).start(); 
        }
    }
    
    public void stop() {
        if (running) {
           stop_request = true;
        }
    }

    @Override
    public void run() {
        running = true;
        try {
            do {
            // initiate a ping every "period" milliseconds
            device.sendTimeRequest(next_seq_number());
            Thread.sleep(period);
            } while (!stop_request);
        } catch (InterruptedException ex) {
            Logger.getLogger(TimeSynchronizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            running = false;
        }
    }
}
