package com.jiayi.fruit.service.impl;

import com.jiayi.fruit.repository.CommentRepository;
import com.jiayi.fruit.repository.DeliveryStoreRepository;
import com.jiayi.fruit.repository.OrderMasterRepository;
import com.jiayi.fruit.repository.StoreInfoRepository;
import com.jiayi.fruit.repository.SysUserRepository;
import com.jiayi.fruit.repository.UserAddressRepository;
import com.jiayi.fruit.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService{

  @Autowired
  private CommentRepository commentRepository;
  @Autowired
  private SysUserRepository sysUserRepository;
  @Autowired
  private OrderMasterRepository orderMasterRepository;
  @Autowired
  private StoreInfoRepository storeInfoRepository;
  @Autowired
  private UserAddressRepository userAddressRepository;
  @Autowired
  private DeliveryStoreRepository deliveryStoreRepository;
  

  @Override
  @Transactional
  public int deleteAllById(Long id) {
    int n = 0;
    //删除评论
    n += commentRepository.deleteByUserId(id);
    //删除订单
    n += orderMasterRepository.deleteByBuyerId(id);
    //删除地址
    n += userAddressRepository.deleteByUserId(id);
    //删除配送关系
    n += deliveryStoreRepository.deleteByUserId(id);
    //删除店铺
    n += storeInfoRepository.deleteByOwnerId(id);
    //删除用户
    sysUserRepository.deleteById(id);
    return n;
  }
  
}