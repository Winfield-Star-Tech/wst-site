package org.wst.shipbuilder.data.fittings;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ShipFitting {

	private FittingShipType shipType;
	@XmlAttribute
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FittingShipType getShipType() {
		return shipType;
	}

	public void setShipType(FittingShipType shipType) {
		this.shipType = shipType;
	}

	@XmlElement(name="hardware")
	private List<ShipHardware> hardwareSlots;


	public List<ShipHardware> getHardware() {
		return hardwareSlots;
	}

	public void setHardware(List<ShipHardware> hardware) {
		this.hardwareSlots = hardware;
	}
}
