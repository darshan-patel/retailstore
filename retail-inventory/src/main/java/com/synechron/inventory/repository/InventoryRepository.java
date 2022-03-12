/**
 * 
 */
package com.synechron.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synechron.inventory.entity.Inventory;

/**
 * @author darshan
 *
 */
public interface InventoryRepository extends JpaRepository<Inventory, Long>{

}
