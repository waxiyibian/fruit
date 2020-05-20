package com.jiayi.fruit.entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Data;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
// @IdClass(DeliveryStoreKeys.class)
public class DeliveryStore{
    @EmbeddedId DeliveryStoreKeys id;

    @JoinColumn(name="sys_user_id")
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private SysUser user;

    @JoinColumn(name="store_info_id")
    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    private StoreInfo store;
    private Date createTime;
}