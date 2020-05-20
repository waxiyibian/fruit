package com.jiayi.fruit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.DeliveryStore;
import com.jiayi.fruit.entity.DeliveryStoreKeys;
import com.jiayi.fruit.entity.StoreInfo;
import com.jiayi.fruit.entity.SysRole;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/delivery")
public class AdminDeliveryController {
    @Autowired
    private Gson gson;
    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private StoreInfoRepository storeRepository;
    @Autowired
    private DeliveryStoreRepository deliveryStoreRepositoy;
    @Autowired
    private SysRoleRepository sysRoleRepository;
    @Autowired
    private SysUserRepository sysUserRepository;

    @RequestMapping("/list")
    public String list(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("createTime"));
        PageRequest request = PageRequest.of(page - 1, size, sort);// new PageRequest(page - 1, size, sort);
        Page<DeliveryStore> lists = deliveryStoreRepositoy.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p = null;
            // equal 示例
            if (keyword != null && !StringUtils.isEmpty(keyword)) {
                Join<DeliveryStore, SysUser> user = root.join("user", JoinType.LEFT);
                Join<DeliveryStore, StoreInfo> store = root.join("store", JoinType.LEFT);
                p = cb.like(user.get("id").as(String.class), "%" + keyword + "%");
                p = cb.or(p, cb.like(store.get("id").as(String.class), "%" + keyword + "%"));
                p = cb.or(p, cb.like(user.get("nickname"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(user.get("username"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(user.get("phone"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(store.get("ownerId").as(String.class), "%" + keyword + "%"));
                p = cb.or(p, cb.like(store.get("storeName"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(store.get("address"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(store.get("description"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(store.get("phone"), "%" + keyword + "%"));
            }
            if (p != null){
                predicates.add(p);
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, request);
        List<DeliveryStore> list = lists.getContent();
        ResponseFormat rep = Result.success();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total_num", lists.getTotalElements());
        map.put("page_num", page);
        map.put("page_size", size);
        map.put("tableData", list);
        rep.setData(map);
        return gson.toJson(rep);
    }

    @RequestMapping("/save")
    @Transactional
    public String finish(@RequestBody List<DeliveryStoreKeys> keys) {
        ResponseFormat rep = Result.success();
        SysUser u;
        if(keys!=null&&keys.size()>0){
            Long firstUserId = keys.get(0).getUserId();
            u = userRepository.findById(firstUserId).orElse(null);
            if(u==null){
                throw new IllegalParameterException(){{setMessage("配送员账号不存在");}};
            }
            //如果用户没有配送员角色，则进行添加
            if(!SecurityUtil.hasRole(u, RoleName.DELIVERY)){
                u.getRoles().add(sysRoleRepository.findByName(RoleName.DELIVERY));
                sysUserRepository.save(u);
            }
            //获取配送员和商店的对应关系列表
            List<DeliveryStore> deliveryStores = deliveryStoreRepositoy.findByUserId(firstUserId);
            for(DeliveryStore ds : deliveryStores){
                //如果keys中不包含原来的列表中的对应关系，则原来的对应关系要删除
                if(keys.indexOf(ds.getId())<0) deliveryStoreRepositoy.deleteById(ds.getId());
            }
            for(DeliveryStoreKeys key : keys){
                if(!firstUserId.equals(key.getUserId())){
                    throw new IllegalParameterException(){{setMessage("一次只能操作一个配送员");}}; 
                }
                Long storeId = key.getStoreId();
                StoreInfo olds = storeRepository.findById(storeId).orElse(null);
                if(olds==null){
                    throw new IllegalParameterException(){{setMessage("ID"+storeId+"的店铺不存在");}};
                }
                DeliveryStore ds = new DeliveryStore();
                DeliveryStoreKeys dsk = new DeliveryStoreKeys();
                dsk.setStoreId(storeId);
                dsk.setUserId(firstUserId);
                ds.setId(dsk);
                ds.setUser(u);
                ds.setStore(olds);
                deliveryStoreRepositoy.save(ds);
            }
        }
        return gson.toJson(rep);
    }

    @RequestMapping("/delete")
    @Transactional
    public String cancel(Long userId) {
        ResponseFormat rep = Result.success();
        SysUser u = userRepository.findById(userId).orElse(null);
        if(u==null){
            throw new IllegalParameterException(){{setMessage("配送员账号不存在");}};
        }
        boolean hasRole = false;
        for(SysRole sr : u.getRoles()){
            if(sr.getName().equals(RoleName.DELIVERY)){
                u.getRoles().remove(sr);
                hasRole = true;
                break;
            }
        }
        if(hasRole) userRepository.save(u);
        deliveryStoreRepositoy.deleteByUserId(userId);
        return gson.toJson(rep);
    }

}