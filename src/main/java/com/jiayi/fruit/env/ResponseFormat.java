package com.jiayi.fruit.env;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFormat {
    private int code;
    private String message;
    private String tips;
    private Object data;

    public ResponseFormat(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ResponseFormat(int code, String message, String tips) {
        this.code = code;
        this.message = message;
        this.tips = tips;
    }
}
