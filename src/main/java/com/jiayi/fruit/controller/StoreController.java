package com.jiayi.fruit.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.jiayi.fruit.config.FileProperties;
import com.jiayi.fruit.entity.ProductCategory;
import com.jiayi.fruit.entity.ProductInfo;
import com.jiayi.fruit.entity.StoreInfo;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.repository.ProductCategoryRepository;
import com.jiayi.fruit.repository.ProductInfoRepository;
import com.jiayi.fruit.repository.StoreInfoRepository;
import com.jiayi.fruit.service.ProductService;
import com.jiayi.fruit.service.StoreService;
import com.jiayi.fruit.util.CommonUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.persistence.criteria.Predicate;

@RestController
@RequestMapping("/store")
public class StoreController {
    @Autowired
    private Gson gson;
    @Autowired
    private FileProperties prop;
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private StoreInfoRepository repository;
    @Autowired
    private ProductService productService;
    @Autowired
    private StoreService storeService;

    @PostMapping("/uploadIcon")
    public String uploadIcon(MultipartFile file,HttpServletRequest request)
            throws IllegalStateException, IOException {
        ResponseFormat rep = Result.fail();
        String filename = UUID.randomUUID().toString()+file.getOriginalFilename();
        CommonUtil.createDirectoryIfNotExsist(prop.getUploadFolder());
        file.transferTo(new File(prop.getUploadFolder(),filename));
        rep = Result.success();
        String path = CommonUtil.getServerIPPort(request) + prop.getUri()+filename;
        rep.setData(path);
        return gson.toJson(rep);
    }

    @RequestMapping("/saveProduct")
    public String saveProduct(@Validated ProductInfo product){
        
        product.setStoreId(storeService.getCurrentStoreId());
        productInfoRepository.save(product);
        ResponseFormat rep = Result.success();
        rep.setData(product);
        return gson.toJson(rep);
    }

    /**
     * 商品上架
     * @param id
     * @return
     */
    @RequestMapping(value="/onSaleProduct",produces="application/json;charset=utf-8")
    public String onSaleProduct(@RequestParam("id") Long id) {
        productService.onSale(id);
        ResponseFormat rep = Result.success();
        return gson.toJson(rep);
    }
    /**
     * 商品下架
     * @param id
     * @return
     */
    @RequestMapping(value="/offSaleProduct",produces = "application/json;charset=utf-8")
    public String offSaleProduct(@RequestParam("id") Long id) {
        productService.offSale(id);
        ResponseFormat rep = Result.success();
        return gson.toJson(rep);
    }

    /**
     * 商品删除
     * @param id
     * @return
     */
    @RequestMapping(value="/deleteProduct",produces = "application/json;charset=utf-8")
    public String deleteProduct(@RequestParam("id") Long id) {
        productInfoRepository.deleteById(id);
        ResponseFormat rep = Result.success();
        return gson.toJson(rep);
    }

    @RequestMapping(value="/productList",produces = "application/json;charset=utf-8")
    public String productList(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
                       @RequestParam(value = "page_size", defaultValue = "10") Integer size,
                       @RequestParam(value = "keyword",required = false,defaultValue = "") String keyword,
                       @RequestParam(value = "status",required = false,defaultValue = "-1")Integer status){
        Sort sort = Sort.by(Sort.Order.desc("updateTime"));
        PageRequest request = PageRequest.of(page-1, size, sort);//new PageRequest(page - 1, size, sort);
        Page<ProductInfo> userList = productInfoRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p=null;
            //equal 示例
            Long storeId = storeService.getCurrentStoreId();
            Predicate pr = cb.equal(root.get("storeId"), storeId);
            if (keyword!=null && !StringUtils.isEmpty(keyword)){
                p = cb.like(root.get("id").as(String.class),"%"+keyword+"%");
                p = cb.or(p,cb.like(root.get("productName").as(String.class),"%"+keyword+"%"));
                p = cb.or(p,cb.like(root.get("description"),"%"+keyword+"%"));
            }
            if(status!=null&&status>=0){
                if(p==null) p = cb.equal(root.get("status"), status);
                else p = cb.and(p,cb.equal(root.get("status"), status));
            }
            if (p != null){
                p = cb.and(pr, p);
            }else{
                p = pr;
            }
            predicates.add(p);
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, request);
        List<ProductInfo> list = userList.getContent();
        ResponseFormat rep = Result.success();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total_num", userList.getTotalElements());
        map.put("page_num", page);
        map.put("page_size", size);
        map.put("tableData",list);

        List<ProductCategory> cates = productCategoryRepository.findByStoreId(storeService.getCurrentStoreId());
        map.put("categories",cates);
        
        rep.setData(map);
        return gson.toJson(rep);
    }


    /**
     * 分类删除
     * @param categoryId
     * @return
     */
    @RequestMapping(value="/deleteCategory",produces = "application/json;charset=utf-8")
    public String deleteCategory(@RequestParam("categoryId") Long categoryId) {
        productCategoryRepository.deleteById(categoryId);
        ResponseFormat rep = Result.success();
        return gson.toJson(rep);
    }


    @RequestMapping("/saveCategory")
    public String saveCategory(String id,String categoryName){
        ProductCategory cate = new ProductCategory();
        cate.setStoreId(storeService.getCurrentStoreId());
        ResponseFormat rep;
        try{
            Long categoryId = Long.parseLong(id);
            if(categoryId!=null && categoryId>=0){
                cate.setId(categoryId);
            }
        }catch(Exception e){}
        if(StringUtils.isEmpty(categoryName)){
            rep = Result.fail();
            rep.setTips("分类名称不能为空");
        }else{
            cate.setName(categoryName);
            productCategoryRepository.save(cate);
        }
        rep = Result.success();
        rep.setData(cate);
        return gson.toJson(rep);
    }

    @RequestMapping(value="/info")
    public String info() {
        ResponseFormat rep = Result.fail();
        StoreInfo info = repository.findById(storeService.getCurrentStoreId()).orElse(null);
        if(info!=null){
            rep = Result.success();
            rep.setData(info);
        }
        return gson.toJson(rep);
    }

    @RequestMapping("save")
    public String save(@Validated StoreInfo info){
        Long id = storeService.getCurrentStoreId();
        if(id==null||!id.equals(info.getId())) return gson.toJson(Result.permissionDenied());
        StoreInfo s = repository.findById(id).orElse(null);
        info.setCreateTime(s.getCreateTime());
        info.setUpdateTime(s.getUpdateTime());
        repository.save(info);
        ResponseFormat rep = Result.success();
        return gson.toJson(rep);
    }
}