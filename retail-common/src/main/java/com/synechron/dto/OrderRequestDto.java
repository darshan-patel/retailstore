/**
 * 
 */
package com.synechron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author darshan
 * Order request dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDto {

	private long userId;
	private long productId;
	private int productQty;
	private long orderId;
	private double productPrice;
	
}