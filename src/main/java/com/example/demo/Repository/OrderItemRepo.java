package com.example.demo.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.model.OrderItem;

public interface OrderItemRepo extends JpaRepository<OrderItem,Integer> ,JpaSpecificationExecutor<OrderItem>{

	List<OrderItem> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

}
