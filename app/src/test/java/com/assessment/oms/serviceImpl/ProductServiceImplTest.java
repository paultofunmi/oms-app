package com.assessment.oms.serviceImpl;

import com.assessment.oms.dto.request.OrderRequestDTO;
import com.assessment.oms.exception.BadRequestException;
import com.assessment.oms.model.Order;
import com.assessment.oms.model.Product;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddProduct() {
        Product testProduct = Product.builder()
                                    .productName("testProduct")
                                    .productPrice(BigDecimal.TEN)
                                    .build();
        when(productRepository.save(any())).thenReturn(testProduct);

        ResponseEntity result = productServiceImpl.addProduct(testProduct);
        Assertions.assertEquals(new ResponseEntity( testProduct , HttpStatus.CREATED), result);
    }

    @Test
    void testUpdateProduct() {
        Product testProduct = Product.builder()
                .productName("testProduct")
                .id(1L)
                .productPrice(BigDecimal.TEN)
                .build();

        when(productRepository.save(any())).thenReturn(testProduct);
        when(productRepository.findById(any())).thenReturn(Optional.of(testProduct));

        ResponseEntity result = productServiceImpl.updateProduct(testProduct, 1L);
        Assertions.assertEquals(new ResponseEntity( testProduct , HttpStatus.OK), result);
    }

    @Test
    void testAllProducts() {
        Page<Product> products = Mockito.mock(Page.class);
        ResponseEntity r = new ResponseEntity(products, HttpStatus.OK);
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC,"id"));

        when(productRepository.findAll(pageable)).thenReturn(products);

        ResponseEntity result = productServiceImpl.allProducts(pageable);

        Assertions.assertEquals(r, result);
    }

    @Test
    void testExceptionThrownWhenProductNameIsAbsent(){
        Product testProduct = Product.builder()
                .productPrice(BigDecimal.ONE)
                .build();

        Assertions.assertThrows(BadRequestException.class, () -> {
            productServiceImpl.addProduct(testProduct);
        });
    }

    @Test
    void testExceptionThrownWhenProductPriceIsAbsent(){
        Product testProduct = Product.builder()
                .productName("test")
                .build();

        Assertions.assertThrows(BadRequestException.class, () -> {
            productServiceImpl.addProduct(testProduct);
        });
    }
}
