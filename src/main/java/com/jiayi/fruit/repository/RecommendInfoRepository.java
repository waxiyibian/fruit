package com.jiayi.fruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.jiayi.fruit.entity.RecommendInfo;

/**
 * Created by wwd
 */
public interface RecommendInfoRepository extends JpaSpecificationExecutor<RecommendInfo>,JpaRepository<RecommendInfo, Long> {
    List<RecommendInfo> findByTypeId(Long typeId);

    @Query(value="select * from recommend_info where product_id=?",nativeQuery = true)
    List<RecommendInfo> findByProductId(Long productId);
}
