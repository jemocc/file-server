package com.cc.fileserver.plat_entity;

/**
 * @Classname RspStatus
 * @Description TODO
 * @Date 2020/7/18 16:31
 * @Created by Administrator
 */
public enum RspStatus {
    OK(0), FAILURE(1);

    private final Integer code;

    RspStatus(Integer code){
        this.code = code;
    }

    public Integer getCode(){
        return code;
    }
}
