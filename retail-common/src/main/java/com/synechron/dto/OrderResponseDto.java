/**
 * 
 */
package com.synechron.dto;

import com.synechron.constants.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author darshan
 * Order response dro
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

	private long userId;
	private long productId;
	private long orderId;
	private double price;
	private OrderStatus orderStatus;
	
}
