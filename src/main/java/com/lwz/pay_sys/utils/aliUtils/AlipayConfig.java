package com.lwz.pay_sys.utils.aliUtils;

import com.lwz.pay_sys.constant.Constants;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径。
 */
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "******";
    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "******";
    public static String alipay_public_key = "******";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipay.com/gateway.do";
    //格式
    public static String format = "JSON";

    //pc端
    public static String toSucessPage = Constants.LOCALHOST + "/jiaofeiSuccess.html";
    //回调
    public static String paySuccessCall = Constants.LOCALHOST + "/alipay/alipay/paySuccessCall";

}

