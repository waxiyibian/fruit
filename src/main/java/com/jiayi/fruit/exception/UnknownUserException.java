package com.jiayi.fruit.exception;

import lombok.Data;

@Data
public class UnknownUserException extends RuntimeException{
    private String message="用户不存在";

}