package com.example.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result implements Serializable {
    private String code;
    private String msg;
    private Object data;

    public static Result success(){
        return new Result("200","",null);
    }

    public static Result success(String msg){
        return new Result("200",msg,null);
    }

    public static Result success(Object data){
        return new Result("200","",data);
    }

    public static Result success(String msg,Object data){
        return new Result("200",msg,data);
    }

    public static Result fail(String msg){
        return new Result("-1",msg,null);
    }

    public static Result fail(String msg,Object data){
        return new Result("-1",msg,data);
    }

    public static Result fail(String code,String msg,Object data){
        return new Result(code,msg,data);
    }

}
