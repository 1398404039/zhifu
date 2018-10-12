package com.lwz.pay_sys.exception.exceptionHandler;

import com.alibaba.fastjson.JSONObject;
import com.lwz.pay_sys.exception.exception.SignErrorException;
import com.lwz.pay_sys.utils.response.RespMsg;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseBody
public class SignErrorExceptionHandler {

    @ExceptionHandler(value = SignErrorException.class)
    public JSONObject doSignErrorHandler(){
        return RespMsg.SIGNERROTR();
    }
}
