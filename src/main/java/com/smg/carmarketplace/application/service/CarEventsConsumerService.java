package com.smg.carmarketplace.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.smg.carmarketplace.domain.model.CarEvent;

@Service
public class CarEventsConsumerService {

	Logger logger = LoggerFactory.getLogger(CarEventsConsumerService.class);
	
	@Autowired
	private CarMarketplaceService carMarketplaceService;
	
	@KafkaListener(topics = "smg.car.events",  groupId = "consumer-group-1")
	public void receiveMessage(CarEvent event) {
		switch(event.getEvent()) {
			case CREATED:
				carMarketplaceService.save(event.getCar());
				break;
			case UPDATED:
				carMarketplaceService.update(event.getCar());
				break;
			case DELETED:
				carMarketplaceService.delete(event.getCar());			
				break;
		}
	}
	
}
