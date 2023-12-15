package com.smg.carmarketplace.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.NoSuchIndexException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smg.carmarketplace.application.service.CarMarketplaceService;
import com.smg.carmarketplace.presentation.dto.CarListingPageDTO;

@RestController
@RequestMapping("/smg/marketplace")
public class CarMarketplaceController {

	@Autowired
	private CarMarketplaceService carMarketplaceService;
	
	@GetMapping("/cars")
	public ResponseEntity<CarListingPageDTO> searchMarketplaceBy(
			@RequestParam(required = false) String manufacturer, 
			@RequestParam(required = false) String model, 
			@RequestParam(required = false) String bodyType, 
			@RequestParam(required = false) String fuelType, 
			@RequestParam(required = false) Integer yearFrom, 
			@RequestParam(required = false) Integer yearTo,
			@RequestParam(required = false) Integer mileageFrom, 
			@RequestParam(required = false) Integer mileageTo, 
			@RequestParam(required = false) Integer powerFrom, 
			@RequestParam(required = false) Integer powerTo,
			@RequestParam(required = false) Double priceFrom, 
			@RequestParam(required = false) Double priceTo,
			Pageable pageable){
		
		try {
			var searchPage = carMarketplaceService.searchCarBy(
					manufacturer, model, bodyType, fuelType, 
					yearFrom, yearTo, 
					mileageFrom, mileageTo, 
					powerFrom, powerTo, 
					priceFrom, priceTo, 
					pageable);
			
			return ResponseEntity.ok(CarListingPageDTO.from(searchPage));
		} catch (NoSuchIndexException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
}
