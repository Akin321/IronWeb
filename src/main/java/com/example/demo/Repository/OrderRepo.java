package com.example.demo.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.demo.model.NewUserModel;
import com.example.demo.model.OrderModel;

public interface OrderRepo extends JpaRepository<OrderModel,Integer>, JpaSpecificationExecutor<OrderModel>{

	List<OrderModel> findByUser(NewUserModel user);

	Page<OrderModel> findByOrderIdContainingIgnoreCaseOrUser_EmailContainingIgnoreCase(String keyword, String keyword2,
			Pageable pageable);

	Page<OrderModel> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

	List<OrderModel> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

}
