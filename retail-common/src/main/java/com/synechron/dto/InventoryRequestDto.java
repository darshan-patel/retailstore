/**
 * 
 */
package com.synechron.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author darshan
 * Inventory request dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryRequestDto {

	private long userId;
	private long productId;
	private long orderId;
	private long productQty;
	public InventoryRequestDto(long productQty) {
		super();
		this.productQty = productQty;
	}
}