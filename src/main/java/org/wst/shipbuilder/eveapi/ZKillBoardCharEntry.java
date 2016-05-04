package org.wst.shipbuilder.eveapi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ZKillBoardCharEntry {

	private Long characterID;
	private String name;
	private int shipsLost;
	private int shipsDestroyed;
	private long iskLost;
	private int pointsLost;
	private long iskDestroyed;
	private int pointsDestroyed;
	
	private Map<String, ZKBMonthStats> months = new HashMap<String, ZKBMonthStats>();
	

	public Map<String, ZKBMonthStats> getMonths() {
		return months;
	}
	public void setMonths(Map<String, ZKBMonthStats> months) {
		this.months = months;
	}
	public Long getCharacterID() {
		return characterID;
	}
	public void setCharacterID(Long characterID) {
		this.characterID = characterID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getShipsLost() {
		return shipsLost;
	}
	public void setShipsLost(int shipsLost) {
		this.shipsLost = shipsLost;
	}
	public int getShipsDestroyed() {
		return shipsDestroyed;
	}
	public void setShipsDestroyed(int shipsDestroyed) {
		this.shipsDestroyed = shipsDestroyed;
	}
	public long getIskLost() {
		return iskLost;
	}
	public void setIskLost(long iskLost) {
		this.iskLost = iskLost;
	}
	public int getPointsLost() {
		return pointsLost;
	}
	public void setPointsLost(int pointsLost) {
		this.pointsLost = pointsLost;
	}
	public long getIskDestroyed() {
		return iskDestroyed;
	}
	public void setIskDestroyed(long iskDestroyed) {
		this.iskDestroyed = iskDestroyed;
	}
	public int getPointsDestroyed() {
		return pointsDestroyed;
	}
	public void setPointsDestroyed(int pointsDestroyed) {
		this.pointsDestroyed = pointsDestroyed;
	}
	
	
}
