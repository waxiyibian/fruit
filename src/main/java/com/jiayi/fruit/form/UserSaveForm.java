package com.jiayi.fruit.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserSaveForm{
    @NotNull(message = "账号不存在")
    private Long id;

    @Size(min=3,message = "账号长度过短")
    private String username;//phone

    @Size(min=4,message = "密码过短")
    private String password;

    @Size(min=1,message = "昵称为空")
    private String nickname;

    @Size(min=1,message = "手机号为空")
    private String phone;

    
}