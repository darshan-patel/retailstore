/**
 * 
 */
package com.synechron.payment.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.synechron.payment.entity.UserBalance;
import com.synechron.payment.service.PaymentService;

/**
 * 
 * @author darshan
 *
 */
@RestController
@RequestMapping("/balances")
public class PaymentController {

	@Autowired
	PaymentService paymentService;
		
	@GetMapping
	public List<UserBalance> getInventory(){
		return paymentService.getAllUserBalance();
	}
}
