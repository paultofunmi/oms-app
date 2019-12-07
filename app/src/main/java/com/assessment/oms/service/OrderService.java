package com.assessment.oms.service;

import com.assessment.oms.dto.request.OrderRequestDTO;
import com.assessment.oms.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Date;


public interface OrderService {
    ResponseEntity placeOrder(OrderRequestDTO orderRequestDTO);
    ResponseEntity ordersByStartEndDate(String start, String end, Pageable pageable);
    ResponseEntity viewOrder(Long id);
}
