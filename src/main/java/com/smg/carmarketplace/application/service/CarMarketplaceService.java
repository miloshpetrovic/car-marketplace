package com.smg.carmarketplace.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.smg.carmarketplace.domain.model.Car;
import com.smg.carmarketplace.domain.model.CarForMarketplace;
import com.smg.carmarketplace.infrastructure.repository.CarMarketplaceRepository;

@Retryable
@Service
public class CarMarketplaceService {
	
	@Autowired
	private CarMarketplaceRepository carMarketplaceRepository;
	
	@CacheEvict(value = "cars", allEntries = true)
	public void save(Car car) {
		carMarketplaceRepository.save(car.forMarketplace());
	}
	
	@CacheEvict(value = "cars", allEntries = true)
	public void update(Car car) {
		carMarketplaceRepository.update(car.forMarketplace());
	}
	
	@CacheEvict(value = "cars", allEntries = true)
	public void delete(Car car) {
		carMarketplaceRepository.delete(car.getId());
	}
	
	Logger logger = LoggerFactory.getLogger(CarMarketplaceService.class);
	
	@Cacheable("cars")
	public SearchPage<CarForMarketplace> searchCarBy(
			String manufacturer, String model, String bodyType, String fuelType, 
			Integer yearFrom, Integer yearTo,
			Integer mileageFrom, Integer mileageTo, 
			Integer powerFrom, Integer powerTo,
			Double priceFrom, Double priceTo,
			Pageable pageable){
		
		logger.info("search called");
		
		Criteria criteria = new Criteria();
		
		if(manufacturer != null && !manufacturer.isEmpty()) {
			criteria = criteria.and("manufacturer").is(manufacturer);
		}		
		if(model != null && !model.isEmpty()) {
			criteria = criteria.and("model").is(model);
		}   
		if(bodyType != null && !bodyType.isEmpty()) {
			criteria = criteria.and("bodyType").is(bodyType);
		}   
		if(fuelType != null && !fuelType.isEmpty()) {
			criteria = criteria.and("fuelType").is(fuelType);
		}   
		if(yearFrom != null || yearTo != null) {
			criteria = criteria.and("year").between(yearFrom, yearTo);
		}
		if(mileageFrom != null || mileageTo != null) {
			criteria = criteria.and("mileage").between(mileageFrom, mileageTo);
		}
		if(powerFrom != null || powerTo != null) {
			criteria = criteria.and("power").between(powerFrom, powerTo);
		}
		if(priceFrom != null || priceTo != null) {
			criteria = criteria.and("price").between(priceFrom, priceTo);
		}
		
		return carMarketplaceRepository.searchBy(criteria, pageable);
	}

}
