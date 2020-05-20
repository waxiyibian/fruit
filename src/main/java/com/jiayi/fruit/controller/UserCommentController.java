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
import com.google.gson.JsonObject;
import com.jiayi.fruit.entity.Comment;
import com.jiayi.fruit.entity.OrderMaster;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.env.OrderStatus;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.exception.IllegalParameterException;
import com.jiayi.fruit.exception.PermissionDeniedException;
import com.jiayi.fruit.repository.CommentRepository;
import com.jiayi.fruit.repository.OrderMasterRepository;
import com.jiayi.fruit.repository.SysUserRepository;
import com.jiayi.fruit.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/comment")
public class UserCommentController {
  @Autowired
  private Gson gson;
  @Autowired
  private OrderMasterRepository orderMasterRepository;
  @Autowired
  private SysUserRepository sysUserRepository;
  @Autowired
  private CommentRepository commentRepository;

  @RequestMapping("/list")
    public String list(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("updateTime"));
        PageRequest request = PageRequest.of(page - 1, size, sort);// new PageRequest(page - 1, size, sort);
        Page<Comment> lists = commentRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p = null;
            Long userId = SecurityUtil.getUser().getId();
            Join<Comment, SysUser> user = root.join("user", JoinType.LEFT);
            Predicate pr = cb.equal(user.get("id"), userId);
            // equal 示例
            if (!StringUtils.isEmpty(keyword)) {
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

  @RequestMapping("/find")
  public String find(Long orderId){
    Long userId = SecurityUtil.getUser().getId();
    Comment comment = commentRepository.findByUserIdAndOrderId(userId, orderId);
    ResponseFormat rep = Result.success();
    JsonObject json = null;
    if(comment!=null){
      json = new JsonObject();
      json.addProperty("orderId", comment.getOrder().getOrderId());
      json.addProperty("content", comment.getContent());
      json.addProperty("userId", userId);
      json.addProperty("storeId", comment.getStoreId());
    }
    rep.setData(json);
    return gson.toJson(rep);
  }

  @RequestMapping("/save")
  @Transactional
  public String save(Long orderId,String content){
    Long userId = SecurityUtil.getUser().getId();
    SysUser user = sysUserRepository.findById(userId).orElse(null);
    OrderMaster order = orderMasterRepository.findByOrderId(orderId);
    Comment cmt = commentRepository.findByUserIdAndOrderId(userId, orderId);
    if(cmt!=null){
      throw new IllegalParameterException(){{setMessage("您已评价过改订单");}};
    }
    if(user==null){
      throw new IllegalParameterException(){{setMessage("用户异常");}};
    }
    if(order==null){
      throw new IllegalParameterException(){{setMessage("订单不存在");}};
    }
    if(order.getOrderStatus()!=OrderStatus.FINISHED){
      throw new IllegalParameterException(){{setMessage("订单状态未完成，无法评价");}};
    }
    order.setOrderStatus(OrderStatus.COMMENTED);
    Comment comment = new Comment();
    comment.setUser(user);
    comment.setOrder(order);
    comment.setStoreId(order.getStoreId());
    comment.setContent(content);
    Comment newOne = commentRepository.save(comment);
    if(newOne.getOrder().getOrderStatus()!=OrderStatus.COMMENTED){
      orderMasterRepository.save(order);
    }
    return gson.toJson(Result.success());
  }

  @RequestMapping("/delete")
  @Transactional
  public String delete(Long id){
    Long userId = SecurityUtil.getUser().getId();
    Comment comment = commentRepository.findById(id).orElse(null);
    if(comment==null){
      throw new IllegalParameterException(){{setMessage("评论不存在");}};
    }
    if(!userId.equals( comment.getUser().getId() ) ){
      throw new PermissionDeniedException();
    }
    OrderMaster order = comment.getOrder();
    order.setOrderStatus(OrderStatus.FINISHED);
    orderMasterRepository.save(order);
    commentRepository.deleteById(id);

    return gson.toJson(Result.success());
  }
}