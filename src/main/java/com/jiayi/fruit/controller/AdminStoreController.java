package com.jiayi.fruit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.StoreInfo;
import com.jiayi.fruit.entity.SysRole;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.env.RoleName;
import com.jiayi.fruit.exception.IllegalParameterException;
import com.jiayi.fruit.exception.UnknownException;
import com.jiayi.fruit.repository.StoreInfoRepository;
import com.jiayi.fruit.repository.SysRoleRepository;
import com.jiayi.fruit.repository.SysUserRepository;
import com.jiayi.fruit.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/store")
public class AdminStoreController {
    @Autowired
    private Gson gson;
    @Autowired
    private SysUserRepository userRepository;
    @Autowired
    private StoreInfoRepository storeRepository;
    @Autowired
    private SysRoleRepository roleRepository;
    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping("/findAll")
    public String findAll(){
        ResponseFormat rep = Result.success();
        rep.setData(storeRepository.findAll());
        return gson.toJson(rep);
    }

    @RequestMapping("/list")
    public String list(@RequestParam(value = "page_num", defaultValue = "1") Integer page,
            @RequestParam(value = "page_size", defaultValue = "10") Integer size,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        Sort sort = Sort.by(Sort.Order.desc("updateTime"));
        PageRequest request = PageRequest.of(page - 1, size, sort);// new PageRequest(page - 1, size, sort);
        Page<StoreInfo> lists = storeRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            Predicate p = null;
            // equal 示例
            if (keyword != null && !StringUtils.isEmpty(keyword)) {
                p = cb.like(root.get("ownerId").as(String.class), "%" + keyword + "%");
                p = cb.or(p, cb.like(root.get("id").as(String.class), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("storeName"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("address"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("description"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("phone"), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("minCost").as(String.class), "%" + keyword + "%"));
                p = cb.or(p, cb.like(root.get("postFee").as(String.class), "%" + keyword + "%"));
            }
            if (p != null){
                predicates.add(p);
            }
            return query.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        }, request);
        List<StoreInfo> list = lists.getContent();
        ResponseFormat rep = Result.success();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total_num", lists.getTotalElements());
        map.put("page_num", page);
        map.put("page_size", size);
        map.put("tableData", list);
        Map<String, Object> userMap = new HashMap<String, Object>();
        for(StoreInfo str : list){
            SysUser ur = userRepository.findById(str.getOwnerId()).orElse(null);
            ur.setPassword(null);
            userMap.put(""+str.getOwnerId(),ur);
        }
        map.put("userMap", userMap);
        rep.setData(map);
        return gson.toJson(rep);
    }

    @RequestMapping("/save")
    @Transactional
    public String finish(StoreInfo store) {
        ResponseFormat rep = Result.success();
        SysUser u = userRepository.findById(store.getOwnerId()).orElse(null);
        if(u==null){
            throw new IllegalParameterException(){{setMessage("店主账号不存在");}};
        }
        StoreInfo olds = storeRepository.findByOwnerId(store.getOwnerId());
        if(olds!=null&&store.getId()==null){
            throw new IllegalParameterException(){{setMessage("该用户已是店铺ID"+olds.getId()+"的店主");}};
        }
        if(!SecurityUtil.hasRole(u, RoleName.STORE)){
            //增加商家角色
            List <SysRole> roles = u.getRoles();
            SysRole ro = roleRepository.findByName(RoleName.STORE);
            roles.add(ro);
            u.setRoles(roles);
            userRepository.save(u);
        }
        if(olds!=null){
            store.setCreateTime(olds.getCreateTime());
            store.setUpdateTime(olds.getUpdateTime());
        }
        storeRepository.save(store);
        return gson.toJson(rep);
    }

    @RequestMapping("/delete")
    @Transactional
    public String cancel(Long id) {
        StoreInfo s = storeRepository.findById(id).orElse(null);
        if(s==null){
            throw new IllegalParameterException(){{setMessage("店铺记录不存在");}};
        }
        SysUser u = userRepository.findById(s.getOwnerId()).orElse(null);
        if(u==null){
            throw new IllegalParameterException(){{setMessage("店主账号不存在");}};
        }
        if(SecurityUtil.hasRole(u, RoleName.STORE)){
            //移除商家角色
            List <SysRole> roles = u.getRoles();
            SysRole ro = null;
            for(SysRole r:roles) if(r.getName().equals(RoleName.STORE)){ro=r;break;}
            roles.remove(ro);
            u.setRoles(roles);
            userRepository.save(u);
        }
        storeRepository.deleteById(id);
        ResponseFormat rep = Result.success();
        return gson.toJson(rep);
    }


    @RequestMapping("/chroot")
    public String chroot(Long id,HttpServletRequest request){
        StoreInfo store = storeRepository.findById(id).orElse(null);
        if(store==null){
            throw new IllegalParameterException(){{setMessage("店铺不存在");}};
        }
        SysUser u = userRepository.findById(store.getOwnerId()).orElse(null);
        if(u==null){
            throw new IllegalParameterException(){{setMessage("店主账号不存在");}};
        }
        //使用该商家的账号重新登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(u.getUsername());
        //根据userDetails构建新的Authentication,这里使用了
        //PreAuthenticatedAuthenticationToken当然可以用其他token,如UsernamePasswordAuthenticationToken
        PreAuthenticatedAuthenticationToken authentication =
            new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());
        //设置authentication中details
        authentication.setDetails(new WebAuthenticationDetails(request));
        //存放authentication到SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long userId = SecurityUtil.getUser().getId();
        if( u.getId().equals(userId) ){
            return gson.toJson(Result.success());
        }else{
            throw new UnknownException();
        }
    }
}