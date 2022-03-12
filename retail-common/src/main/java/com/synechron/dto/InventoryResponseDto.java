/**
 * 
 */
package com.synechron.dto;

import com.synechron.constants.InventoryStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author darshan
 * Inventory Response dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponseDto {

	private long userId;
	private long orderId;
	private long productId;
	private InventoryStatus inventoryStatus;
	
}
