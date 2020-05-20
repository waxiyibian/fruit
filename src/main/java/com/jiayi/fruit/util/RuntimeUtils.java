package com.jiayi.fruit.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.concurrent.*;

@Component
public class RuntimeUtils {
    private static ExecutorService executorService;
    @Autowired
    public void setExecutorService(ExecutorService executorService){
        RuntimeUtils.executorService = executorService;
    }
    private static void checkExecutorService(){
        if(executorService==null) {
            executorService = Executors.newCachedThreadPool();
        }
    }
    public static RuntimeResolve execute(String command) throws IOException, ExecutionException{
        return execute(command, null);
    }
    public static RuntimeResolve execute(String command,String write) throws IOException, ExecutionException {
        checkExecutorService();
        RuntimeResolve resolve = new RuntimeResolve();
        resolve.setCommand(command);
        //执行命令
        Runtime runtime = Runtime.getRuntime();
        Process p = runtime.exec(command);
        //exec使用时要注意清空两个输入流，并且使用waitFor等待。
        InputStream i = p.getInputStream();
        InputStream eri = p.getErrorStream();
        if(!StringUtils.isEmpty(write)){
            OutputStreamWriter writer = new OutputStreamWriter(p.getOutputStream(),"utf-8");
            writer.write(write);
            writer.flush();
            writer.close();
        }
        resolve.setInput(i);
        resolve.setErrorInput(eri);
        Future<StringBuilder> futureinput = executorService.submit(()->{
            StringBuilder output = new StringBuilder();
            Scanner sc = new Scanner(new InputStreamReader(i,"utf-8"));
            while(sc.hasNext()){
                output.append(sc.nextLine());
                output.append(System.getProperty("line.separator"));
            }
            return output;
        });
        Future<StringBuilder> futureerror = executorService.submit(()->{
            StringBuilder output = new StringBuilder();
            Scanner sc = new Scanner(new InputStreamReader(eri,"utf-8"));
            while(sc.hasNext()){
                output.append(sc.nextLine());
                output.append(System.getProperty("line.separator"));
            }
            return output;
        });
        StringBuilder input=null,error=null;
        int n;
        //等待两个输入流完成 //需要分开catch
        try {
            input = futureinput.get();
            resolve.setDatum(input);
        } catch (InterruptedException e) {}
        try {
            error = futureerror.get();
            resolve.setError(error);
        } catch (InterruptedException e) {}
        //等待exec返回
        try {
            n = p.waitFor();
            resolve.setCode(n);
        } catch (InterruptedException e) {}

        return resolve;
    }
    @Data
    public static class RuntimeResolve{
        //输入流
        private InputStream input;
        //错误输入流
        private InputStream errorInput;
        //返回码
        private Integer code;
        //执行语句
        private String command;
        private StringBuilder datum;
        private StringBuilder error;
    }
}
