package com.jiayi.fruit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "file")
@Component
public class FileProperties{
    private String uploadFolder;
    private String innerPath;
    private String staticAccessPath;
    private String uri;
}