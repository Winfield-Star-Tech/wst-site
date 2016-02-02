package org.wst.shipbuilder;

import java.util.List;

public class ShipBOM {

	InvType ship;
	List<BOMEntry> materials;
	InvType blueprint;
	double buildCost = 0;
	public double getProfit() {
		return ship.getPrice().getSellPrice() - buildCost;
	}
	public double getMargin() {
		return 100.0 * (getProfit() / ship.getPrice().getSellPrice());
	}
	public InvType getBlueprint() {
		return blueprint;
	}
	public void setBlueprint(InvType blueprint) {
		this.blueprint = blueprint;
	}
	public double getBuildCost() {
		return buildCost;
	}
	public void setBuildCost(double buildCost) {
		this.buildCost = buildCost;
	}
	public ShipBOM(InvType ship, InvType blueprint, List<BOMEntry> materials) {
		super();
		this.ship = ship;
		this.materials = materials;
		this.blueprint = blueprint;
	}
	public InvType getShip() {
		return ship;
	}
	public void setShip(InvType ship) {
		this.ship = ship;
	}
	public List<BOMEntry> getMaterials() {
		return materials;
	}
	public void setMaterials(List<BOMEntry> materials) {
		this.materials = materials;
	}
	
}
