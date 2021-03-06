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
public class RecommendType{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Date createTime;
    private Date updateTime;
}