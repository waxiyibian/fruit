package com.jiayi.fruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jiayi.fruit.entity.OrderMaster;

/**
 * Created by wwd
 */
public interface OrderMasterRepository extends JpaSpecificationExecutor<OrderMaster>,JpaRepository<OrderMaster, Long> {
    OrderMaster findByOrderId(Long orderId);

    @Query(value="select max(order_id) from order_master",nativeQuery = true)
    Long findMaxId();

    @Modifying
    Integer deleteByBuyerId(Long buyerId);
}
