package org.thingml.chestbelt.desktop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import org.thingml.chestbelt.driver.ChestBelt;

/**
 *
 * @author stians
 */

 
public class SensorXmlClass {
    
 
    
     public String heartrate;
     public String temperature;
     public String activity;
     public String battery;
     public String posture;
     public String chronos;
     public String computer;
     //public String skinTemperature;
     
   
    SensorXmlClass(String heartrate, String temperature, String activity, String battery, String posture, String chronos) {
       this.heartrate = heartrate;
       this.temperature = temperature;
       this.activity = activity;
       this.battery = battery;
       this.posture = posture;
       this.chronos = chronos;
       //this.computer = computer;
       
    }
    
   
    public String getXml(){
    
    String ret = "";
    
        
    ArrayList<Value> ValueList = new ArrayList<Value>();
    // create Values
    Value Value1 = new Value();
    Value1.setVal(heartrate);
    Value1.setName("HR");
    ValueList.add(Value1);

    Value Value2 = new Value();
    Value2.setVal(temperature);
    Value2.setName("Temperature");
    ValueList.add(Value2);
    
    Value Value3 = new Value();
    Value3.setVal(posture);
    Value3.setName("Posture");
    ValueList.add(Value3);
    
    Value Value4 = new Value();
    Value4.setVal(activity);
    Value4.setName("Activity");
    ValueList.add(Value4);
    
    Value Value5 = new Value();
    Value5.setVal(battery);
    Value5.setName("BatteryStatus");
    ValueList.add(Value5);
    
    Value Value6 = new Value();
    Value6.setVal("0");
    Value6.setName("Sensor Disconnect");
    ValueList.add(Value6);
    
    Value Value7 = new Value();
    Value7.setVal("0");
    Value7.setName("Sensor Alarm");
    ValueList.add(Value7);
    
  
    

    // create Sensorstore, assigning elements in sensor
    
    Sensor Sensorstore = new Sensor();
    Sensorstore.setSensorId("ChestBelt");
    Sensorstore.setComputerId(computer);//"Chestbelt 13606"
    Sensorstore.setTimeStamp(chronos);//"18.06.2014 16:43:49,61"
    Sensorstore.setDate("2014-02-10T12:09:32");
    Sensorstore.setCompanyCode("42");
    Sensorstore.setCompanyName("SINTEF");
    Sensorstore.setUnitCode("Chestbelt 13606");
    Sensorstore.setUnitSerial("1326000180");
    Sensorstore.setUnitName("1326000180");
    Sensorstore.setAlarmCode("10");
    Sensorstore.setAlarmType("ChangedToNormal");
    /*    Sensorstore.setAlarmType("TagSerial/");
    Sensorstore.setAlarmType("TagName");
    Sensorstore.setAlarmType("ChangedToNormal");*/
   
    Sensorstore.setValueList(ValueList);
         
    
    
    // create JAXB context and instantiate marshaller
    JAXBContext context;
    Marshaller m;
    ByteArrayOutputStream os = new ByteArrayOutputStream();
         try {
             context = JAXBContext.newInstance(Sensor.class);
             m = context.createMarshaller();
             m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
             m.marshal(Sensorstore, os);
             ret = os.toString();  /* add codepage ??? */
             int pos = ret.indexOf("<Sensor>");
             if (pos != -1) 
                 ret = ret.substring(pos);
        } catch (JAXBException ex) {
             Logger.getLogger(SensorXmlClass.class.getName()).log(Level.SEVERE, null, ex);
             ret = "Error JAXBException";
             
             return ret;
         }
    
    
    // Write to System.out
    
    return ret;
    
    
    }
}
