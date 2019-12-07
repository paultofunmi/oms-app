package com.assessment.oms.serviceImpl;

import com.assessment.oms.dto.request.OrderItemRequestDTO;
import com.assessment.oms.dto.request.OrderRequestDTO;
import com.assessment.oms.dto.response.OrderResponse;
import com.assessment.oms.exception.BadRequestException;
import com.assessment.oms.model.Order;
import com.assessment.oms.model.OrderItem;
import com.assessment.oms.model.Product;
import com.assessment.oms.repository.OrderRepository;
import com.assessment.oms.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderServiceImplTest {
    @Mock
    ProductRepository productRepository;

    @Mock
    OrderRepository orderRepository;

    @InjectMocks
    OrderServiceImpl orderServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testPlaceOrder() {
        Product testProduct = Product.builder()
                .id(1L)
                .productPrice(BigDecimal.ONE)
                .build();

        when(orderRepository.save(any())).thenReturn(Mockito.mock(Order.class));
        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));

        OrderItemRequestDTO orderItemRequestDTO1 = OrderItemRequestDTO.builder()
                                                        .productId(1L)
                                                        .quantity(1)
                                                        .build();

        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder()
                                                .buyerEmail("paul@testoms.com")
                                                .orderItems(Arrays.asList(orderItemRequestDTO1))
                                                .build();

        OrderResponse orderResponse = OrderResponse.builder().id(0L).orderItems(new HashSet<>()).build();

        ResponseEntity result = orderServiceImpl.placeOrder(orderRequestDTO);
        Assertions.assertEquals(new ResponseEntity( orderResponse , HttpStatus.CREATED), result);
    }

    @Test
    void testOrdersByStartEndDate() {
        Page<Order> orders = Mockito.mock(Page.class);
        ResponseEntity r = new ResponseEntity(orders, HttpStatus.OK);
        when(orderRepository.findByDateOfOrderGreaterThanEqualAndDateOfOrderLessThanEqual(any(), any(), any()))
                .thenReturn(orders);

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,"id"));
        ResponseEntity result = orderServiceImpl.ordersByStartEndDate("01-12-2019", "03-12-2019", pageable);
        Assertions.assertEquals(r, result);
    }

    @Test
    void testExceptionThrownWhenBuyerEmailIsAbsent(){
        Product testProduct = Product.builder()
                .id(1L)
                .productPrice(BigDecimal.ONE)
                .build();

        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));

        Assertions.assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.placeOrder(Mockito.mock(OrderRequestDTO.class));
        });
    }

    @Test
    void testExceptionIsThrownWhenOrderItemDoesNotHaveQuantity(){
        Product testProduct = Product.builder()
                .id(1L)
                .productPrice(BigDecimal.ONE)
                .build();

        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));

        OrderItemRequestDTO orderItemRequestDTO1 = OrderItemRequestDTO.builder()
                .productId(1L)
                .build();

        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder()
                .buyerEmail("paul@testoms.com")
                .orderItems(Arrays.asList(orderItemRequestDTO1))
                .build();

        Assertions.assertThrows(BadRequestException.class, () -> {
            orderServiceImpl.placeOrder(orderRequestDTO);
        });
    }

    @Test
    void testTotalOrderValue(){
        Product testProduct = Product.builder()
                .id(1L)
                .productPrice(BigDecimal.TEN)
                .build();

        when(orderRepository.save(any())).thenReturn(Order.builder().orderValue(new BigDecimal(150)).build());
        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));

        OrderItemRequestDTO orderItemRequestDTO1 = OrderItemRequestDTO.builder()
                .productId(1L)
                .quantity(5)
                .build();

        OrderItemRequestDTO orderItemRequestDTO2 = OrderItemRequestDTO.builder()
                .productId(2L)
                .quantity(10)
                .build();

        OrderRequestDTO orderRequestDTO = OrderRequestDTO.builder()
                .buyerEmail("paul@testoms.com")
                .orderItems(Arrays.asList(orderItemRequestDTO1))
                .build();

        OrderResponse orderResponse = OrderResponse.builder().id(0L).orderItems(new HashSet<>()).build();

        OrderResponse result = (OrderResponse)orderServiceImpl.placeOrder(orderRequestDTO).getBody();
        Assertions.assertEquals(new BigDecimal(150), result.getOrderValue());
    }
}
