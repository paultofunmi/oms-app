package com.assessment.oms.serviceImpl;

import com.assessment.oms.exception.BadRequestException;
import com.assessment.oms.exception.NotFoundException;
import com.assessment.oms.model.Product;
import com.assessment.oms.repository.ProductRepository;
import com.assessment.oms.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;


    @Override
    public ResponseEntity addProduct(Product product) {
        List<String> errors = new ArrayList();

        if(product.getProductName() == null){
            errors.add("Product Name is required when adding a product");
        }

        if(product.getProductPrice() == null){
            errors.add("Product price is required when adding a product");
        }else {
            if(product.getProductPrice().compareTo(BigDecimal.ZERO) < 0) {
                errors.add("Product price cannot be less than 0");
            }
        }


        if(errors.size() > 0){
            Logger.getLogger("PSI").log(Level.SEVERE, Arrays.toString(errors.toArray()));
            throw new BadRequestException(errors, "Validation Errors: Please check your input");
        }else {
            Product savedProduct = productRepository.save(product);
            return new ResponseEntity(savedProduct, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity updateProduct(Product p, Long id) {
        List<String> errors = new ArrayList();

        Optional<Product> optProduct = productRepository.findById(id);
        Product foundProduct = null;

        if(!optProduct.isPresent()){
            errors.add("No product found with specified id");
        }else {
            foundProduct = optProduct.get();
            if(p.getProductName() != null){
                foundProduct.setProductName(p.getProductName());
            }

            if(p.getProductPrice() != null){
                if(p.getProductPrice().compareTo(BigDecimal.ZERO) < 0){
                    errors.add("Product price cannot be less than 0");
                }else {
                    foundProduct.setProductPrice(p.getProductPrice());
                }
            }
        }


        if(errors.size() > 0){
            Logger.getLogger("OSI").log(Level.SEVERE, Arrays.toString(errors.toArray()));
            throw new BadRequestException(errors, "Validation Errors: Please check your input");
        }else {
            Product updatedProduct = productRepository.save(foundProduct);
            return ResponseEntity.ok(updatedProduct);
        }
    }

    @Override
    public ResponseEntity allProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);

        return ResponseEntity.ok(products);
    }

    @Override
    public ResponseEntity deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        Product foundProduct = null;

        if(optionalProduct.isPresent()){
            foundProduct = optionalProduct.get();
            productRepository.delete(foundProduct);
            return new ResponseEntity("Product Deleted", HttpStatus.NO_CONTENT);
        }else {
           throw new NotFoundException(Arrays.asList("No product with specified id found"), "User error");
        }
    }
}
