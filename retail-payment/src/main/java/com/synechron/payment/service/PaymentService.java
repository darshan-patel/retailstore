/**
 * 
 */
package com.synechron.payment.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synechron.constants.PaymentStatus;
import com.synechron.dto.OrderRequestDto;
import com.synechron.dto.PaymentRequestDto;
import com.synechron.event.OrderEvent;
import com.synechron.event.PaymentEvent;
import com.synechron.payment.entity.UserBalance;
import com.synechron.payment.entity.UserTransaction;
import com.synechron.payment.repository.UserBalanceRepository;
import com.synechron.payment.repository.UserTransactionRepository;

/**
 * 
 * @author darshan
 *
 */
@Service
public class PaymentService {

	@Autowired
	private UserBalanceRepository userBalanceRepository;

	@Autowired
	private UserTransactionRepository userTransactionRepository;
	
	@PostConstruct
    public void initUserBalanceInDB() {
        userBalanceRepository.saveAll(Stream.of(new UserBalance(101, 5000),
                new UserBalance(102, 3000),
                new UserBalance(103, 4200),
                new UserBalance(104, 20000),
                new UserBalance(105, 999)).collect(Collectors.toList()));
    }
	
	/**
	 * Processing Order created event for Payment Here, We need to check whether
	 * user balance is available or not. If available then deduct amount
	 * and generate Payment completed event This event will be read by Order
	 * service and update payment status in order transaction else generate
	 * payment failed event
	 * 
	 * @param orderEvent
	 * @return
	 */

	@Transactional
	public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
		OrderRequestDto dto = orderEvent.getOrderRequestDto();
		PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
		paymentRequestDto.setOrderId(dto.getOrderId());
		paymentRequestDto.setPrice(dto.getProductPrice());
		paymentRequestDto.setUserId(dto.getUserId());

		double amount = dto.getProductQty() * dto.getProductPrice();
		return userBalanceRepository.findById(dto.getUserId()).filter(ub -> ub.getBalance() >= amount).map(ub -> {
			ub.setBalance(ub.getBalance() - amount);
			userTransactionRepository.save(new UserTransaction(dto.getUserId(), dto.getOrderId(), amount));
			return new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_COMPLETED);
		}).orElse(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_FAILED));
	}

	/**
	 * Cancel order event. So, Roll back payment transaction Get payment
	 * transaction by order id and compensate user balance
	 * @param orderEvent
	 * @return
	 */
	@Transactional
	public Optional<PaymentEvent> rollbackOrderEvent(OrderEvent orderEvent) {
		OrderRequestDto dto = orderEvent.getOrderRequestDto();
		PaymentRequestDto paymentRequestDto = new PaymentRequestDto();
		paymentRequestDto.setOrderId(dto.getOrderId());
		paymentRequestDto.setPrice(dto.getProductPrice());
		paymentRequestDto.setUserId(dto.getUserId());
		
		return userTransactionRepository.findById(orderEvent.getOrderRequestDto().getOrderId()).map(ut -> {
			userBalanceRepository.findById(ut.getUserid())
			.ifPresent(ub -> {
				ub.setBalance(ub.getBalance() + ut.getAmount());
				userBalanceRepository.save(ub);
			});
			userTransactionRepository.delete(ut);
			return Optional.of(new PaymentEvent(paymentRequestDto, PaymentStatus.PAYMENT_ROLLBACK));
		}).orElse(Optional.empty());
	}

	public List<UserBalance> getAllUserBalance() {
		return userBalanceRepository.findAll();
	}
}
