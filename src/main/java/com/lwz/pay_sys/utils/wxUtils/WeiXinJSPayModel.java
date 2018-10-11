package com.lwz.pay_sys.utils.wxUtils;

import java.io.Serializable;

/**
 * @Author:lwz
 * @Description:
 * @Date:2018/2/21 16:30
 */
public class WeiXinJSPayModel implements Serializable {
    private String appid;
    private String timestamp;
    private String noceStr;
    private String prepay_id;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNoceStr() {
        return noceStr;
    }

    public void setNoceStr(String noceStr) {
        this.noceStr = noceStr;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
