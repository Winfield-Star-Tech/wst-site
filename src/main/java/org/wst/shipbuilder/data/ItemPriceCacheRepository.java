package org.wst.shipbuilder.data;

import org.springframework.data.repository.CrudRepository;
import org.wst.shipbuilder.data.entities.ItemPriceCacheEntry;

public interface ItemPriceCacheRepository  extends CrudRepository<ItemPriceCacheEntry, Long> {
	public ItemPriceCacheEntry findByCacheId(Long id);
	public ItemPriceCacheEntry findByTypeId(Long typeId);
}
