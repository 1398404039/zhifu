package com.lwz.pay_sys.exception.exception;

public class SignErrorException extends RuntimeException {
    private Integer code;
    private String msg;

    public SignErrorException() {
        this.code = -1;
        this.msg = "验签失败，请重试";
    }

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
}
