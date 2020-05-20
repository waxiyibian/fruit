package com.jiayi.fruit.exception;

import lombok.Data;

@Data
public class UnknownException extends RuntimeException{
    private String message="未知错误";

}