/**
 * 
 */
package com.synechron.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.synechron.constants.OrderStatus;
import com.synechron.event.InventoryEvent;
import com.synechron.event.OrderEvent;

import lombok.extern.slf4j.Slf4j;

import static com.synechron.constants.Constants.*;

/**
 * 
 * Kafka Handler
 * Listen Order Event from ORDER_TOPIC
 * Produce Inventory Event to INVENTORY_TOPIC
 * 
 * @author darshan
 *
 */
@Service
@Slf4j
public class InventoryKafkaHandler {

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	KafkaTemplate<String, InventoryEvent> publishInventory;
	
	/**
	 * Order Event Listener
	 * 
	 * If order is created then check inventory. 
	 * 		If inventory available them deduct inventory and add entry in Inventory Transaction and then generate Inventory Complete event
	 * 		else generate Inventory failed event
	 * If order is cancelled then get inventory from Inventory transaction using orderId, add it to Inventory and then remove Inventory Transaction entry
	 * 
	 * @param orderEvent
	 */
	@KafkaListener(topics = ORDER_TOPIC, groupId =  ORDER_GROUP, containerFactory = "orderInventoryListenerContainerFactory")
	public void inventoryProcessor(OrderEvent orderEvent) {
		log.info("Listener : "+ ORDER_TOPIC + "Group :" + ORDER_GROUP);
		log.info("OrderStatus	:"+orderEvent.getOrderStatus());
		if(OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
			InventoryEvent inventoryEvent = inventoryService.newOrderEvent(orderEvent);
			publishInventory.send(INVENTORY_TOPIC,inventoryEvent);
		}else if(OrderStatus.ORDER_CANCELLED.equals(orderEvent.getOrderStatus())) {
			inventoryService.rollbackOrderEvent(orderEvent).ifPresent(oe->publishInventory.send(INVENTORY_TOPIC, oe));
		}
	}
}