package com.roopy.services.payment.service;

import java.util.List;

import com.roopy.services.payment.domain.Order;
import com.roopy.services.payment.domain.Payment;

public interface IPaymentService {
	
	public List<Payment> save(Order order) throws Exception;
	
}
