package com.jiayi.fruit.repository;

import java.util.List;

import com.jiayi.fruit.entity.ProductInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductInfoRepository extends JpaSpecificationExecutor<ProductInfo>,JpaRepository<ProductInfo, Long> {
    List<ProductInfo> findByCategoryIdAndStoreId(Long categoryId,Long storeId);
}