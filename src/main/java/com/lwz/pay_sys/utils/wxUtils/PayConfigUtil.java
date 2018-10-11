package com.lwz.pay_sys.utils.wxUtils;

import com.lwz.pay_sys.constant.Constants;

/**
 * @Author:lwz
 * @Description:
 * @Date:2018/1/6 22:30
 */
public class PayConfigUtil {
    // 账号信息
    public static final String appid = "*****";  // appid 填写自己的
    public static final String MCH_ID = "*****"; // 商业号 填写自己微信的
    public static final String API_KEY = "*****"; // Api key 填写自己微信的
    public static final String APP_SECRET = ""; // appsecret 填写自己的
    public static final String NOTIFY_URL = Constants.LOCALHOST + "/pay/wxPay/Notify"; //回调url
    public static final String CREATE_IP = "112.74.135.224";  //ip地址
    public static final String UFDODER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder"; //统一下单url，微信提供的
    public static final String QUERY_ORDER_STATE = "https://api.mch.weixin.qq.com/pay/orderquery"; //查询订单支付状态url，微信提供
    public static final String TRADE_TYPE = "NATIVE";
    public static final String DEVICE_INFO = "H5";

}
