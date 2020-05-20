package com.jiayi.fruit.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    /**
     * 获取格式化时间
     * */
    public static String getFormatedDate(Date date, String format){
        String result = null;
        try{
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            result = formatter.format(date);
        }catch(Exception e){}
        return result;
    }
    public static String getFormatedDate(String format){
        return getFormatedDate(new Date(),format);
    }
    public static String getFormatedDate(Date date){
        String result=null;
        try{result=formatter.format(date);}catch(Exception e){}
        return result;
    }
    public static String getFormatedDate(){return getFormatedDate(new Date());}
    public static String getSimpleFormateDate(){return getFormatedDate("yyyyMMddhhmmss");}

}
