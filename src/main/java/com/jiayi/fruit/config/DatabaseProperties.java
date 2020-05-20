package com.jiayi.fruit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "db")
@PropertySource(value = { "classpath:jdbc.properties" }, ignoreResourceNotFound = true, encoding = "utf-8")
@Component
public class DatabaseProperties {
  // 数据库名称
  private String databasename;
  // mysql安装的地址
  private String mysqldump;
  private String mysqladmin;
  // 数据库用户名
  private String username;
  // 数据库密码
  private String password;
  // mysqlexe
  private String mysqlexe;
  private String host;
  private String port;

  public String getMysqlConfig() {
    return " -u " + username + " " + (StringUtils.isEmpty(password) ? "" : " -p " + password + " ")
        + (StringUtils.isEmpty(host) ? "" : " -h " + host + " ")
        + (StringUtils.isEmpty(port) ? "" : "-P " + port + " ");
  }

  public String getMysqlDumpCommand() {
    String str = mysqldump + getMysqlConfig() + databasename+ " --default-character-set=utf8 --hex-blob ";
    System.out.println(str);
    return str;
  }

  public String getMysqlAdminCommand() {
    return mysqladmin + getMysqlConfig() + "create " + databasename;
  }

  public String getMysqlCommend() {
    return mysqlexe + getMysqlConfig() + databasename;
  }
}