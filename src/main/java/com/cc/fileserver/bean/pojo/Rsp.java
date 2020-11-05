package com.cc.fileserver.bean.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @Classname Rsp
 * @Description TODO
 * @Date 2020/7/18 16:30
 * @Created by Administrator
 */
@ApiModel(description = "响应根结构")
public class Rsp implements Serializable {
    @ApiModelProperty(value = "响应码")
    private Integer code;
    @ApiModelProperty(value = "响应描述")
    private String msg;
    @ApiModelProperty(value = "响应数据")
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
        return build().code(HttpStatus.OK.value());
    }

    public static Rsp failure(String msg){ return build().code(HttpStatus.INTERNAL_SERVER_ERROR.value()).msg(msg); }

    public Rsp body(Object body){
        this.body = body;
        return this;
    }
}
