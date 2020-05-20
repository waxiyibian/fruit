package com.jiayi.fruit.repository;

import com.jiayi.fruit.entity.StoreInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

public interface StoreInfoRepository extends JpaSpecificationExecutor<StoreInfo>,JpaRepository<StoreInfo, Long> {
    StoreInfo findByOwnerId(Long ownerId);

    @Modifying
    Integer deleteByOwnerId(Long ownerId);
}