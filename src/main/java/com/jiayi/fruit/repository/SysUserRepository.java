package com.jiayi.fruit.repository;

import com.jiayi.fruit.entity.SysUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysUserRepository extends JpaSpecificationExecutor<SysUser>,JpaRepository<SysUser, Long> {
    SysUser findByUsername(String username);
}