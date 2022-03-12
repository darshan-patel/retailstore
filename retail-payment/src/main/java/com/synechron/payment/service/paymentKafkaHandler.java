/**
 * 
 */
package com.synechron.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.synechron.constants.OrderStatus;
import com.synechron.event.OrderEvent;
import com.synechron.event.PaymentEvent;

import lombok.extern.slf4j.Slf4j;

import static com.synechron.constants.Constants.*;

/**
 * Kafka Handler
 * Listen Order Event from ORDER_TOPIC
 * Produce Payment Event to PAYMENT_TOPIC
 * @author darshan
 *
 */
@Service
@Slf4j
public class paymentKafkaHandler {

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	KafkaTemplate<String, PaymentEvent> kafkaPaymentTemplate;
	
	
	/**
	 * Order Event Listener
	 * 
	 * If order is created then check User Balance. 
	 * 		If balance is available them deduct amount and add entry in user balance Transaction and then generate payment Complete event
	 * 		else generate payment failed event
	 * If order is cancelled then get user balance from Payment transaction using orderId, add it to Payment and then remove user balance Transaction entry
	 * 
	 * @param orderEvent
	 */
	@KafkaListener(topics = ORDER_TOPIC, groupId =  ORDER_PAYMENT_GROUP,
			containerFactory = "orderPaymentListenerContainerFactory")
	public void paymentProcessor(OrderEvent orderEvent) {
		log.info("Listener : "+ ORDER_TOPIC + "Group :" + ORDER_PAYMENT_GROUP);
		log.info("orderStatus	:"+ orderEvent.getOrderStatus());
		if(OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
			PaymentEvent paymentEvent = paymentService.newOrderEvent(orderEvent);
			log.info("---------------------Generating Event---------------------");
			log.info("Event :" + paymentEvent);
			kafkaPaymentTemplate.send(PAYMENT_TOPIC,paymentEvent);
		}else if(OrderStatus.ORDER_CANCELLED.equals(orderEvent.getOrderStatus())) {
			paymentService.rollbackOrderEvent(orderEvent).ifPresent(pe -> kafkaPaymentTemplate.send(PAYMENT_TOPIC,pe));
		}
	}
}