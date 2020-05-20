package com.jiayi.fruit.repository;

import com.jiayi.fruit.entity.SysRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SysRoleRepository extends JpaSpecificationExecutor<SysRole>,JpaRepository<SysRole, Long> {
    SysRole findByName(String name);
}