package com.cc.fileserver.plat_entity;

import java.io.Serializable;

/**
 * @Classname Rsp
 * @Description TODO
 * @Date 2020/7/18 16:30
 * @Created by Administrator
 */
public class Rsp implements Serializable {
    private Integer code;
    private String msg;
    private Object body;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public static Rsp build(){
        return new Rsp();
    }


    public Rsp code(Integer code){
        this.code = code;
        return this;
    }

    public Rsp msg(String msg){
        this.msg = msg;
        return this;
    }

    public static Rsp ok(){
        return build().code(RspStatus.OK.getCode());
    }

    public static Rsp failure(String msg){ return build().code(RspStatus.FAILURE.getCode()).msg(msg); }

    public Rsp body(Object body){
        this.body = body;
        return this;
    }
}
