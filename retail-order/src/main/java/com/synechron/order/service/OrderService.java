/**
 * 
 */
package com.synechron.order.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synechron.constants.OrderStatus;
import com.synechron.dto.OrderRequestDto;
import com.synechron.order.entity.Order;
import com.synechron.order.repository.OrderRepository;

/**
 * 
 * @author darshan
 *
 */
@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepository;
	
	@Autowired
	private OrderKafkaHandler orderStatusPublisher;
	
	/**
	 * Convert Dto to order and then
	 * Create Order and generate ORDER_CREATED event
	 * @param dto
	 * @return
	 */
	@Transactional
	public Order createOrder(OrderRequestDto dto) {
		Order order =orderRepository.save(convertDtoToEntity(dto));
		dto.setOrderId(order.getOrderId());
		
		//produce kafka event with status ORDER_CREATED
		orderStatusPublisher.publishOrderEvent(dto, OrderStatus.ORDER_CREATED);
		return order;
	}
	
	public List<Order> getAllOrders(){
		return orderRepository.findAll();
	}
	
	private Order convertDtoToEntity(OrderRequestDto dto) {
		Order order = new Order();
		order.setUserId(dto.getUserId());
		order.setProductId(dto.getProductId());
		order.setProductQty(dto.getProductQty());
		order.setProductPrice(dto.getProductPrice());
		order.setOrderstatus(OrderStatus.ORDER_CREATED);
		return order;
	}

	public Order getOrderById(long id) {
		return orderRepository.findById(id).get();
	}

}
