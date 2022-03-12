/**
 * 
 */
package com.synechron.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synechron.inventory.entity.InventoryTransaction;

/**
 * 
 * @author darshan
 *
 */
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {

}
