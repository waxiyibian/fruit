package com.jiayi.fruit.controller;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.UserAddress;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.repository.UserAddressRepository;
import com.jiayi.fruit.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/address")
public class UserAddressController {
  @Autowired
  private Gson gson;
  @Autowired
  private UserAddressRepository userAddressRepository;
  
  @RequestMapping("/save")
    public String addressSave(@Validated UserAddress address){
        ResponseFormat rep = Result.success();
        address.setUserId(SecurityUtil.getUser().getId());
        userAddressRepository.save(address);
        return gson.toJson(rep);
    }

    @RequestMapping("/delete")
    public String addressDelete(Long id){
        ResponseFormat rep = Result.success();
        userAddressRepository.deleteById(id);
        return gson.toJson(rep);
    }

    @RequestMapping("/list")
    public String addressList(){
        ResponseFormat rep = Result.success();
        Long userId = SecurityUtil.getUser().getId();
        rep.setData(userAddressRepository.findByUserId(userId));
        return gson.toJson(rep);
    }
}