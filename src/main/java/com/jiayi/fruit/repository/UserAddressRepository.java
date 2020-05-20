package com.jiayi.fruit.repository;

import java.util.List;

import com.jiayi.fruit.entity.UserAddress;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

public interface UserAddressRepository extends JpaSpecificationExecutor<UserAddress>,JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserId(Long userId);

    @Modifying
    Integer deleteByUserId(Long userId);
}