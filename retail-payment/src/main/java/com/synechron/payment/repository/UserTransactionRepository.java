/**
 * 
 */
package com.synechron.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synechron.payment.entity.UserTransaction;

/**
 * 
 * @author darshan
 *
 */
public interface UserTransactionRepository extends JpaRepository<UserTransaction, Long> {

}
