package com.jiayi.fruit.exception;

import com.google.gson.Gson;
import com.jiayi.fruit.env.ResponseFormat;
import com.jiayi.fruit.env.Result;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler{
    @Autowired
    private Gson gson;
    @ExceptionHandler(Exception.class)
    public String unknownException(Exception e){
        e.printStackTrace();
        return gson.toJson(Result.fail());
    }

    @ExceptionHandler(InvalidEmailException.class)
    public String invalidEmail(){
        return gson.toJson(Result.invalidEmail());
    }

    @ExceptionHandler(InvalidCodeException.class)
    public String invalidCode(){
        return gson.toJson(Result.invalidCode());
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public String unAuthorized(){
        return gson.toJson(Result.unAuthorized());
    }

    @ExceptionHandler(UnknownUserException.class)
    public String unknownUser(){
        return gson.toJson(Result.UnknownUsername());
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public String permissionDenied(){
        return gson.toJson(Result.permissionDenied());
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public String incorrectPassword(){
        return gson.toJson(Result.IncorrectPassword());
    }

    @ExceptionHandler(RecordNotFoundException.class)
    public String recordNotFound(){
        return gson.toJson(Result.recordNotFound());
    }

    @ExceptionHandler(OperationNotAllowException.class)
    public String operationNotAllow(){
        return gson.toJson(Result.operationNotAllow());
    }

    @ExceptionHandler(IllegalParameterException.class)
    public String illegalParameterException(IllegalParameterException e){
        ResponseFormat rep = Result.invalidArguments();
        if(e.getMessage()!=null) rep.setTips(e.getMessage());
        return gson.toJson(rep);
    }
}