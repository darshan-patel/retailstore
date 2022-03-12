/**
 * 
 */
package com.synechron.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synechron.order.entity.Order;

/**
 * 
 * @author darshan
 *
 */
public interface OrderRepository extends JpaRepository<Order, Long>{

}
