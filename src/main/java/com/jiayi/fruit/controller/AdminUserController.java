package com.jiayi.fruit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.exception.IllegalParameterException;
import com.jiayi.fruit.repository.SysUserRepository;
import com.jiayi.fruit.service.UserService;
import com.jiayi.fruit.util.CommonUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/user")
public class AdminUserController {
    @Autowired
    private Gson gson;
    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private UserService userService;

    @RequestMapping("/findAll")
    public String findAll(){
        ResponseFormat rep = Result.success();
        List<SysUser> list = userRepository.findAll();
        for(SysUser u : list){u.setPassword(null);}
        rep.setData(list);
        return gson.toJson(rep);
    }

    @RequestMapping("/list")
    public String list(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("updateTime"));
        PageRequest request = PageRequest.of(page - 1, size, sort);// new PageRequest(page - 1, size, sort);
        Page<SysUser> lists = userRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p = null;
            // equal 示例
            if (keyword != null && !StringUtils.isEmpty(keyword)) {
                p = cb.like(root.get("id").as(String.class), "%" + keyword + "%");
                p = cb.or(p, cb.like(root.get("nickname"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("username"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("phone"), "%" + keyword + "%"));
            }
            if (p != null){
                predicates.add(p);
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, request);
        List<SysUser> list = lists.getContent();
        for(SysUser u : list){u.setPassword(null);}
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
    public String finish(SysUser user) {
        ResponseFormat rep = Result.success();
        SysUser u = null;
        user.setCreateTime(null);
        user.setUpdateTime(null);
        if(StringUtils.isEmpty(user.getUsername()) ){
            throw new IllegalParameterException(){{setMessage("用户名不能为空");}};
        }
        if(user.getId()!=null){//编辑
            u = userRepository.findById(user.getId()).orElse(null);
            if(StringUtils.isEmpty(user.getPassword())) user.setPassword(u.getPassword());
            user.setCreateTime(u.getCreateTime());
            user.setUpdateTime(u.getUpdateTime());
            user.setRoles(u.getRoles());
        }else{//新增
            if(StringUtils.isEmpty(user.getPassword()) ){
                throw new IllegalParameterException(){{setMessage("密码不能为空");}};
            }
        }
        userRepository.save(user);
        return gson.toJson(rep);
    }

    @RequestMapping("/delete")
    @Transactional
    public String cancel(Long id) {
        SysUser u = userRepository.findById(id).orElse(null);
        if(u==null){
            throw new IllegalParameterException(){{setMessage("用户不存在");}};
        }
        //删除与用户相关的一切信息，评论，订单等，再删除用户本身
        userService.deleteAllById(id);
        ResponseFormat rep = Result.success();
        return gson.toJson(rep);
    }

}