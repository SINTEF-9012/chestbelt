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
package org.thingml.chestbelt.desktop;

import org.thingml.chestbelt.driver.ChestBelt;
import org.thingml.chestbelt.driver.ChestBeltMode;
import org.thingml.chestbelt.driver.ChestBeltListener;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;

import javax.swing.JOptionPane;


public class SimpleChestBeltTest implements ChestBeltListener {

	@Override
	public void cUSerialNumber(long value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cUFWRevision(String value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void batteryStatus(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void indication(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void status(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageOverrun(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void referenceClockTime(long value, boolean seconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fullClockTimeSync(long value, boolean seconds) {
		// TODO Auto-generated method stub

	}

	@Override
	public void heartRate(int value, int timestamp) {
		System.out.println("Heart Rate = " + (value/10) + " (t=" + timestamp + ")" );

	}

	@Override
	public void heartRateConfidence(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eCGData(int value) {
		System.out.println("ECG Data = " + value );

	}

	@Override
	public void eCGSignalQuality(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void eCGRaw(int value, int timestamp) {
		System.out.println("ECG Raw = " + value + " (t=" + timestamp + ")" );

	}

	@Override
	public void gyroPitch(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gyroRoll(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void gyroYaw(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void accLateral(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void accLongitudinal(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void accVertical(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rawActivityLevel(int value, int timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void combinedIMU(int ax, int ay, int az, int gx, int gy, int gz, int timestamp) {
		System.out.println("IMU: " + ax + "\t" + ay + "\t" + az + "\t" + gx + "\t" + gy + "\t" + gz + "\t" + "(" + timestamp + ")");

	}

	@Override
	public void skinTemperature(int value, int timestamp) {
		System.out.println("Skin Temp = " + (value/10) + " (t=" + timestamp + ")" );

	}
	
	
    /* ***********************************************************************
     * Serial port utilities: listing
     *************************************************************************/
    /**
     * @return    A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
    public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
                case CommPortIdentifier.PORT_SERIAL:
                    try {
                        CommPort thePort = com.open("CommUtil", 50);
                        thePort.close();
                        h.add(com);
                    } catch (PortInUseException e) {
                        System.out.println("Port, " + com.getName() + ", is in use.");
                    } catch (Exception e) {
                        System.err.println("Failed to open port " + com.getName());
                        e.printStackTrace();
                    }
            }
        }
        return h;
    }

    public static void registerPort(String port) {
        
    	String prop = System.getProperty("gnu.io.rxtx.SerialPorts");
        if (prop == null) {
            prop = "";
        }
        if (!prop.contains(port)) {
            prop += port + File.pathSeparator;
            System.setProperty("gnu.io.rxtx.SerialPorts", prop);
        }
        System.out.println("gnu.io.rxtx.SerialPorts = " + prop);

        prop = System.getProperty("javax.comm.rxtx.SerialPorts");
        if (prop == null) {
            prop = "";
        }
        if (!prop.contains(port)) {
            prop += port + File.pathSeparator;
            System.setProperty("javax.comm.rxtx.SerialPorts", prop);
        }
        System.out.println("javax.comm.rxtx.SerialPorts = " + prop);
    }

    public static String selectSerialPort() {

        ArrayList<String> possibilities = new ArrayList<String>();
        //possibilities.add("Emulator");
        for (CommPortIdentifier commportidentifier : getAvailableSerialPorts()) {
            possibilities.add(commportidentifier.getName());
        }

        int startPosition = 0;
        if (possibilities.size() > 1) {
            startPosition = 1;
        }
        
       return (String) JOptionPane.showInputDialog(
               null,
               "ChestBelt",
               "Select serial port",
               JOptionPane.PLAIN_MESSAGE,
               null,
               possibilities.toArray(),
               possibilities.toArray()[startPosition]);
        
    }
    
    public static ChestBelt connectChestBelt() {
    	try {
    		
    		String portName = selectSerialPort();
    		
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            
            if (portIdentifier.isCurrentlyOwned()) {
                System.err.println("Error: Port " + portName + " is currently in use");
            } 
            else {
                CommPort commPort = portIdentifier.open("ChestBelt", 2000);
                
                System.out.println("port = " + commPort);

                if (commPort instanceof SerialPort) {
                    SerialPort serialPort = (SerialPort) commPort;
                    serialPort.setSerialPortParams(115200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                    
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);
                    serialPort.setRTS(true);
                    

                    System.out.println("serial port = " + serialPort);
                    
                    return new ChestBelt(serialPort.getInputStream(), serialPort.getOutputStream());

                } else {
                    System.err.println("Error: Port " + portName + " is not a valid serial port.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return null;
    }
    

    /* ***********************************************************************
     * Main
     *************************************************************************/
    public static void main(String[] args) {
    	ChestBelt e = connectChestBelt();
    	e.addChestBeltListener(new SimpleChestBeltTest());
    	if(e != null) {
    		//e.setBTUpdateInterval(1);
    		//e.setHRAverageInterval(1);
    		//e.setHRUpdateInterval(1);
    		//e.setTempAverageInterval(1);
    		e.setDataMode(ChestBeltMode.RawGyroMode);
    		e.connectionRestored();
    	}
    	
    }

static {
        
        try {
            String osName = System.getProperty("os.name");
            String osProc = System.getProperty("os.arch");

		//System.out.println(System.properties['java.library.path']);

		System.out.println("Load RxTx for os.name=" + osName  + " os.arch=" + osProc);

            if (osName.equals("Mac OS X")) {
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Mac_OS_X/librxtxSerial.jnilib"), "librxtxSerial.jnilib");
            }
            if (osName.equals("Win32")) {
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Windows/win32/rxtxSerial.dll"), "rxtxSerial.dll");
            }
            if (osName.equals("Win64") || osName.equals("Windows 7")) {
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Windows/win64/rxtxSerial.dll"), "rxtxSerial.dll");
            }
            if (osName.equals("Linux") && osProc.equals("x86-64")) {
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/x86_64-unknown-linux-gnu/librxtxSerial.so"), "librxtxSerial.so");
            }
	     if (osName.equals("Linux") && osProc.equals("amd64")) {
		
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/x86_64-unknown-linux-gnu/librxtxSerial.so"), "librxtxSerial.so");
		System.out.println("Copied for amd64");
            }
            if (osName.equals("Linux") && osProc.equals("ia64")) {
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/ia64-unknown-linux-gnu/librxtxSerial.so"), "librxtxSerial.so");
            }
            if (osName.equals("Linux") && osProc.equals("x86")) {
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/i686-unknown-linux-gnu/librxtxParallel.so"), "librxtxParallel.so");
                NativeLibUtil.copyFile(NativeLibUtil.class.getClassLoader().getResourceAsStream("nativelib/Linux/i686-unknown-linux-gnu/librxtxSerial.so"), "librxtxSerial.so");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
