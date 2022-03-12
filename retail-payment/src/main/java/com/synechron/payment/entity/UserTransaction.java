/**
 * 
 */
package com.synechron.payment.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author darshan
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserTransaction {
	private long userid;
	@Id
	private long orderId;
	private double amount;
	@Version
	@JsonIgnore
	private long versionId;
	
	/**
	 * @param userid
	 * @param orderId
	 * @param price
	 */
	public UserTransaction(long userid, long orderId, double amount) {
		super();
		this.userid = userid;
		this.orderId = orderId;
		this.amount = amount;
	}
}
