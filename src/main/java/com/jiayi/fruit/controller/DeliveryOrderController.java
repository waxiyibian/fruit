package com.jiayi.fruit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.DeliveryStore;
import com.jiayi.fruit.entity.OrderMaster;
import com.jiayi.fruit.env.OrderStatus;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.exception.IllegalParameterException;
import com.jiayi.fruit.exception.OperationNotAllowException;
import com.jiayi.fruit.exception.PermissionDeniedException;
import com.jiayi.fruit.exception.RecordNotFoundException;
import com.jiayi.fruit.repository.DeliveryStoreRepository;
import com.jiayi.fruit.repository.OrderMasterRepository;
import com.jiayi.fruit.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/delivery/order")
public class DeliveryOrderController {

  @Autowired
  private Gson gson;
  @Autowired
  private OrderMasterRepository orderMasterRepository;
  @Autowired
  private DeliveryStoreRepository deliveryStoreRepository;

  @RequestMapping("/list")
  public String list(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
      @RequestParam(value = "page_size", defaultValue = "10") Integer size,
      @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
      @RequestParam(value = "status", required = false, defaultValue = "-1") Integer status) {
    Sort sort = Sort.by(Sort.Order.desc("updateTime"));
    PageRequest request = PageRequest.of(page - 1, size, sort);// new PageRequest(page - 1, size, sort);
    Long userId = SecurityUtil.getUser().getId();
    List<DeliveryStore> list = deliveryStoreRepository.findByUserId(userId);
    List<Long> storeIds = new ArrayList<>();
    for(DeliveryStore ds : list){storeIds.add(ds.getId().getStoreId());};
    Page<OrderMaster> lists = orderMasterRepository.findAll((root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<Predicate>();
      Predicate p = null;
      // equal 示例
      Predicate pr = root.get("storeId").in(storeIds);
      if (keyword != null && !StringUtils.isEmpty(keyword)) {
        p = cb.like(root.get("orderId").as(String.class), "%" + keyword + "%");
        p = cb.or(p, cb.like(root.get("buyerId").as(String.class), "%" + keyword + "%"));
        p = cb.or(p, cb.like(root.get("buyerName"), "%" + keyword + "%"));
        p = cb.or(p, cb.like(root.get("buyerPhone"), "%" + keyword + "%"));
      }
      if (status != null && status >= 0) {
        if (p == null)
          p = cb.equal(root.get("orderStatus"), status);
        else
          p = cb.and(p, cb.equal(root.get("orderStatus"), status));
      }
      if (p != null) {
        p = cb.and(pr, p);
      } else {
        p = pr;
      }
      predicates.add(p);
      return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
    }, request);
    List<OrderMaster> lt = lists.getContent();
    ResponseFormat rep = Result.success();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("total_num", lists.getTotalElements());
    map.put("page_num", page);
    map.put("page_size", size);
    map.put("tableData", lt);

    rep.setData(map);
    return gson.toJson(rep);
  }

  private void setStatus(Long orderId,Integer status){
    OrderMaster order = orderMasterRepository.findByOrderId(orderId);
    if(order==null){
        throw new RecordNotFoundException();
    }
    if(status==OrderStatus.DELIVERY){
      Long userId = SecurityUtil.getUser().getId();
      List<DeliveryStore> list = deliveryStoreRepository.findByUserId(userId);
      boolean hasRight = false;
      for(DeliveryStore ds: list){
        if( order.getStoreId().equals(ds.getId().getStoreId() )){
          hasRight = true;
          break;
        }
      }
      if(!hasRight){
        throw new IllegalParameterException(){{setMessage("当前配送员并未与订单中的商店建立配送关系");}};
      }
      if(status==null||order.getOrderStatus()>=status){
          throw new OperationNotAllowException();
      }
    }else{
      throw new PermissionDeniedException();
    }
    order.setOrderStatus(status);
    orderMasterRepository.save(order);
  }

  @RequestMapping("/confirmDelivery")
  public String confirmDelivery(@RequestParam("orderId") Long orderId) {
    setStatus(orderId, OrderStatus.DELIVERY);
    ResponseFormat rep = Result.success();
    return gson.toJson(rep);
  }

}