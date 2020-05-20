package com.jiayi.fruit.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.jiayi.fruit.entity.SysRole;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.env.Constant;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.env.RoleName;
import com.jiayi.fruit.exception.IncorrectPasswordException;
import com.jiayi.fruit.exception.InvalidCodeException;
import com.jiayi.fruit.exception.InvalidEmailException;
import com.jiayi.fruit.exception.PermissionDeniedException;
import com.jiayi.fruit.exception.UnknownUserException;
import com.jiayi.fruit.form.UserRegisterForm;
import com.jiayi.fruit.repository.StoreInfoRepository;
import com.jiayi.fruit.repository.SysUserRepository;
import com.jiayi.fruit.util.CommonUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/open")
@Slf4j
public class OpenController {
    @Autowired
    private Gson gson;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserRepository repository;
    @Autowired
    private StoreInfoRepository storeRepository;
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JavaMailSender javaMailSender;
  
    @Value("${spring.mail.username}")
    private String from;

    @RequestMapping("/store/findAll")
    public String findAll(){
        ResponseFormat rep = Result.success();
        rep.setData(storeRepository.findAll());
        return gson.toJson(rep);
    }

    @RequestMapping("/getCode")
    public String getCode(@RequestParam("email")String to, HttpSession session) {
        // 模拟发送短信存储在session中
        ResponseFormat rep = Result.success();
        boolean isEmail = CommonUtil.emailTest(to);
        if(!isEmail) throw new InvalidEmailException();
         //生成6位验证码
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        session.setAttribute("code", verifyCode);
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(from);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject("这是毕业设计的功能测试");
        simpleMailMessage.setText("这是生成的数字："+verifyCode+"，这不是垃圾邮件，谢谢！！！！");
        try{
            javaMailSender.send(simpleMailMessage);// 发送
        }catch(Throwable e){
            e.printStackTrace();
            log.error(from+","+to+",fail");
        }
        return gson.toJson(rep);
    }

    @RequestMapping("/register")
    public String register(@Validated UserRegisterForm form, BindingResult result,HttpSession session) {
        Map<String, String> map = new HashMap<String, String>();
        ResponseFormat rep;
        if (result.hasErrors()) {
            for (FieldError fe : result.getFieldErrors()) {
                map.put(fe.getField(), fe.getDefaultMessage());
            }
            rep = Result.invalidArguments();
            rep.setData(map);
        } else {
            // 进行验证码比对
            String compare = (String)session.getAttribute("code");
            String code = form.getCode();
            if(compare==null||code==null||!compare.contentEquals(code)){
                throw new InvalidCodeException();
            }
            
            // 对密码进行加密
            form.setPassword(passwordEncoder.encode(form.getPassword()));
            SysUser user = new SysUser();
            BeanUtils.copyProperties(form, user);
            repository.save(user);
            rep = Result.success();
        }
        return gson.toJson(rep);
    }

    @RequestMapping("/loginStatus")
    public String loginStatus() {
        SysUser user = null;
        ResponseFormat rep = Result.unAuthorized();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        try{
            user = (SysUser) auth.getPrincipal();
            user = repository.findById(user.getId()).orElse(null);
            user.setPassword(null);
            rep = Result.success();
            rep.setData(user);
        }catch(ClassCastException e){
        }
        return gson.toJson(rep);
    }

    @RequestMapping("/storeLogin")
    public String storeLogin(String username, String password, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = RoleName.STORE;
        return loginWithRole(username, password, request, response, role);
    }

    @RequestMapping("/loginAdmin")
    public String loginAdmin(String username, String password, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = RoleName.ADMIN;
        return loginWithRole(username, password, request, response, role);
    }

    @RequestMapping("/deliverLogin")
    public String deliverLogin(String username, String password, HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String role = RoleName.DELIVERY;
        return loginWithRole(username, password, request, response, role);
    }

    private String loginWithRole(String username, String password, HttpServletRequest request, HttpServletResponse response,String role){
        SysUser user = repository.findByUsername(username);
        if(user==null){
            throw new UnknownUserException();
        }else if(!passwordEncoder.matches(password, user.getPassword())){
            throw new IncorrectPasswordException();
        }
        List<SysRole> roles = user.getRoles();
        boolean hasRole = false;
        for(SysRole ro : roles){
            if(role != null && role.contentEquals(ro.getName())){
                hasRole = true;
                break;
            }
        }
        if(!hasRole){
            throw new PermissionDeniedException();
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        //根据userDetails构建新的Authentication,这里使用了
        //PreAuthenticatedAuthenticationToken当然可以用其他token,如UsernamePasswordAuthenticationToken
        PreAuthenticatedAuthenticationToken authentication =
            new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(),userDetails.getAuthorities());
        //设置authentication中details
        authentication.setDetails(new WebAuthenticationDetails(request));
        //存放authentication到SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            return gson.toJson(Result.success());
        }else{
            throw new UnknownUserException();
        }
    }
}