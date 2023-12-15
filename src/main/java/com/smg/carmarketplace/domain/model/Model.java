package com.smg.carmarketplace.domain.model;

import lombok.Data;

@Data
public class Model {

	private Integer id;
	private String name;
	private String bodyType;
	private String fuelType;
	private Integer power;
	
	private Manufacturer manufacturer;
	
}
