/**
 * 
 */
package com.synechron.event;

import java.util.Date;
import java.util.UUID;

import com.synechron.constants.OrderStatus;
import com.synechron.dto.OrderRequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 
 * @author darshan
 * Order Event
 *
 */
@Data
@NoArgsConstructor
@ToString
public class OrderEvent implements Event {

	private UUID eventId = UUID.randomUUID();
	private Date eventDate = new Date();
	private OrderRequestDto orderRequestDto;
	private OrderStatus orderStatus;

	/**
	 * @param orderRequestDto
	 * @param orderStatus
	 */
	public OrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
		super();
		this.orderRequestDto = orderRequestDto;
		this.orderStatus = orderStatus;
	}

	@Override
	public UUID getEventId() {
		return this.eventId;
	}
	
	@Override
	public Date getEventDate() {
		return this.eventDate;
	}

}
