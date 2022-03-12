/**
 * 
 */
package com.synechron.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synechron.dto.OrderRequestDto;
import com.synechron.order.entity.Order;
import com.synechron.order.service.OrderService;

/**
 * 
 * @author darshan
 *
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

	@Autowired
	OrderService orderService;
	
	@PostMapping("/create")
	public Order createOrder(@RequestBody OrderRequestDto dto) {
		return orderService.createOrder(dto);
	}
	
	@GetMapping
	public List<Order> getOrders(){
		return orderService.getAllOrders();
	}
	
	@GetMapping("/{id}")
	public Order getOrderById(@PathVariable long id){
		return orderService.getOrderById(id);
	}
}
