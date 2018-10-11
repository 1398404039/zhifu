package com.lwz.pay_sys.utils.sms;

import com.alibaba.fastjson.JSONObject;
import com.lwz.pay_sys.utils.http.HttpTool;
import com.lwz.pay_sys.utils.response.ConstVar;
import com.lwz.pay_sys.utils.response.RespMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

public class SmsUtil {

    private static Logger logger = LoggerFactory.getLogger(SmsUtil.class);

    private static final int smsFlag = 1; //1:发送短信    0：不发送短信
    //    public static final int smsFlag=1; //1:发送短信    0：不发送短信
    private static final String smsUrl = "http://sms.253.com/msg/send";
    private static final String user = "N9094140";
    private static final String pwd = "inTd7kmfp0eb87";

    /**
     * 发送短信
     *
     * @param phoneNum
     * @return
     */
    public static JSONObject sendSmsMessage(String phoneNum, String message) {
        try {
            String url = new StringBuffer(smsUrl).append("?un={0}&pw={1}&phone={2}&msg={3}&rd=1").toString();
            String urlParam = MessageFormat.format(url, user, pwd, phoneNum, message);
            if (smsFlag == 1) {
                String smsResp = HttpTool.httpGetRequest(urlParam);
                if (smsResp.contains(ConstVar.ZERO)) {
                    return RespMsg.SUCCESS();
                }
            } else {
                logger.info("接收手机：" + phoneNum + "短信内容：" + message);
                return RespMsg.SUCCESS();
            }
        } catch (Exception e) {
            logger.error(String.format("发送短信失败，手机号：%s******短信内容：%s", phoneNum, message));
        }
        return RespMsg.FAIL("发送短信消息错误");
    }

    public static void main(String[] args){
        sendSmsMessage("17610772092","兑换码：XXXXXXXXXX");
    }
}
