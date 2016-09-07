package org.wst.shipbuilder.data.fittings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="hardware")
@XmlAccessorType(XmlAccessType.FIELD)
public class ShipHardware {
	@XmlAttribute(name="qty")
	private int quantity;
	@XmlAttribute
	private String slot;
	@XmlAttribute(name="type")
	private String hardwareType;
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getSlot() {
		return slot;
	}
	public void setSlot(String slot) {
		this.slot = slot;
	}
	public String getHardwareType() {
		return hardwareType;
	}
	public void setHardwareType(String hardwareType) {
		this.hardwareType = hardwareType;
	}

}
