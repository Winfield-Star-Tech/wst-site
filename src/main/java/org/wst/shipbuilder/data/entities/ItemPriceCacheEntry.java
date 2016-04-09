package org.wst.shipbuilder.data.entities;

import java.io.Serializable;

 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ItemPriceCacheEntry implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long cacheId;
	
	@Column(nullable = false)
	private Long typeId;
	
	@Column(nullable = false)
	private Double sellPrice;
	
	@Column(nullable = false)
	private Double buyPrice;
	
	protected ItemPriceCacheEntry() {}
	
	public ItemPriceCacheEntry(Long typeID, Double sellPrice, Double buyPrice) {
		this.typeId = typeID;
		this.sellPrice = sellPrice;
		this.buyPrice = buyPrice;
	}

	public Long getCacheId() {
		return cacheId;
	}

	public void setCacheId(Long cacheId) {
		this.cacheId = cacheId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public Double getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Double sellPrice) {
		this.sellPrice = sellPrice;
	}

	public Double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(Double buyPrice) {
		this.buyPrice = buyPrice;
	}
	
}
