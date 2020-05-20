package com.jiayi.fruit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.jiayi.fruit.entity.Comment;

/**
 * Created by wwd
 */
public interface CommentRepository extends JpaSpecificationExecutor<Comment>,JpaRepository<Comment, Long> {
  @Query(value="select * from comment where user_id=? and order_id=?",nativeQuery = true)
  Comment findByUserIdAndOrderId(Long userId,Long orderId);

  @Modifying
  Integer deleteByUserId(Long userId);
}
