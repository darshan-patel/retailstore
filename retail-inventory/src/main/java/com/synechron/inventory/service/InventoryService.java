/**
 * 
 */
package com.synechron.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synechron.constants.InventoryStatus;
import com.synechron.dto.InventoryRequestDto;
import com.synechron.dto.OrderRequestDto;
import com.synechron.event.InventoryEvent;
import com.synechron.event.OrderEvent;
import com.synechron.inventory.entity.Inventory;
import com.synechron.inventory.entity.InventoryTransaction;
import com.synechron.inventory.repository.InventoryRepository;
import com.synechron.inventory.repository.InventoryTransactionRepository;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author darshan
 *
 */
@Service
@Slf4j
public class InventoryService {

	@Autowired
	private InventoryRepository inventoryRepository;

	@Autowired
	private InventoryTransactionRepository inventoryTransactionRepository;

	@PostConstruct
	public void initInventoryInDB() {
		log.info("Adding default Inventories");
		inventoryRepository.saveAll(
				Stream.of(
						new Inventory(1, 5000), 
						new Inventory(2, 3000), 
						new Inventory(3, 4200),
						new Inventory(4, 20000), 
						new Inventory(5, 9000)).collect(Collectors.toList()));
	}

	/**
	 * Processing Order created event for inventory Here, We need to check whether
	 * inventory is available or not. If Inventory Available then deduct inventory
	 * and generate Inventory completed event This event will be read by Order
	 * service and update inventory status in order transaction else generate
	 * inventory failed event
	 * 
	 * @param orderEvent
	 * @return
	 */
	@Transactional
	public InventoryEvent newOrderEvent(OrderEvent orderEvent) {
		return inventoryRepository.findById(orderEvent.getOrderRequestDto().getProductId())
				.filter(inv -> inv.getStock() >= orderEvent.getOrderRequestDto().getProductQty()).map(inv -> {
					inv.setStock(inv.getStock() - orderEvent.getOrderRequestDto().getProductQty());
					inventoryTransactionRepository
							.save(new InventoryTransaction(orderEvent.getOrderRequestDto().getOrderId(),
									orderEvent.getOrderRequestDto().getProductId(),
									orderEvent.getOrderRequestDto().getProductQty()));
					return new InventoryEvent(transformDto(orderEvent.getOrderRequestDto()),
							InventoryStatus.INVENTORY_COMPLETED);
				}).orElse(new InventoryEvent(transformDto(orderEvent.getOrderRequestDto()),
						InventoryStatus.INVENTORY_FAILED));
	}

	/**
	 * Cancel order event. So, Roll back inventory transaction Get inventory
	 * transaction by on order id and add deducted inventory
	 * 
	 * @param orderEvent
	 */
	@Transactional
	public Optional<InventoryEvent> rollbackOrderEvent(OrderEvent orderEvent) {

		return inventoryTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId()).map(it -> {
			inventoryRepository.findById(it.getProductId()).ifPresent(inv -> {
				inv.setStock(inv.getStock() + it.getProductQty());
				inventoryRepository.save(inv);
			});
			inventoryTransactionRepository.delete(it);
			return Optional.of(new InventoryEvent(transformDto(orderEvent.getOrderRequestDto()),
					InventoryStatus.INVENTORY_ROLLBACK));
		}).orElse(Optional.empty());
	}

	private InventoryRequestDto transformDto(OrderRequestDto orderRequestDto) {
		return InventoryRequestDto.builder().orderId(orderRequestDto.getOrderId())
				.productId(orderRequestDto.getProductId()).userId(orderRequestDto.getUserId())
				.productQty(orderRequestDto.getProductQty()).build();
	}

	public List<Inventory> getAllInventory() {
		return inventoryRepository.findAll();
	}

	public Optional<Inventory> getInventory(long id) {
		return inventoryRepository.findById(id);
	}

	public Inventory addInventory(InventoryRequestDto dto) {
		return inventoryRepository.save(dtoToEntity(dto));
	}

	private Inventory dtoToEntity(InventoryRequestDto dto) {
		Inventory inventory = new Inventory();
		inventory.setProductId(dto.getProductId());
		inventory.setStock(dto.getProductQty());
		return inventory;
	}

	public Inventory updateInventory(InventoryRequestDto dto, long id) {
		return inventoryRepository.findById(id).map(inventory -> {
			inventory.setStock(dto.getProductQty());
			return inventoryRepository.save(inventory);
		}).orElseGet(() -> {
			Inventory newInventory = new Inventory();
			newInventory.setProductId(id);
			return inventoryRepository.save(newInventory);
		});
	}

	public void deleteInventory(long id) {
		inventoryRepository.deleteById(id);
	}
}
