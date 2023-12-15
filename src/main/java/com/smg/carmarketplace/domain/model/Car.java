package com.smg.carmarketplace.domain.model;

import lombok.Data;

@Data
public class Car {
	
	private Integer id;
	private Integer mileage;
	private Integer year;
	private Double price;
	private String vin;
	
	private Model model;

	
	public CarForMarketplace forMarketplace() {
		var car = new CarForMarketplace();
		car.setId(this.id);
		car.setManufacturer(this.getModel().getManufacturer().getName());
		car.setModel(this.getModel().getName());
		car.setBodyType(this.getModel().getBodyType());
		car.setFuelType(this.getModel().getFuelType());
		car.setPower(this.getModel().getPower());
		car.setMileage(this.getMileage());
		car.setYear(this.getYear());
		car.setPrice(this.getPrice());
		
		return car;
	}

}
