package com.smg.carmarketplace.infrastructure.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;

import com.smg.carmarketplace.domain.model.CarForMarketplace;

@Repository
public class CarMarketplaceRepository {
	
	@Autowired
	private ElasticsearchOperations operations;
	
	
	public void save(CarForMarketplace car) {
		operations.save(car);
	}

	
	public void update(CarForMarketplace car) {
		var carToUpdate = operations.get(car.getId().toString(), CarForMarketplace.class);
		
		carToUpdate.setManufacturer(car.getManufacturer());
		carToUpdate.setModel(car.getModel());
		carToUpdate.setBodyType(car.getBodyType());
		carToUpdate.setFuelType(car.getFuelType());
		carToUpdate.setMileage(car.getMileage());
		carToUpdate.setYear(car.getYear());
		carToUpdate.setPower(car.getPower());
		carToUpdate.setPrice(car.getPrice());
		
		operations.save(carToUpdate);
	}
	
	public void delete(Integer carId) {
		operations.delete(carId.toString(), CarForMarketplace.class);
	}
	
	public SearchPage<CarForMarketplace> searchBy(Criteria criteria, Pageable pageable){
		Query query = new CriteriaQuery(criteria);
		query.setPageable(pageable);
		return SearchHitSupport.searchPageFor(operations.search(query, CarForMarketplace.class), pageable);
	}
}
