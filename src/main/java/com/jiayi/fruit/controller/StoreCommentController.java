package com.jiayi.fruit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.Comment;
import com.jiayi.fruit.entity.OrderMaster;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.repository.CommentRepository;
import com.jiayi.fruit.service.StoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/store/comment")
public class StoreCommentController {
    @Autowired
    private Gson gson;
    @Autowired
    private CommentRepository repository;
    @Autowired
    private StoreService storeService;
    
    @RequestMapping("/list")
    public String list(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("updateTime"));
        PageRequest request = PageRequest.of(page - 1, size, sort);// new PageRequest(page - 1, size, sort);
        Page<Comment> lists = repository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p = null;
            Predicate pr = cb.equal(root.get("storeId"), storeService.getCurrentStoreId());
            // equal 示例
            if (!StringUtils.isEmpty(keyword)) {
                Join<Comment, SysUser> user = root.join("user", JoinType.LEFT);
                Join<Comment, OrderMaster> order = root.join("order", JoinType.LEFT);
                p = cb.like(user.get("id").as(String.class), "%" + keyword + "%");
                p = cb.or(p,cb.like(order.get("orderId").as(String.class), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("id").as(String.class), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("content"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(user.get("username"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(user.get("nickname"), "%" + keyword + "%"));
            }
            if (p != null){
                p = cb.and(pr, p);
            }else{
                p = pr;
            }
            predicates.add(p);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, request);
        List<Map<String,Object>> list = new LinkedList<>();
        for(Comment c : lists.getContent()){
            Map<String,Object> m = new HashMap<>();
            m.put("id", c.getId());
            m.put("content", c.getContent());
            m.put("userId", c.getUser().getId());
            m.put("nickname", c.getUser().getNickname());
            m.put("username", c.getUser().getUsername());
            m.put("phone",c.getUser().getPhone());
            m.put("orderId", c.getOrder().getOrderId());
            m.put("order",c.getOrder());
            m.put("createTime", c.getCreateTime());
            m.put("updateTime", c.getUpdateTime());
            list.add(m);
        }
        ResponseFormat rep = Result.success();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total_num", lists.getTotalElements());
        map.put("page_num", page);
        map.put("page_size", size);
        map.put("tableData", list);
        rep.setData(map);
        return gson.toJson(rep);
    }

}