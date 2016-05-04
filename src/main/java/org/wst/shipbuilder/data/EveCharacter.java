package org.wst.shipbuilder.data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EveCharacter {

	public long getCharacterID() {
		return characterID;
	}

	public void setCharacterID(long characterID) {
		this.characterID = characterID;
	}

	public long getCorporationID() {
		return corporationID;
	}

	public void setCorporationID(long corporationID) {
		this.corporationID = corporationID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Id
	private long characterID;
	private long corporationID;
	private String name;
	private long shipsDestroyed;
	private long shipsLost;
	
	
	
	
	public long getShipsDestroyed() {
		return shipsDestroyed;
	}

	public void setShipsDestroyed(long shipsDestroyed) {
		this.shipsDestroyed = shipsDestroyed;
	}

	public long getShipsLost() {
		return shipsLost;
	}

	public void setShipsLost(long shipsLost) {
		this.shipsLost = shipsLost;
	}

	protected EveCharacter(){}
	
	public EveCharacter(long id, String name) {
		characterID = id;
		corporationID = 98140131;
		this.name = name;
	}
	
}
