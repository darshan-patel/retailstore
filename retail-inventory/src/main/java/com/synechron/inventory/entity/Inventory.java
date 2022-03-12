/**
 * 
 */
package com.synechron.inventory.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author darshan
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="mst_inventory")
public class Inventory {

	@Id
	private long productId;
	private long stock;
	
	@Version
	@JsonIgnore
	private long versionId;
	
	
	public Inventory(long productId, long stock) {
		super();
		this.productId = productId;
		this.stock = stock;
	}
}
