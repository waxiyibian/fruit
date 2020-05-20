package com.jiayi.fruit.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.OrderDetail;
import com.jiayi.fruit.entity.OrderMaster;
import com.jiayi.fruit.entity.StoreRequest;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.entity.UserAddress;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.exception.IllegalParameterException;
import com.jiayi.fruit.exception.PermissionDeniedException;
import com.jiayi.fruit.exception.UnAuthorizedException;
import com.jiayi.fruit.exception.UnknownUserException;
import com.jiayi.fruit.form.UserSaveForm;
import com.jiayi.fruit.repository.OrderDetailRepository;
import com.jiayi.fruit.repository.OrderMasterRepository;
import com.jiayi.fruit.repository.StoreRequestRepository;
import com.jiayi.fruit.repository.SysUserRepository;
import com.jiayi.fruit.repository.UserAddressRepository;
import com.jiayi.fruit.util.CommonUtil;
import com.jiayi.fruit.util.SecurityUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private Gson gson;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserRepository repository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private StoreRequestRepository storeRequestRepository;

    @RequestMapping("/save")
    public String getUserInfo(UserSaveForm user) {
        String pwd = user.getPassword();
        // 密码为空则不更新，不为空则对密码进行加密
        if(StringUtils.isEmpty(pwd)) user.setPassword(null);
        else user.setPassword(passwordEncoder.encode(pwd));
        // 获取当前用户
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SysUser u = (SysUser) auth.getPrincipal();
        if(u==null) throw new UnAuthorizedException();//未登录
        u = repository.findById(user.getId()).orElse(null);
        //操作的不是当前用户
        if(u.getId()!=user.getId()) throw new PermissionDeniedException();
        BeanUtils.copyProperties(user,u,CommonUtil.getNullPropertyNames(user));
        repository.save(u);
        //保存
        return gson.toJson(Result.success());
    }

    @PostMapping(value="/submitOrder")
    @Transactional
    public String postMethodName(@RequestBody List<OrderMaster> list){
        ResponseFormat rep = Result.success();
        Long userId = SecurityUtil.getUser().getId();
        for(OrderMaster order : list){
            order.setBuyerId(userId);
            List<OrderDetail> ods = order.getOrderDetailList();
            order.setOrderDetailList(null);
            OrderMaster newo = orderMasterRepository.save(order);
            for(OrderDetail od : ods){
                od.setOrderId(newo.getOrderId());
                orderDetailRepository.save(od);
            }
        }
        return gson.toJson(rep);
    }

    @RequestMapping("/storeRequest")
    public String storeRequest(StoreRequest req){
        req.setId(null);
        Long userId = SecurityUtil.getUser().getId();
        req.setUserId(userId);
        storeRequestRepository.save(req);
        return gson.toJson(Result.success());
    }
    
}