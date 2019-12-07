package com.assessment.oms.controller;

import com.assessment.oms.exception.BadRequestException;
import com.assessment.oms.model.Product;
import com.assessment.oms.service.ProductService;
import com.assessment.oms.utils.CreatePageable;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@Api(value="Product Controller")
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    ProductService productService;

    private static Logger logger = Logger.getLogger("ProductControllerLogger");

    @GetMapping
    @ApiOperation(value = "All products",
                    response = Product.class,
                    responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returned response")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "int", paramType = "query",
                    value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "int", paramType = "query",
                    value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,ASC|DESC). E.g. id,ASC" +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria not supported.")
    })
    public ResponseEntity allProducts(@RequestParam(required = false, defaultValue = "0") Integer page,
                                      @RequestParam(required = false, defaultValue = "20") Integer size,
                                      @RequestParam(value = "sort", defaultValue = "id,ASC", required = false) String sort) {
        try{
            Sort sortOrderIgnoreCase = CreatePageable.createSort(sort);

            Pageable pageable = PageRequest.of(Integer.valueOf(page), Integer.valueOf(size), sortOrderIgnoreCase);
            return productService.allProducts(pageable);
        }catch (BadRequestException ex){
            logger.log(Level.SEVERE, "----BadRequestException Occurred----");
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }catch (Exception ex){
            logger.log(Level.SEVERE, "----Exception Occurred----");
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }
    }

    @PostMapping
    @ApiOperation(value = "Add a Product", response = ResponseEntity.class)
    public ResponseEntity addProduct(@ApiParam(value = "Product to be added", required = true) @RequestBody Product product) {
        try{
            return productService.addProduct(product);
        }catch (BadRequestException ex){
            logger.log(Level.SEVERE, "----BadRequestException Occurred----");
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }catch (Exception ex){
            logger.log(Level.SEVERE, "----Exception Occurred----");
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }
    }

    @PutMapping(value = "{id}")
    @ApiOperation(value = "Update a product", response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product Updated"),
            @ApiResponse(code = 400, message = "No product with specified id found") })
    public ResponseEntity updateProduct(@ApiParam(value = "Id of Product to be updated", required = true) @PathVariable(value = "id") Long id,
                                        @ApiParam(value = "Product to be updated", required = true) @RequestBody Product product) {
        try{
            return productService.updateProduct(product, id);
        }catch (BadRequestException ex){
            logger.log(Level.SEVERE, "----BadRequestException Occurred----");
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }catch (Exception ex){
            logger.log(Level.SEVERE, "----Exception Occurred----");
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }
    }


    @DeleteMapping(value = "{id}")
    @ApiOperation(value = "Delete a product", response = Product.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Product Deleted"),
            @ApiResponse(code = 400, message = "No product with specified id found") })
    public ResponseEntity deleteProduct(@ApiParam(value = "Id of Product to be deleted", required = true) @PathVariable(value = "id") Long id) {
        try{
            return productService.deleteProduct(id);
        }catch (BadRequestException ex){
            logger.log(Level.SEVERE, "----BadRequestException Occurred----");
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }catch (Exception ex){
            logger.log(Level.SEVERE, "----Exception Occurred----");
            logger.log(Level.SEVERE, ex.getMessage());
            throw ex;
        }
    }
}
