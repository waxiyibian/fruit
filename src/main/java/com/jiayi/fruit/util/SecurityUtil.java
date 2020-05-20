package com.jiayi.fruit.util;

import com.jiayi.fruit.entity.SysRole;
import com.jiayi.fruit.entity.SysUser;
import com.jiayi.fruit.exception.UnAuthorizedException;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
  public static SysUser getUser() {
    Object o;
    try {
      o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (o == null || !(o instanceof SysUser)) {
        throw new UnAuthorizedException();
      }
    } catch (Exception e) {
      throw new UnAuthorizedException();
    }
    return (SysUser) o;
  }

  public static boolean hasRole(String role) {
    SysUser u = getUser();
    return hasRole(u, role);
  }

  public static boolean hasRole(SysUser u,String role) {
    for (SysRole r : u.getRoles()) {
      if (r.getName().equals(role))
        return true;
    }
    return false;
  }
}