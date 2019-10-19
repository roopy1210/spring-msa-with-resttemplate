package com.roopy.services.order.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.roopy.services.order.domain.Order;
import com.roopy.services.order.service.IOrderService;

@RestController
public class OrderController {
	
	@Autowired
	private IOrderService orderService;

	@RequestMapping(value = "/order", method = RequestMethod.POST)
	public Order save(HttpServletRequest request, HttpServletResponse response,
			@RequestBody Order order) throws Exception {
		
		try {
			order = orderService.save(order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return order;
	}
	
	@RequestMapping(value = "/order/{orderId}", method = RequestMethod.POST)
	public Order findOrder(HttpServletRequest request, HttpServletResponse response, @PathVariable String orderId,
			@RequestParam HashMap<String, String> param) throws Exception {
		
		return orderService.find(orderId);
	}
}
