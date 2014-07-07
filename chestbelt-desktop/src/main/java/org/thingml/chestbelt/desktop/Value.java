package org.thingml.chestbelt.desktop;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

//@XmlRootElement(name = "Value")
// If you want you can define the order in which the fields are written
// Optional
//@XmlType(propOrder = { "TimeStamp", "SensorId", "ComputerId" })
@XmlAccessorType(XmlAccessType.NONE) // Removes duplicate outputstreams
public class Value {

public String Name;
public String Val;

// If you like the variable name, e.g. "name", you can easily change this
// name for your XML-Output:
//@XmlElement(name = "title")
    @XmlElement(name ="Name")
    public String getName() {
    return Name;
    }

    public void setName(String Name) {
    this.Name = Name;
    }
    
    public String getVal() {
    return Val;
    }
    @XmlElement(name ="Val")
    public void setVal(String Val) {
    this.Val = Val;
    }



} 


/*
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

/*public class Value {

String Name;
String Val;

Value(String Val, String Name) {
this.Val = Val;
this.Name = Name;
}

public String getVal() {
return Val;
}

@XmlElement
public void setVal(String Val) {
this.Val = Val;
}

public String getName() {
return Name;
}

@XmlElement
public void setName(String Name) {
this.Name = Name;
}
}*/



