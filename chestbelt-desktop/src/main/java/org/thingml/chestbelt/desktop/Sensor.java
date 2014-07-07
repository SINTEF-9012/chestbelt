package org.thingml.chestbelt.desktop;


import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;



//This statement means that class "Sensor.java" is the root-element of our example

@XmlRootElement(name = "Sensor")
@XmlType(propOrder = { "timeStamp", "sensorId", "computerId", "ValueList", "date", "companyCode", "companyName","unitCode","unitSerial","unitName","alarmCode","alarmType" })
public class Sensor {
    
  // XmLElementWrapper generates a wrapper element around XML representation
   
   @XmlElementWrapper(name = "Values")
  
  
  // XmlElement sets the name of the entities
  @XmlElement(name = "Value")
  
  
    private ArrayList<Value> ValueList;
    private String SensorId;
    private String ComputerId;
    private String TimeStamp;
    private String Date;
    private String CompanyCode;
    private String CompanyName;
    private String UnitCode;
    private String UnitSerial;
    private String UnitName;
    private String AlarmCode;
    private String AlarmType;
 
  


    @XmlElement(name = "TimeStamp")
      public String getTimeStamp() {
    return TimeStamp;
    }


    public void setTimeStamp(String TimeStamp) {
    this.TimeStamp = TimeStamp;
    }


    @XmlElement(name = "ComputerId")
    public String getComputerId() {
    return ComputerId;
    }


    public void setComputerId(String ComputerId) {
    this.ComputerId = ComputerId;
    }
    @XmlElement(name = "SensorId")
    public String getSensorId() {
    return SensorId;
    }

    public void setSensorId(String SensorId) {
    this.SensorId = SensorId;


}
    /* public ArrayList<Value> getValueList() {
    return this.ValueList;
    }*/

    public void setValueList(ArrayList<Value> ValueList) {
      this.ValueList = ValueList;
    }

    @XmlElement(name = "Date")
    public String getDate() {
    return Date;
    }

    public void setDate(String Date) {
    this.Date = Date;
    }

    @XmlElement(name = "CompanyCode")
    public String getCompanyCode() {
    return CompanyCode;
    }
    
    public void setCompanyCode(String CompanyCode) {
    this.CompanyCode = CompanyCode;
    }
    
    @XmlElement(name = "CompanyName")
    public String getCompanyName() {
    return CompanyName;
    }
    
    public void setCompanyName(String CompanyName) {
    this.CompanyName = CompanyName;
    }
    
    @XmlElement(name = "UnitCode")
    public String getUnitCode() {
    return UnitCode;
    }
    
    public void setUnitCode(String UnitCode) {
    this.UnitCode = UnitCode;
    }
    
    @XmlElement(name = "UnitSerial")
    public String getUnitSerial() {
    return UnitSerial;
    }
    
    public void setUnitSerial(String UnitSerial) {
    this.UnitSerial = UnitSerial;
    }
    
    @XmlElement(name = "UnitName")
    public String getUnitName() {
    return UnitName;
    }
    
    public void setUnitName(String UnitName) {
    this.UnitName = UnitName;
    }
    
    @XmlElement(name = "AlarmCode")
    public String getAlarmCode() {
    return AlarmCode;
    }
    
    public void setAlarmCode(String AlarmCode) {
    this.AlarmCode = AlarmCode;
    }
    
    @XmlElement(name = "AlarmType")
    public String getAlarmType() {
    return AlarmType;
    }
    
    public void setAlarmType(String AlarmType) {
    this.AlarmType = AlarmType;
    }
    
    
} 






/*/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/

/*package org.thingml.chestbelt.desktop;

import java.util.Map;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;*/

/**
 *
 * @author stians
 */

/*@XmlRootElement
public class Sensor {

//String TimeStamp;
String SensorId;
String ComputerId;
String Name;
Value Values = new Value(" ", " ");
//       String Value;
String Val;
String TimeStamp;
//int id;




//-------------------------------------

public String getTimeStamp() {
return TimeStamp;
}


@XmlElement
public void setTimeStamp(String TimeStamp) {
this.TimeStamp = TimeStamp;
}



public String getComputerId() {
return ComputerId;
}


@XmlElement
public void setComputerId(String ComputerId) {
this.ComputerId = ComputerId;
}



public String getSensorId() {
return SensorId;
}

@XmlElement
public void setSensorId(String SensorId) {
this.SensorId = SensorId;


}


public String getValues() {
return Values.getVal();
}

@XmlElement
public void setValues(String Values) {
this.Values.setVal(Values);


}
*/
    
    
    
    
    /*  @XmlType
    public class Values{
    
    String Value;
    
    
    
    public String getValue() {
    return Value;
    }
    
    @XmlElement
    public void setValue(String Value) {
    this.Value = Value;
    
    
    }
    
    } 
    
    public String getName() {
        return Name;
    }
    
    @XmlElement
    public void setName(String Name) {
        this.Name = Name;
    }
    
    /*  public int getId() {
    return id;
    }
    
    @XmlAttribute
    public void setId(int id) {
    this.id = id;
    }*/
    
    
//}

