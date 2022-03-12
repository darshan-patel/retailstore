/**
 * 
 */
package com.synechron.event;

import java.util.Date;
import java.util.UUID;

import com.synechron.constants.InventoryStatus;
import com.synechron.dto.InventoryRequestDto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 
 * @author darshan
 * Inventory Event
 */
@Data
@NoArgsConstructor
@ToString
public class InventoryEvent implements Event {

	private UUID eventId = UUID.randomUUID();
	private Date eventDate = new Date();
	private InventoryRequestDto inventoryRequestDto;
	private InventoryStatus inventoryStatus;
	
	/**
	 * @param inventoryRequestDto
	 * @param inventoryStatus
	 */
	public InventoryEvent(InventoryRequestDto inventoryRequestDto, InventoryStatus inventoryStatus) {
		super();
		this.inventoryRequestDto = inventoryRequestDto;
		this.inventoryStatus = inventoryStatus;
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
