package com.roopy.services.order.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.roopy.services.order.domain.Order;
import com.roopy.services.order.domain.OrderDetailIdentity;
import com.roopy.services.order.helper.IDGeneratorHelper;
import com.roopy.services.order.repository.OrderDetailRepository;
import com.roopy.services.order.repository.OrderRepository;
import com.roopy.services.order.service.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private IDGeneratorHelper idGenerator;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional
	@Override
	public Order save(Order order) throws Exception {
		
		HttpEntity<Order> request = new HttpEntity<>(order);

		/*주문ID생성*/
		String orderId = idGenerator.getOrderId();
		
		/*주문ID설정*/
		order.setOrderId(orderId);
		
		/*주문날짜설정*/
		order.setOrderDtm(orderId.substring(1));
		
		int orderSeq = 1;
		
		/*주문상세추가정보설정*/
		for (int i = 0; i < order.getOrderDetails().size(); i++) {
			/*주문ID설정*/
			order.getOrderDetails().get(i).setOrderDetailIdentity(new OrderDetailIdentity(orderId, orderSeq + i));
			
			/*주문상세정보 저장*/
			orderDetailRepository.save(order.getOrderDetails().get(i));
		}
		
		/*주문정보저장*/
		order = orderRepository.save(order);

		/*결제처리*/
		ResponseEntity<List> pymentResponse =  restTemplate.postForEntity("http://localhost:7001/payment", request, List.class);
		order.setPayments(pymentResponse.getBody());
		
		/*상품수량업데이트처리*/
		ResponseEntity<List> productResponse = null;
		if (pymentResponse.getStatusCodeValue() == 200) {
			productResponse =  restTemplate.postForEntity("http://localhost:7002/product", request, List.class);
			order.setOrderDetails(productResponse.getBody());
		}
		
		return order;
		
	}

	public Order find(String orderId) throws Exception {
		return orderRepository.find(orderId);
	}

}
