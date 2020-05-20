package com.jiayi.fruit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;

import com.jiayi.fruit.entity.RecommendInfo;
import com.jiayi.fruit.entity.RecommendType;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.exception.IllegalParameterException;
import com.google.gson.Gson;
import com.jiayi.fruit.entity.ProductInfo;
import com.jiayi.fruit.repository.ProductInfoRepository;
import com.jiayi.fruit.repository.RecommendInfoRepository;
import com.jiayi.fruit.repository.RecommendTypeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * commend是推荐的意思和recommend类似
 */
@RestController
@RequestMapping("/admin/commend")
public class AdminCommendController {
  @Autowired
  private Gson gson;
  @Autowired
  private RecommendInfoRepository recommendInfoRepository;
  @Autowired
  private RecommendTypeRepository recommendTypeRepository;
  @Autowired
  private ProductInfoRepository productInfoRepository;

  @RequestMapping("/list")
  public String list(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
                       @RequestParam(value = "page_size", defaultValue = "10") Integer size,
                       @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword){
        Sort sort = Sort.by(Sort.Order.desc("updateTime"));
        PageRequest request = PageRequest.of(page-1, size, sort);//new PageRequest(page - 1, size, sort);
        Page<RecommendInfo> lists = recommendInfoRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p=null;
            //equal 示例
            if (keyword!=null && !StringUtils.isEmpty(keyword)){
              Join<RecommendInfo,ProductInfo> product = root.join("product");
                p = cb.like(root.get("id").as(String.class),"%"+keyword+"%");
                p = cb.or(p,cb.like(product.get("id").as(String.class),"%"+keyword+"%"));
                p = cb.or(p,cb.like(product.get("productName"),"%"+keyword+"%"));
                p = cb.or(p,cb.like(product.get("description"),"%"+keyword+"%"));
            }
            if (p != null){
              predicates.add(p);
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, request);
        List<RecommendInfo> list = lists.getContent();
        ResponseFormat rep = Result.success();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total_num", lists.getTotalElements());
        map.put("page_num", page);
        map.put("page_size", size);
        map.put("tableData",list);

        List<RecommendType> cates = recommendTypeRepository.findAll();
        map.put("categories",cates);
        
        rep.setData(map);
        return gson.toJson(rep);
    }

    /**
     * 保存推荐商品信息recommend_info
     */
    @RequestMapping("save")
    public String saveProduct(RecommendInfo reco,Long productId) {
      List<RecommendInfo> list = recommendInfoRepository.findByProductId(productId);
      if(list!=null&&list.size()>0){
        throw new IllegalParameterException(){{setMessage("ID为"+productId+"的商品存在推荐列表中");}};
      }
      RecommendType type = recommendTypeRepository.findById(reco.getTypeId()).orElse(null);
      if(type==null){
        throw new IllegalParameterException(){{setMessage("所选推荐分类不存在");}};
      }
      ProductInfo product = productInfoRepository.findById(productId).orElse(null);
      if(product==null){
        throw new IllegalParameterException(){{setMessage("要推荐的商品不存在");}};
      }
      reco.setProduct(product);
      recommendInfoRepository.save(reco);
      return gson.toJson(Result.success());
    }

    @RequestMapping("/delete")
    public String deleteProduct(Long id){
      RecommendInfo reco = recommendInfoRepository.findById(id).orElse(null);
      if(reco==null){
        throw new IllegalParameterException(){{setMessage("要删除的推荐商品不存在");}};
      }
      recommendInfoRepository.deleteById(id);
      return gson.toJson(Result.success());
    }

    @RequestMapping("/saveCate")
    public String saveCate(RecommendType type){
      if(type.getId()!=null){
        RecommendType oldt = recommendTypeRepository.findById(type.getId()).orElse(null);
        type.setCreateTime(oldt.getCreateTime());
        type.setUpdateTime(oldt.getUpdateTime());
      }
      if(StringUtils.isEmpty(type.getName())){
        throw new IllegalParameterException(){{setMessage("推荐分类名称不能为空");}};
      }
      recommendTypeRepository.save(type);
      return gson.toJson(Result.success());
    }
    
    @RequestMapping("/deleteCate")
    public String deleteCate(Long id){
      RecommendType type = recommendTypeRepository.findById(id).orElse(null);
      if(type==null){
        throw new IllegalParameterException(){{setMessage("要删除的推荐分类不存在");}};
      }
      recommendTypeRepository.deleteById(id);
      return gson.toJson(Result.success());
    }
}