/**
 * 
 */
package com.synechron.inventory.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synechron.dto.InventoryRequestDto;
import com.synechron.inventory.entity.Inventory;
import com.synechron.inventory.service.InventoryService;

/**
 * Rest API for Inventories resources
 * @author darshan
 *
 */
@RestController
@RequestMapping("/inventories")
public class InventoryController {

	@Autowired
	InventoryService inventoryService;
	
	/**
	 * Get all Inventory
	 * @return
	 */
	@GetMapping
	public List<Inventory> getAllInventory(){
		return inventoryService.getAllInventory();
	}
	
	/**
	 * Get Inventory by Id
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public Optional<Inventory> getInventory(@PathVariable long id){
		return inventoryService.getInventory(id);
	}
	
	@PostMapping
	public Inventory addInventory(@RequestBody InventoryRequestDto dto){
		return inventoryService.addInventory(dto);
	}
	
	@PutMapping("/{id}")
	public Inventory updateInventory(@PathVariable("id") long id, @RequestBody InventoryRequestDto dto){
		return inventoryService.updateInventory(dto,id);
	}
	
	@DeleteMapping("/{id}")
	public void deleteInventory(@PathVariable("id") long id){
		inventoryService.deleteInventory(id);
	}
	
}
