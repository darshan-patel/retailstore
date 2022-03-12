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

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class UserBalance {
	
	@Id
	private long userid;
	private double balance;
	@Version
	@JsonIgnore
	private long versionId;
	
	/**
	 * @param userid
	 * @param balance
	 */
	public UserBalance(long userid, double balance) {
		super();
		this.userid = userid;
		this.balance = balance;
	}
	
}
