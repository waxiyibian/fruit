package com.jiayi.fruit.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import com.jiayi.fruit.entity.StoreInfo;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.repository.StoreInfoRepository;
import com.jiayi.fruit.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    private StoreInfoRepository repository;

    @Override
    public Long getCurrentStoreId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            SysUser user = (SysUser) auth.getPrincipal();
            if (user != null) {
                return repository.findByOwnerId(user.getId()).getId();
            }
        }
        return null;
    }

    @Override
    public Page<StoreInfo> getList(String keyword, Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Order.desc("updateTime"));
        PageRequest request = PageRequest.of(page-1, size, sort);//new PageRequest(page - 1, size, sort);
        Page<StoreInfo> lists = repository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p=null;
            //equal 示例
            if (keyword!=null && !StringUtils.isEmpty(keyword)){
                p = cb.like(root.get("storeName"),"%"+keyword+"%");
                p = cb.or(p,cb.like(root.get("address"),"%"+keyword+"%"));
            }
            if(p!=null) predicates.add(p);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, request);
        return lists;
    }
    
}