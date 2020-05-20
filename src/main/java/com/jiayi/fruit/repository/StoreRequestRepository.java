package com.jiayi.fruit.repository;

import com.jiayi.fruit.entity.StoreRequest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

public interface StoreRequestRepository extends JpaSpecificationExecutor<StoreRequest>,JpaRepository<StoreRequest, Long> {
    StoreRequest findByUserId(Long userId);

    @Modifying
    Integer deleteByUserId(Long userId);
}