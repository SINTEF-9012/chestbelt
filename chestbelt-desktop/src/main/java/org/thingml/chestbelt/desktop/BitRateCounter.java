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

import org.thingml.chestbelt.driver.ChestBelt;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author franck
 */
public class BitRateCounter extends Thread {
    
    private int update_rate = 100; // 1000 ms
    
    protected ChestBelt belt;
    protected int bitrate = 0;
    
    private ArrayList<BitRateListemer> listeners = new ArrayList<BitRateListemer>();

    public void addChestBeltListener(BitRateListemer l) {
        listeners.add(l);
    }

    public void removeChestBeltListener(BitRateListemer l) {
        listeners.remove(l);
    }
    
    public BitRateCounter(ChestBelt b) {
        this.belt = b;
    }
    
    public void run() {
        
       long old_time = System.currentTimeMillis();
       long old_bytes = belt.getReceivedBytes();
       
       while (belt.isConnected()) {
            try {
                Thread.sleep(update_rate);
            } catch (InterruptedException ex) {
                Logger.getLogger(BitRateCounter.class.getName()).log(Level.SEVERE, null, ex);
            }
            long new_time = System.currentTimeMillis();
            long new_bytes = belt.getReceivedBytes();
            
            bitrate = (int)(((new_bytes - old_bytes) * 1000) / (new_time - old_time));

            //bitrate = (bitrate * ((average_rate / update_rate) - 1) + new_bitrate) / (average_rate / update_rate);

            old_time = new_time;
            old_bytes = new_bytes;
            
            for (BitRateListemer l : listeners) {
                l.bitRate(bitrate);
            }
       }
    }
}
