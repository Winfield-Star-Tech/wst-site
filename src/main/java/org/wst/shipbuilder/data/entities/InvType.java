package org.wst.shipbuilder.data.entities;

public class InvType {
	private Long id;
	private String name;
	private PriceDetail price = new PriceDetail();
	
	
	public PriceDetail getPrice() {
		return price;
	}
	public void setPrice(PriceDetail price) {
		this.price = price;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
