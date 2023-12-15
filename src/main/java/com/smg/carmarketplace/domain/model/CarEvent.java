package com.smg.carmarketplace.domain.model;

import lombok.Data;

@Data
public class CarEvent {
	
	public static enum Event{
		CREATED, UPDATED, DELETED;
	}
	
	private Event event;
	private Car car;

}
