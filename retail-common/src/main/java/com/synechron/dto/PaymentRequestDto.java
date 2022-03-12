/**
 * 
 */
package com.synechron.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author darshan
 * payment request dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {
	
	private long orderId;
	private long userId;
	private double price;

}
