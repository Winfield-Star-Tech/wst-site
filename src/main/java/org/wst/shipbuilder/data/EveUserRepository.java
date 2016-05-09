package org.wst.shipbuilder.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EveUserRepository extends PagingAndSortingRepository<EveUser, Long>{
	

}
