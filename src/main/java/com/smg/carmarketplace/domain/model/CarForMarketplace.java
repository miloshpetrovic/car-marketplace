package com.smg.carmarketplace.domain.model;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Data
@Document(indexName = "cars", createIndex = true)
public class CarForMarketplace {
	
	private Integer id;
	private String manufacturer;
	private String model;
	private String bodyType;
	private String fuelType;
	private Integer year;
	private Integer mileage;
	private Integer power;
	private Double price;

}
