package com.jiayi.fruit.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class StoreInfo{
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private Long ownerId;
    private String storeName;
    private String description;
    private String address;
    private String phone;
    private String logo;
    private BigDecimal minCost;
    private BigDecimal postFee;
    private Date createTime;
    private Date updateTime;
}