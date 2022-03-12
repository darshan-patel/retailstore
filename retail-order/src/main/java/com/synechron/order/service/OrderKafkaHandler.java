/**
 * 
 */
package com.synechron.order.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.synechron.constants.InventoryStatus;
import com.synechron.constants.OrderStatus;
import com.synechron.constants.PaymentStatus;
import com.synechron.dto.OrderRequestDto;
import com.synechron.event.Event;
import com.synechron.event.InventoryEvent;
import com.synechron.event.OrderEvent;
import com.synechron.event.PaymentEvent;
import com.synechron.order.entity.Order;
import com.synechron.order.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

import static com.synechron.constants.Constants.*;
/**
 * 
 * 
 * Order Kafka Handler
 * Produce Order Event to ORDER_TOPIC
 * Consume Inventory Event to INVENTORY_TOPIC
 * Consume payment Event from PAYMENT_TOPIC
 * @author darshan
 *
 */
@Service
@Slf4j
public class OrderKafkaHandler {

	@Autowired
	private KafkaTemplate<String, OrderEvent> producerTemplate;


	@Autowired
	private OrderRepository orderRepository;

	public void publishOrderEvent(OrderRequestDto dto, OrderStatus orderStatus) {
		OrderEvent orderEvent = new OrderEvent(dto, orderStatus);
		log.info("---------------------Generating Event---------------------");
		log.info("Event :" + orderEvent);
		producerTemplate.send(ORDER_TOPIC, orderEvent);
	}

	/**
	 * Processing Inventory event If Inventory completed then update inventory
	 * status in order transaction If Inventory failed them generate cancel order
	 * event
	 * 
	 * @param inventoryEvent
	 */
	@KafkaListener(topics = INVENTORY_TOPIC, groupId = INVENTORY_GROUP, containerFactory = "inventoryListenerContainerFactory")
	public void orderInventoryProcessor(InventoryEvent inventoryEvent) {
		log.info("Listener : " + INVENTORY_TOPIC + "Group :" + INVENTORY_GROUP);
		log.info("inventoryStatus	:" + inventoryEvent.getInventoryStatus());

		updateOrder(inventoryEvent, inventoryEvent.getInventoryRequestDto().getOrderId());
	}

	/**
	 * Payment Event Listener Processing Payment event If Inventory completed then
	 * update inventory status in order transaction If Inventory failed them
	 * generate cancel order event
	 * 
	 * @param inventoryEvent
	 */

	@KafkaListener(topics = PAYMENT_TOPIC, groupId = PAYMENT_GROUP, containerFactory = "paymentListenerContainerFactory")
	public void orderPaymentProcessor(PaymentEvent paymentEvent) {
		log.info("Listener : " + PAYMENT_TOPIC + ", Group :" + PAYMENT_GROUP);
		log.info("paymentStatus	:" + paymentEvent.getPaymentStatus());

		updateOrder(paymentEvent,paymentEvent.getPaymentRequestDto().getOrderId());
	}

	
	private synchronized void updateOrder(Event event, long orderId) {
		orderRepository.findById(orderId).ifPresent(o -> {
			o = updateEventStatus(event, o);
			if (PaymentStatus.PAYMENT_ROLLBACK.equals(o.getPaymentStatus())
					|| InventoryStatus.INVENTORY_ROLLBACK.equals(o.getInventoryStatus())) {
				orderRepository.save(o);
			} else
				updateOrder(o);
		});
	}

	private Order updateEventStatus(Event event, Order Order) {
		if (event instanceof PaymentEvent) {
			Order.setPaymentStatus(((PaymentEvent) event).getPaymentStatus());
		} else {
			Order.setInventoryStatus(((InventoryEvent) event).getInventoryStatus());
		}
		return Order;
	}

	@Transactional
	public void updateOrder(Order order) {

		boolean isInventoryCompleted = InventoryStatus.INVENTORY_COMPLETED.equals(order.getInventoryStatus());
		boolean isPaymentCompleted = PaymentStatus.PAYMENT_COMPLETED.equals(order.getPaymentStatus());

		OrderStatus orderStatus = order.getOrderstatus();

		if (isInventoryCompleted && isPaymentCompleted) {
			// order completed
			order.setOrderstatus(OrderStatus.ORDER_COMPLETED);
		} else if (order.getInventoryStatus() != null && !isInventoryCompleted
				|| order.getPaymentStatus() != null && !isPaymentCompleted) {
			// inventory failed or payment failed so generate cancel order event
			order.setOrderstatus(OrderStatus.ORDER_CANCELLED);
		}
		orderRepository.save(order);
		// publish order event of order status is updated
		if (orderStatus != order.getOrderstatus()) {
			publishOrderEvent(convertDtoToEntityToDTO(order), order.getOrderstatus());
		}
	}

	private OrderRequestDto convertDtoToEntityToDTO(Order order) {
		OrderRequestDto dto = new OrderRequestDto();
		dto.setOrderId(order.getOrderId());
		dto.setUserId(order.getUserId());
		dto.setProductPrice(order.getProductPrice());
		dto.setProductId(order.getProductId());
		return dto;
	}
}
