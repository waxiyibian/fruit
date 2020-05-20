package com.jiayi.fruit.controller;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.DeliveryStore;
import com.jiayi.fruit.entity.DeliveryStoreKeys;
import com.jiayi.fruit.entity.StoreInfo;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.env.RoleName;
import com.jiayi.fruit.exception.IllegalParameterException;
import com.jiayi.fruit.repository.DeliveryStoreRepository;
import com.jiayi.fruit.repository.StoreInfoRepository;
import com.jiayi.fruit.repository.SysRoleRepository;
import com.jiayi.fruit.repository.SysUserRepository;
import com.jiayi.fruit.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
  @Autowired
  private Gson gson;
  @Autowired
  private DeliveryStoreRepository deliveryStoreRepositoy;
  @Autowired
  private SysUserRepository userRepository;
  @Autowired
  private SysRoleRepository sysRoleRepository;
  @Autowired
  private StoreInfoRepository storeRepository;

  @RequestMapping("/get")
  public String get() {
    Long userId = SecurityUtil.getUser().getId();
    List<DeliveryStore> lists = deliveryStoreRepositoy.findAll((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<Predicate>();
      Join<DeliveryStore, SysUser> user = root.join("user", JoinType.LEFT);
      Predicate p = cb.equal(user.get("id"), userId);
      // equal 示例
      if (p != null) {
        predicates.add(p);
      }
      return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
    });
    ResponseFormat rep = Result.success();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("total_num", 1);
    map.put("page_num", 1);
    map.put("page_size", 1);
    map.put("tableData", lists);
    rep.setData(map);
    return gson.toJson(rep);
  }

  @RequestMapping("/save")
  @Transactional
  public String save(@RequestBody List<DeliveryStoreKeys> keys) {
    ResponseFormat rep = Result.success();
    Long userId = SecurityUtil.getUser().getId();
    SysUser u;
    if (keys != null && keys.size() > 0) {
      u = userRepository.findById(userId).orElse(null);
      if (u == null) {
        throw new IllegalParameterException() {
          {
            setMessage("配送员账号不存在");
          }
        };
      }
      // 如果用户没有配送员角色，则进行添加
      if (!SecurityUtil.hasRole(u, RoleName.DELIVERY)) {
        u.getRoles().add(sysRoleRepository.findByName(RoleName.DELIVERY));
        userRepository.save(u);
      }
      // 获取配送员和商店的对应关系列表
      List<DeliveryStore> deliveryStores = deliveryStoreRepositoy.findByUserId(userId);
      for (DeliveryStore ds : deliveryStores) {
        // 如果keys中不包含原来的列表中的对应关系，则原来的对应关系要删除
        if (keys.indexOf(ds.getId()) < 0)
          deliveryStoreRepositoy.deleteById(ds.getId());
      }
      for (DeliveryStoreKeys key : keys) {
        key.setUserId(userId);
        Long storeId = key.getStoreId();
        StoreInfo olds = storeRepository.findById(storeId).orElse(null);
        if (olds == null) {
          throw new IllegalParameterException() {
            {
              setMessage("ID" + storeId + "的店铺不存在");
            }
          };
        }
        DeliveryStore ds = new DeliveryStore();
        DeliveryStoreKeys dsk = new DeliveryStoreKeys();
        dsk.setStoreId(storeId);
        dsk.setUserId(userId);
        ds.setId(dsk);
        ds.setUser(u);
        ds.setStore(olds);
        deliveryStoreRepositoy.save(ds);
      }
    }
    return gson.toJson(rep);
  }
}