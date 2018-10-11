package com.lwz.pay_sys.utils.aliUtils;

import com.lwz.pay_sys.constant.Constants;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径。
 */
public class AlipayConfig {

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "*****";
    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "*****+*****//*****/q36+*****+*****/*****+*****/*****+*****+Gw2j+*****+*****+*****/*****/G14XVkaWNEOcfhVIsZb0lxB7Gs2DN40bKs+JTZCH33GO6mmIPkFUlAbFU/sx1NM4B1WZg8Gq1p8IBhUyMxBaiUwnpaMGWPXuAsvtUiG05V5WX2+MJXPVIJb5sQKBgQDwc77HCiunHBHLHcC+DrdXJa0d/dxIpHgmH1ERc0KheVzhjQHvf13/7xQVbicVOPC26XYShD/1+smG/hIwm3u4Y9/kKpC71Q6tQ+ZpfaS15guH8EJ/46+JTYwIgxD9LjXVUYeEOW1+YuuZj9KOoQR6CTkLWfNrMePkbeNuAmGGrQKBgQDGei/WK3DRVYrBhU+qDTDm/oeedxDexEFOzWMySR+cvVKYVD0/zXO0BvcLnwbsXIigtDqHxWYQGFXcqog7R7KXZWhbAei+zMA6EUOVLiFLqE/7xG8GTB+ELdkJt8H1LBzh1oJn1vJ56T5Rf2120klwP/ctz2K+AM4Sy1LvbYqbhQKBgC0Q1HHDkzjnxuH1upkkcvJPamnTmYS35pjmh7AolRVkhKb1YaZ8jaaZLX9yvLp6iIsPqISB6ub0UO8PEWOw0HhXp6/A1o2Nugl5T2b4GHXLHuAkhKIpj03BjfxObFG3ZnmLpUV/6eilK5kFXqZCwW8J6XoaKsgtepPmkJies56lAoGBALEYo4h5WIAg0N+eQRgeDw19/ari2NW8zf0vfJyI7MEhmQo82C4O93eiY1smObo7Utn6FuPAujIQVMTCES676dn27SePS4q8DaWCdAgvkhv9AelHPD2lPNXnRurJ4TIPe3HzwoY/IDqxaR61Egpc1pRnLvncO2IIO9g/PjBPUMc1AoGBAM+gKL4qY+GCuwkaBapMVYWhiX6ffcQTUNgZ4k+7A2oQlsY2Gci6R3qMd9+JW8XJiOj8ezl2NJnHrAWTxlLTkQ26nXDShzq1XXjHY07/ekrixwkAk7Qz7AvWGCDUILX6NBkd9h8Um3Jpb1eSk+5IA1oRjHQYlGnJgIWe/HFQdNak";
    public static String alipay_public_key = "*****/*****+*****+*****/*****/*****+*****+*****+*****/*****";

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
    public static String paySuccessCall = Constants.LOCALHOST + "/pay/alipay/paySuccessCall";

}

