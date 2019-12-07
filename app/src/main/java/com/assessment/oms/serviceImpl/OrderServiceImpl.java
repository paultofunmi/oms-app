package com.assessment.oms.serviceImpl;

import com.assessment.oms.dto.request.OrderItemRequestDTO;
import com.assessment.oms.dto.request.OrderRequestDTO;
import com.assessment.oms.dto.response.OrderResponse;
import com.assessment.oms.exception.BadRequestException;
import com.assessment.oms.exception.NotFoundException;
import com.assessment.oms.model.Order;
import com.assessment.oms.model.OrderItem;
import com.assessment.oms.model.Product;
import com.assessment.oms.repository.OrderRepository;
import com.assessment.oms.repository.ProductRepository;
import com.assessment.oms.service.OrderService;
import com.assessment.oms.utils.DateUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    OrderRepository orderRepository;

    @Override
    @Transactional
    public ResponseEntity placeOrder(OrderRequestDTO orderRequestDTO) {
        List<String> errors = new ArrayList();
        Set<OrderItem> orderItems = new HashSet();
        BigDecimal orderValue = new BigDecimal(0);

        if(orderRequestDTO.getBuyerEmail() == null){
            errors.add("Buyer's email is required when placing an order");
        }

        if(orderRequestDTO.getOrderItems() == null){
            errors.add("Order item field is required when placing an order");
        }else {
            for(OrderItemRequestDTO orderDTO: orderRequestDTO.getOrderItems()){
                Product foundProduct = this.getProduct(orderDTO.getProductId());
                if(foundProduct == null){
                    errors.add("No product found with specified id: " + orderDTO.getProductId());
                }else if (orderDTO.getQuantity() == null || orderDTO.getQuantity() <= 0) {
                    errors.add("Buyer must set an order quantity when ordering a product");
                }else {
                    /**
                     * In real app, I would check and reserve the specified quantity in a thread-safe manner.
                     * Since I am not persisting quantity in the product model, so we assume the quantity in stock is infinite
                     */
                    OrderItem newOrderItem = OrderItem.builder()
                                                .product(foundProduct)
                                                .quantityOrdered(orderDTO.getQuantity())
                                                .productPriceAtOrder(foundProduct.getProductPrice())
                                                .build();
                    BigDecimal prodOrderCalc = foundProduct.getProductPrice().multiply(new BigDecimal(orderDTO.getQuantity()));
                    orderValue = orderValue.add(prodOrderCalc);
                    orderItems.add(newOrderItem);
                }
            }
        }

        if(errors.size() > 0){
            Logger.getLogger("OSI").log(Level.SEVERE, Arrays.toString(errors.toArray()));
            throw new BadRequestException(errors, "Validation Error: Please check your input");
        }else {
           Order newOrder  = Order.builder()
                                    .buyerEmail(orderRequestDTO.getBuyerEmail())
                                    .orderItems(orderItems)
                                    .orderValue(orderValue)
                                    .build();

           Order savedOrder = orderRepository.save(newOrder);

            OrderResponse orderResponse = OrderResponse.builder()
                                                .id(savedOrder.getId())
                                                .buyerEmail(savedOrder.getBuyerEmail())
                                                .dateOfOrder(savedOrder.getDateOfOrder())
                                                .createdAt(savedOrder.getCreatedAt())
                                                .updatedAt(savedOrder.getUpdatedAt())
                                                .orderValue(savedOrder.getOrderValue())
                                                .orderItems(savedOrder.getOrderItems())
                                                .build();

           return new ResponseEntity(orderResponse, HttpStatus.CREATED);
        }
    }

    @Override
    public ResponseEntity ordersByStartEndDate(String start, String end, Pageable pageable) {
        List<String> errors = new ArrayList();
        Date startDate = DateUtil.dateFormater(start);
        Date endDate = DateUtil.dateFormater(end);

        if(startDate == null) {
            errors.add("Date format mismatch (Allowed Date format is: dd-MM-yyyy) for start date");
        }

        if(endDate == null) {
            errors.add("Date format mismatch (Allowed Date format is: dd-MM-yyyy) for start date");
        }

//        Logger.getLogger("OS").log(Level.SEVERE, startDate.toString());
//        Logger.getLogger("OS").log(Level.SEVERE, endDate.toString());

        if(errors.size() > 0){
            Logger.getLogger("OSI").log(Level.SEVERE, Arrays.toString(errors.toArray()));
            throw new BadRequestException(errors, "Validation Error: Please check your input");
        }else {
            Page<Order> orderList = orderRepository.findByDateOfOrderGreaterThanEqualAndDateOfOrderLessThanEqual(startDate, endDate, pageable);
            return ResponseEntity.ok(orderList);
        }
    }

    @Override
    @Transactional
    public ResponseEntity viewOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        Order foundOrder = null;

        if(!optionalOrder.isPresent()){
            throw new NotFoundException(Arrays.asList("No order found with specified id"), "User Error");
        }else {
            foundOrder = optionalOrder.get();
            Set<OrderItem> orderItems = foundOrder.getOrderItems();

            OrderResponse orderResponse = OrderResponse.builder()
                                                .id(foundOrder.getId())
                                                .orderItems(orderItems)
                                                .orderValue(foundOrder.getOrderValue())
                                                .buyerEmail(foundOrder.getBuyerEmail())
                                                .dateOfOrder(foundOrder.getDateOfOrder())
                                                .updatedAt(foundOrder.getUpdatedAt())
                                                .createdAt(foundOrder.getCreatedAt())
                                                .build();

            return new ResponseEntity(orderResponse, HttpStatus.OK);
        }
    }

    private Product getProduct(Long id){
        Optional<Product> optProduct = productRepository.findById(id);

        if(optProduct.isPresent()){
            return optProduct.get();
        }else {
            return null;
        }
    }
}
