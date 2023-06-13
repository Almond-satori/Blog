package com.example.blog.dto;

import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private String code;
    private String msg;
    private Object data;

    public static Result success(){
        Result result = new Result();
        result.setCode("0");
        result.setData(null);
        return result;
    }
    public static Result success(String msg){
        Result result = new Result();
        result.setCode("0");
        result.setData(null);
        result.setMsg(msg);
        return result;
    }
    public static Result success(Object data){
        Result result = new Result();
        result.setCode("0");
        result.setData(data);
        return result;
    }

    public static Result success(String msg,Object data){
        Result result = new Result();
        result.setCode("0");
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail(String msg){
        Result result = new Result();
        result.setCode("-1");
        result.setMsg(msg);
        return result;
    }

    public static Result fail(String msg,Object data){
        Result result = new Result();
        result.setCode("-1");
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result fail(String code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
