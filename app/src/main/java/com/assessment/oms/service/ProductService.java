package com.assessment.oms.service;

import com.assessment.oms.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;


public interface ProductService {
    ResponseEntity addProduct(Product p);
    ResponseEntity updateProduct(Product p, Long id);
    ResponseEntity allProducts(Pageable pageable);
    ResponseEntity deleteProduct(Long id);
}
