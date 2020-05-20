package com.jiayi.fruit.controller;

import java.util.List;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.ProductInfo;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.repository.ProductInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/product")
public class AdminProductController {
  @Autowired
  private Gson gson;
  @Autowired
  private ProductInfoRepository productInfoRepository;

  @RequestMapping("/findAll")
  public String findAll(){
    ResponseFormat rep = Result.success();
    List<ProductInfo> list = productInfoRepository.findAll();
    rep.setData(list);
    return gson.toJson(rep);
  }
}