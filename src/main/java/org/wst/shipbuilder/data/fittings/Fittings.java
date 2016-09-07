package org.wst.shipbuilder.data.fittings;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType (XmlAccessType.FIELD)
@XmlRootElement(name="fittings")
public class Fittings {
	@XmlElement(name="fitting")
	private List<ShipFitting> fittings;

	public List<ShipFitting> getFittings() {
		return fittings;
	}

	public void setFittings(List<ShipFitting> fittings) {
		this.fittings = fittings;
	}
	

}
