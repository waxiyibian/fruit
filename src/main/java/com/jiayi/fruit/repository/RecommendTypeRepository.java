package com.jiayi.fruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

import com.jiayi.fruit.entity.RecommendInfo;
import com.jiayi.fruit.entity.RecommendType;

/**
 * Created by wwd
 */
public interface RecommendTypeRepository extends JpaSpecificationExecutor<RecommendType>,JpaRepository<RecommendType,Long> {
    
}
