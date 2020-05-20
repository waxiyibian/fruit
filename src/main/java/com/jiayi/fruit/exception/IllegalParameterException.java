package com.jiayi.fruit.exception;

import lombok.Data;

@Data
public class IllegalParameterException extends RuntimeException{
    private String message="非法参数";

}