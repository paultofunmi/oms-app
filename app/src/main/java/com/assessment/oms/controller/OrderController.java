package com.assessment.oms.controller;

import com.assessment.oms.dto.request.OrderRequestDTO;
import com.assessment.oms.exception.BadRequestException;
import com.assessment.oms.model.Order;
import com.assessment.oms.model.Product;
import com.assessment.oms.service.OrderService;
import com.assessment.oms.service.ProductService;
import com.assessment.oms.utils.CreatePageable;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@Api(value="Order Controller")
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    private static Logger logger = Logger.getLogger("OrderControllerLogger");

    /**
     * Retrieves all orders within a range and sorted using page, size and sort
     *
     * @param startDate
     * @param stopDate
     * @param page
     * @param size
     * @param sort
     * @return ResponseEntity
     */
    @GetMapping(value = "/start/{startDate}/stop/{stopDate}")
    @ApiOperation(value = "All orders within a range", response = Order.class,
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
                    value = "Sorting criteria in the format: property(,ASC|DESC). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria not supported.")
    })
    public ResponseEntity ordersWithinARange(
                @ApiParam(value = "Start date (dd-MM-yyyy) ", required = true) @PathVariable("startDate") String startDate,
                @ApiParam(value = "Stop date (dd-MM-yyyy) ", required = true) @PathVariable("stopDate") String stopDate,
                @RequestParam(required = false, defaultValue = "0") Integer page,
                @RequestParam(required = false, defaultValue = "20") Integer size,
                @RequestParam(value = "sort", defaultValue = "id,ASC", required = false) String sort) {
            try{

                Pageable pageable = PageRequest.of(page, size, CreatePageable.createSort(sort));
            return orderService.ordersByStartEndDate(startDate, stopDate, pageable);
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

    /**
     * Place an order
     * @param orderRequest
     * @return ResponseEntity
     */
    @PostMapping
    @ApiOperation(value = "Place an Order", response = Order.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Order Created"),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    public ResponseEntity placeOrder(@ApiParam(value = "Order details to be placed", required = true) @RequestBody OrderRequestDTO orderRequest) {
        try{
            return orderService.placeOrder(orderRequest);
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

    @GetMapping("/{id}")
    @ApiOperation(value = "View an order", response = Order.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Order Returned"),
            @ApiResponse(code = 404, message = "No order found")
    })
    public ResponseEntity viewAnOrder(@ApiParam(value = "Id of order to be views", required = true) @PathVariable("id") Long id) {
        try{
            return orderService.viewOrder(id);
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
