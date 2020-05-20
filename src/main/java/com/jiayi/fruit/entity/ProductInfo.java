package com.jiayi.fruit.entity;

import java.util.Date;

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
public class ProductInfo{
    @Id
    @GeneratedValue
    private Long id;
    private Long storeId;
    private Long categoryId;

    private String productName;
    private String description;
    private Double price;
    private Long stock;//库存
    private Integer status;//0正常，1下架
    private String imgUrl;
    
    private Date createTime;
    private Date updateTime;

}