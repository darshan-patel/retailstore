/**
 * 
 */
package com.synechron.dto;

import com.synechron.constants.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author darshan
 * payment response dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {

	private long userId;
	private long orderId;
	private double price;
	private PaymentStatus paymentStatus;
	
}
