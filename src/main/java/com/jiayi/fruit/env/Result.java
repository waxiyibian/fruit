package com.jiayi.fruit.env;

import com.google.gson.JsonElement;

public class Result {
    // 正数code代表成功，负数code代表失败
    public static ResponseFormat success() {
        return new ResponseFormat(1, "success");
    }

    public static ResponseFormat fail() {
        return new ResponseFormat(-1, "fail");
    }

    public static ResponseFormat unAuthorized() {
        return new ResponseFormat(-11, "unAuthorized", "登录已失效，请重新登录", null);
    }

    public static ResponseFormat UnknownUsername() {
        return new ResponseFormat(-12, "unknow username", "用户名不存在", null);
    }

    public static ResponseFormat IncorrectPassword() {
        return new ResponseFormat(-13, "incorrect password", "密码错误", null);
    }

    public static ResponseFormat NullParameter() {
        return new ResponseFormat(-2, "parameter cannot be null", "输入不能为空", null);
    }

    public static ResponseFormat IllegalImage() {
        return new ResponseFormat(-21, "illegal image type", "图片格式错误", null);
    }

    public static ResponseFormat uploadFail() {
        return new ResponseFormat(-22, "file upload fail", "文件上传失败", null);
    }

    public static ResponseFormat invalidEmail() {
        return new ResponseFormat(-23, "invalidEmail", "邮箱格式不正确", null);
    }

    public static ResponseFormat invalidCode() {
        return new ResponseFormat(-25, "invalidCode", "验证码错误", null);
    }

    /**
     * 输入的查询条件未能查询到数据，例如试图更新或删除不存在的数据
     */
    public static ResponseFormat unmatchedData() {
        return new ResponseFormat(-23, "no record found for operation", "未查询到数据，无法执行操作", null);
    }

    /**
     * 提交表单的参数不合法
     */
    public static ResponseFormat invalidArguments() {
        return new ResponseFormat(-24, "invalid argument", "非法参数", null);
    }

    public static ResponseFormat permissionDenied() {
        return new ResponseFormat(-3, "permission Denied", "权限不足", null);
    }

    public static ResponseFormat recordNotFound() {
        return new ResponseFormat(-4, "product not found", "记录不存在", null);
    }

    public static ResponseFormat operationNotAllow() {
        return new ResponseFormat(-41, "operation not allow", "不允许的操作", null);
    }
}
