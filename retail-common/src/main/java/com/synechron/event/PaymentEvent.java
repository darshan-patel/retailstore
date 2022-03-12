/**
 * 
 */
package com.synechron.event;

import java.util.Date;
import java.util.UUID;

import com.synechron.constants.PaymentStatus;
import com.synechron.dto.PaymentRequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 
 * @author Darshan
 * Payment Event
 */
@Data
@NoArgsConstructor
@ToString
public class PaymentEvent implements Event{

	private UUID eventId = UUID.randomUUID();
	private Date eventDate = new Date();
	private PaymentRequestDto paymentRequestDto;
	private PaymentStatus paymentStatus;
	
	@Override
	public UUID getEventId() {
		return this.eventId;
	}
	@Override
	public Date getEventDate() {
		return this.eventDate;
	}
	
	/**
	 * @param paymentRequestDto
	 * @param paymentStatus
	 */
	public PaymentEvent(PaymentRequestDto paymentRequestDto, PaymentStatus paymentStatus) {
		super();
		this.paymentRequestDto = paymentRequestDto;
		this.paymentStatus = paymentStatus;
	}
}
