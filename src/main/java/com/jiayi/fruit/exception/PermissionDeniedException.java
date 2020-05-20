package com.jiayi.fruit.exception;

import lombok.Data;

@Data
public class PermissionDeniedException extends RuntimeException{
    private String message="权限不足";

}