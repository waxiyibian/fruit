package com.jiayi.fruit.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.jiayi.fruit.env.Result;
import com.jiayi.fruit.env.RoleName;
import com.jiayi.fruit.security.CustomUserService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public UserDetailsService customUserService() {
        return new CustomUserService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/admin/**").hasAuthority(RoleName.ADMIN)
                .antMatchers("/store/**").hasAnyAuthority(RoleName.STORE,RoleName.ADMIN)
                .antMatchers("/delivery/**").hasAnyAuthority(RoleName.DELIVERY,RoleName.ADMIN)
                .antMatchers("/user/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .rememberMe().tokenValiditySeconds(60*60*24*7)
                .and().formLogin().loginProcessingUrl("/login").successHandler(new AuthenticationSuccessHandler(){
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new Gson().toJson(Result.success()));
                            out.flush();
                        }
                    }).failureHandler(new AuthenticationFailureHandler(){
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest req, HttpServletResponse resp, AuthenticationException exception) throws IOException, ServletException {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new Gson().toJson(Result.unAuthorized()));
                            out.flush();
                        }
                    })
                    .permitAll().and()
                    .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessHandler(new LogoutSuccessHandler(){
                        @Override
                        public void onLogoutSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication authentication) throws IOException, ServletException {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new Gson().toJson(Result.success()));
                            out.flush();
                        }
                    }).permitAll().and()
                    .csrf().disable().exceptionHandling()
                    .authenticationEntryPoint(new AuthenticationEntryPoint(){
                        @Override
                        public void commence(HttpServletRequest req, HttpServletResponse resp, AuthenticationException authException) throws IOException, ServletException {
                            resp.setContentType("application/json;charset=utf-8");
                            PrintWriter out = resp.getWriter();
                            out.write(new Gson().toJson(Result.unAuthorized()));
                            out.flush();
                            out.close();
                        }
                    }).accessDeniedHandler(new AccessDeniedHandler(){
                        @Override
                        public void handle(HttpServletRequest request, HttpServletResponse response,
                                AccessDeniedException accessDeniedException) throws IOException, ServletException {
                                    response.setContentType("application/json;charset=utf-8");
                                    PrintWriter out = response.getWriter();
                                    out.write(new Gson().toJson(Result.permissionDenied()));
                                    out.flush();
                                    out.close();
                        }
                    });
    }
}