package com.smg.carmarketplace.presentation.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.data.elasticsearch.core.SearchPage;

import com.smg.carmarketplace.domain.model.CarForMarketplace;

import lombok.Data;

@Data
public class CarListingPageDTO {
	
	private Collection<CarForMarketplace> cars = new ArrayList<>();
	private Integer number;
	private Integer size;
	private String sortedBy;

	
	public static CarListingPageDTO from(SearchPage<CarForMarketplace> searchPage) {
		var page = new CarListingPageDTO();
		
		if(searchPage.getSearchHits() != null) {
			searchPage.getSearchHits().forEach(hit -> page.getCars().add(hit.getContent()));
		}
		
		page.setNumber(searchPage.getNumber());
		page.setSize(searchPage.getSize());
		page.setSortedBy(searchPage.getSort().toString());
		
		return page;
	}
}
