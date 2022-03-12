/**
 * 
 */
package com.synechron.payment.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.synechron.payment.entity.UserBalance;


/**
 * 
 * @author darshan
 *
 */
public interface UserBalanceRepository extends JpaRepository<UserBalance, Long> {

}
