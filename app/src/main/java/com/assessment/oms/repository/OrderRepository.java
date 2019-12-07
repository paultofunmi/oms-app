package com.assessment.oms.repository;

import com.assessment.oms.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByDateOfOrderGreaterThanEqualAndDateOfOrderLessThanEqual(Date start, Date stop, Pageable pageable);
}
