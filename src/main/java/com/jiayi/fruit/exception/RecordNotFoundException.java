package com.jiayi.fruit.exception;

import lombok.Data;

@Data
public class RecordNotFoundException extends RuntimeException{
    private String message="记录不存在";

}