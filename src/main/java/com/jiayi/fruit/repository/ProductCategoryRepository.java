package com.jiayi.fruit.repository;

import java.util.List;

import com.jiayi.fruit.entity.ProductCategory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    List<ProductCategory> findByStoreId(Long storeId);
}