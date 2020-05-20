package com.jiayi.fruit.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class Comment{
    @Id
    @GeneratedValue
    private Long id;
    //private Long userId;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName="id",nullable=false)
    private SysUser user;
    private String content;
    private Date createTime;
    private Date updateTime;
    private Long storeId;
    //private Long orderId;
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name="order_id",nullable=false)
    private OrderMaster order;
}