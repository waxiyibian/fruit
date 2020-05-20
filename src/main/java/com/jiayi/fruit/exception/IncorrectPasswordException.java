package com.jiayi.fruit.exception;

import lombok.Data;

@Data
public class IncorrectPasswordException extends RuntimeException{
    private String message="密码错误";

}