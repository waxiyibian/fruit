package com.jiayi.fruit.service;

public interface ProductService{
    Integer onSale(Long productId);
    Integer offSale(Long productId);
}