package com.jiayi.fruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.jiayi.fruit.entity.DeliveryStore;
import com.jiayi.fruit.entity.DeliveryStoreKeys;

/**
 * Created by wwd
 */
public interface DeliveryStoreRepository extends JpaSpecificationExecutor<DeliveryStore>,JpaRepository<DeliveryStore, DeliveryStoreKeys> {
  @Query(value="delete from delivery_store where user_id=?",nativeQuery = true)
  @Modifying
  Integer deleteByUserId(Long userId); 

  @Query(value="select * from delivery_store where user_id=?",nativeQuery = true)
  List<DeliveryStore> findByUserId(Long userId);
}
