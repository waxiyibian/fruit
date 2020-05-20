package com.jiayi.fruit.exception;

import lombok.Data;

@Data
public class UnAuthorizedException extends RuntimeException{
    private String message="未登录";

}