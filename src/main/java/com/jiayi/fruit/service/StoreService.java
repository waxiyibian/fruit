package com.jiayi.fruit.service;

import com.jiayi.fruit.entity.StoreInfo;

import org.springframework.data.domain.Page;

public interface StoreService{
    Long getCurrentStoreId();
    Page<StoreInfo> getList(String keyword,Integer page_num,Integer page_size);
}