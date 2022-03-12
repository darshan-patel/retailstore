/**
 * 
 */
package com.synechron.inventory.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Use to store Inventory Transaction which will be useful to rollback when order cancelled
 * @author darshan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class InventoryTransaction {

	@Id
	private long orderId;
	private long productId;
	private int productQty;
	@Version
	@JsonIgnore
	private long versionId;
	
	/**
	 * 
	 * @param orderId
	 * @param productId
	 * @param productQty
	 */
	public InventoryTransaction(long orderId, long productId, int productQty) {
		super();
		this.orderId = orderId;
		this.productId = productId;
		this.productQty = productQty;
	}
	
}
