package com.jiayi.fruit.service.impl;

import com.jiayi.fruit.entity.ProductInfo;
import com.jiayi.fruit.exception.RecordNotFoundException;
import com.jiayi.fruit.repository.ProductInfoRepository;
import com.jiayi.fruit.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductInfoRepository repository;

    @Override
    public Integer onSale(Long productId) {
        ProductInfo product = repository.findById(productId).orElse(null);
        if(product==null){
            throw new RecordNotFoundException();
        }
        product.setStatus(0);
        repository.save(product);
        return null;
    }

    @Override
    public Integer offSale(Long productId) {
        ProductInfo product = repository.findById(productId).orElse(null);
        if(product==null){
            throw new RecordNotFoundException();
        }
        product.setStatus(1);
        repository.save(product);
        return null;
    }

}