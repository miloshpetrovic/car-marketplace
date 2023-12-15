package com.smg.carmarketplace.domain.model;

import lombok.Data;

@Data
public class Manufacturer {

	private Integer id;
	private String name;
	private String country;
	private Integer fromYear;
	private Integer toYear;
			
}
