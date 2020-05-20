package com.jiayi.fruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.jiayi.fruit.entity.OrderDetail;

/**
 * Created by wwd
 */
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderId(String orderId);
}
