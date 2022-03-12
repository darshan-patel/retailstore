/**
 * 
 */
package com.synechron.order.entity;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.synechron.constants.InventoryStatus;
import com.synechron.constants.OrderStatus;
import com.synechron.constants.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author darshan
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="mst_order")
public class Order {

	@Id
	@GeneratedValue
	private long orderId;
	private long userId;
	private long productId;
	private double productPrice;
	private int productQty;
	@Enumerated(EnumType.STRING)
	private OrderStatus orderstatus;
	@Enumerated(EnumType.STRING)
	private PaymentStatus paymentStatus;
	@Enumerated(EnumType.STRING)
	private InventoryStatus inventoryStatus;
	@Version
	@JsonIgnore
	private long versionId;
	
}