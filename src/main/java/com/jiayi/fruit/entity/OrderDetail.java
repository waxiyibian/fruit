package com.jiayi.fruit.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wwd
 */
@Entity
@Data
@DynamicInsert
@DynamicUpdate
public class OrderDetail {

    @Id
    @GeneratedValue
    private Long detailId;

    /** 订单id. */
    private Long orderId;

    /** 商品id. */
    private Long productId;

    /** 商品名称. */
    private String productName;

    /** 商品单价. */
    private BigDecimal productPrice;

    /** 商品数量. */
    private Integer productQuantity;

    /** 商品小图. */
    private String productIcon;

    private Date createTime;
    
    private Date updateTime;
}
