package com.jiayi.fruit.controller;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.ProductCategory;
import com.jiayi.fruit.entity.ProductInfo;
import com.jiayi.fruit.entity.RecommendType;
import com.jiayi.fruit.entity.StoreInfo;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.exception.RecordNotFoundException;
import com.jiayi.fruit.repository.ProductCategoryRepository;
import com.jiayi.fruit.repository.ProductInfoRepository;
import com.jiayi.fruit.repository.RecommendInfoRepository;
import com.jiayi.fruit.repository.RecommendTypeRepository;
import com.jiayi.fruit.repository.StoreInfoRepository;
import com.jiayi.fruit.service.StoreService;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/open/index")
public class OpenIndexController{
    @Autowired
    private Gson gson;
    @Autowired
    private StoreService storeService;
    @Autowired
    private RecommendInfoRepository recommendInfoRepository;
    @Autowired
    private RecommendTypeRepository recommendTypeRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private StoreInfoRepository storeInfoRepository;

    @RequestMapping("/storeList")
    public String getList(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
    @RequestParam(value = "page_size", defaultValue = "10") Integer size,
    @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword){
        ResponseFormat rep = Result.success();
        Page<StoreInfo> list = storeService.getList(keyword, page, size);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total_num", list.getTotalElements());
        map.put("page_num", page);
        map.put("page_size", size);
        map.put("tableData",list.getContent());
        rep.setData(map);
        return gson.toJson(rep);
    }

    @RequestMapping("/getRecommend")
    public String getRecommend(){
        List<RecommendType> list = recommendTypeRepository.findAll();
        List<Map<String,Object>> arr = new LinkedList<>();
        for(RecommendType rt : list){
            BeanMap bm = new BeanMap(rt);
            Map<String,Object> m = new HashMap<>();
            for(Entry et : bm.entrySet()){
                if("class".contentEquals(et.getKey()+"")) continue;
                m.put(et.getKey()+"", et.getValue());
            }
            m.put("list", recommendInfoRepository.findByTypeId(rt.getId()));
            arr.add(m);
        }
        ResponseFormat rep = Result.success();
        rep.setData(arr);
        return gson.toJson(rep);
    }

    @RequestMapping("/getProductList")
    public String getProductList(Long storeId){
        List<ProductCategory> list = productCategoryRepository.findByStoreId(storeId);
        if(list==null) throw new RecordNotFoundException();
        List<Map<String,Object>> arr = new LinkedList<>();
        for(ProductCategory rt : list){
            BeanMap bm = new BeanMap(rt);
            Map<String,Object> m = new HashMap<>();
            for(Entry et : bm.entrySet()){
                if("class".contentEquals(et.getKey()+"")) continue;
                m.put(et.getKey()+"", et.getValue());
            }
            m.put("list", productInfoRepository.findByCategoryIdAndStoreId(rt.getId(),storeId));
            arr.add(m);
        }
        ResponseFormat rep = Result.success();
        rep.setData(arr);
        return gson.toJson(rep);
    }

    @RequestMapping("/getStoreInfo")
    public String getStoreInfo(Long storeId){
        StoreInfo store = storeInfoRepository.findById(storeId).orElse(null);
        if(store==null) throw new RecordNotFoundException();
        ResponseFormat rep = Result.success();
        rep.setData(store);
        return gson.toJson(rep);
    }

    @RequestMapping("/getProductInfo")
    public String getProductInfo(@RequestParam("products") Long[] ids){
        Map<String,Object> map = new HashMap<>();
        for(Long id : ids){
            ProductInfo product = productInfoRepository.findById(id).orElse(null);
            if(product!=null) map.put(""+id, product);
        }
        ResponseFormat rep = Result.success();
        rep.setData(map);
        return gson.toJson(rep);
    }
}