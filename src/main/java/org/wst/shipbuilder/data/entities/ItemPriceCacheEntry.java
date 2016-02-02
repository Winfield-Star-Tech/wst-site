package org.wst.shipbuilder.data.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ItemPriceCacheEntry implements Serializable{

	@Id
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
}
