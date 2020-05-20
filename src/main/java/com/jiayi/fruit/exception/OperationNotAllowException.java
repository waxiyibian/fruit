package com.jiayi.fruit.exception;

import lombok.Data;

@Data
public class OperationNotAllowException extends RuntimeException{
    private String message="不允许的操作";

}