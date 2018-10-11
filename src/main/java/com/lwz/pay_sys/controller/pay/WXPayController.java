package com.lwz.pay_sys.controller.pay;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.lwz.pay_sys.constant.Constants;
import com.lwz.pay_sys.entity.OrderEntity;
import com.lwz.pay_sys.service.OrderService;
import com.lwz.pay_sys.utils.QRUtil;
import com.lwz.pay_sys.utils.response.ConstVar;
import com.lwz.pay_sys.utils.response.RespMsg;
import com.lwz.pay_sys.utils.wxUtils.HttpUtil;
import com.lwz.pay_sys.utils.wxUtils.PayCommonUtil;
import com.lwz.pay_sys.utils.wxUtils.PayConfigUtil;
import com.lwz.pay_sys.utils.wxUtils.XMLUtil;
import io.swagger.annotations.ApiOperation;
import org.jdom2.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping(value = "/wxPay")
public class WXPayController {

    private Logger logger = LoggerFactory.getLogger(WXPayController.class);

    @Autowired
    private OrderService orderService;

    @ApiOperation(value = "微信支付接口", notes = "微信支付接口", httpMethod = "POST")
    @RequestMapping(value = "/pay", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject pay(HttpServletRequest request, HttpServletResponse response,
                          @RequestParam("orderId") String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            return RespMsg.NOTNULLPARAM();
        }
        OrderEntity orderInfo = orderService.getOrderInfoByOrderId(orderId);
        if (null == orderInfo) {
            return RespMsg.FAIL("无此订单，支付失败");
        }
        if (orderInfo.getPayState() != null && orderInfo.getPayState() == Constants.ORDER_STATE_PAYED) {
            return RespMsg.FAIL("此订单已支付");
        }
        try {
            JSONObject object = getPayStateFromWx(orderId);
            if (object.get("code") == ConstVar.SUCCESS) {
                boolean flag = orderService.updateOrderPayState(orderInfo, Constants.ORDER_PAY_CHANNEL_WX);
                if (flag) {
                    return RespMsg.SUCCESS();
                }
                return RespMsg.FAIL("订单处理出错，请联系管理员");
            }
            try {
                String text = weixinPay(orderId);
                logger.info("*********************************成功获取二维码url******************************************************" + text);
                return RespMsg.SUCCESS(text);
//                /* 根据url来生成二维码*/
//                int width = 300;
//                int height = 300;
//
//                /* 二维码格式 */
//                String format = "gif";
//                Hashtable hints = new Hashtable();
//
//                /* 内容所用编码*/
//                hints.put(EncodeHintType.CHARACTER_SET,"utf-8");
//                BitMatrix bitMatrix;
//                bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE,width,height,hints);
//                MatrixToImageWriter.writeToStream(bitMatrix,format,response.getOutputStream());
//                return null;
            } catch (Exception ex) {
                ex.printStackTrace();
                return RespMsg.FAIL("获取二维码失败，请重试");
            }
        } catch (JDOMException e) {
            e.printStackTrace();
            return RespMsg.FAIL("获取二维码失败，请重试");
        } catch (IOException e) {
            e.printStackTrace();
            return RespMsg.FAIL("获取二维码失败，请重试");
        }
    }

    @ApiOperation(value = "微信回调接口", notes = "微信回调接口", httpMethod = "GET")
    @RequestMapping(value = "/Notify", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject Notify(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("*********************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************************");
        boolean flag = false;
        //读取参数
        InputStream inputStream;
        StringBuffer sb = new StringBuffer();
        BufferedOutputStream out = null;
        inputStream = request.getInputStream();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        try {
            while ((s = in.readLine()) != null) {
                sb.append(s);
            }
            in.close();
            inputStream.close();
            //解析xml成map
            Map<String, String> m = new HashMap<String, String>();
            try {
                m = XMLUtil.doXMLParse(sb.toString());
            } catch (JDOMException e) {
                e.printStackTrace();
            }
            System.out.println("====支付回调xml文件==" + sb.toString());
            //过滤空 设置 TreeMap
            SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
            Iterator it = m.keySet().iterator();
            while (it.hasNext()) {
                String parameter = (String) it.next();
                String parameterValue = m.get(parameter);
                String v = "";
                if (null != parameterValue) {
                    v = parameterValue.trim();
                }
                packageParams.put(parameter, v);
            }
            // 账号信息
            String key = PayConfigUtil.API_KEY; //key
            //判断签名是否正确
            if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
                String resXml = "";
                if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
                    // 这里是支付成功，跟自己的业务逻辑
                    System.out.println("支付成功");
                    System.out.println("************************************************************************************************************");

                    String orderId = m.get("out_trade_no");
                    System.out.println(orderId);
                    // 根据返回的orderId获取响应的订单信息
                    OrderEntity order = orderService.getOrderInfoByOrderId(orderId);
                    // 支付成功，更改本地的订单状态
                    flag = orderService.updateOrderPayState(order, Constants.ORDER_PAY_CHANNEL_WX);
                    if (!flag) {
                        logger.error(String.format("微信支付成功，更改本地状态失败,id:%s*****orderId:%s", order.getId(), order.getOrderId()));
                    }
                    System.out.println("业务执行结束");
                    //通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
                    resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
                            + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                } else {
                    resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
                            + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                }
                out = new BufferedOutputStream(response.getOutputStream());
                out.write(resXml.getBytes());
                out.flush();
                out.close();
                if (flag) {
                    return RespMsg.SUCCESS();
                }
                return RespMsg.FAIL();
            } else {
                System.out.println("通知签名验证失败");
                return RespMsg.FAIL();
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return RespMsg.FAIL();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception ex) {
                logger.error(ex.getMessage());
            }
        }
    }


    /**
     * 微信接口，查询订单状态
     *
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    @ApiOperation(value = "获取微信订单支付状态", notes = "获取微信订单支付状态", httpMethod = "POST")
    @RequestMapping(value = "/getPayState", method = {RequestMethod.GET, RequestMethod.POST})
    public JSONObject getPayState(HttpServletRequest request,
                                  @RequestParam(value = "orderId") String orderId) throws JDOMException, IOException {
        if (StringUtils.isEmpty(orderId)) {
            return RespMsg.NOTNULLPARAM();
        }
        OrderEntity orderInfo = orderService.getOrderInfoByOrderId(orderId);
        if (orderInfo != null && orderInfo.getPayState() == Constants.ORDER_STATE_PAYED) {
            return RespMsg.SUCCESS("支付成功");
        }
        return RespMsg.FAIL();
    }


    /**
     * 执行微信支付方法，获取生成二维码的链接url
     *
     * @param orderId
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private String weixinPay(String orderId) throws JDOMException, IOException {
        //根据 orderId，从后台获取
        OrderEntity order = orderService.getOrderInfoByOrderId(orderId);
        Double price = order.getItemAmount();
        if (StringUtils.isEmpty(price)) {
            price = 0.0;
        }
        String order_price = String.valueOf(price * 100);//单位 分
        String body = order.getItemName();  //商品名称
        String out_trade_no = orderId; //订单号

        String appid = PayConfigUtil.appid;
        String mch_id = PayConfigUtil.MCH_ID;
        String key = PayConfigUtil.API_KEY;

        String currTime = PayCommonUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayCommonUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;
        String spbill_create_ip = PayConfigUtil.CREATE_IP;//获取发起电脑ip
        String notify_url = PayConfigUtil.NOTIFY_URL; //回调地址接口

        SortedMap<Object, Object> packageParams = new TreeMap<>();
        packageParams.put("appid", appid);
        packageParams.put("mch_id", mch_id);
//        packageParams.put("device_info",PayConfigUtil.DEVICE_INFO);
        packageParams.put("nonce_str", nonce_str);
        packageParams.put("body", body);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("total_fee", order_price.split("\\.")[0]);
        packageParams.put("spbill_create_ip", spbill_create_ip);
        packageParams.put("notify_url", notify_url);
        packageParams.put("trade_type", PayConfigUtil.TRADE_TYPE);
        String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
        packageParams.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        System.out.println(requestXML);
        String resXml = HttpUtil.postData(PayConfigUtil.UFDODER_URL, requestXML);
        System.out.println(resXml);
        Map map = XMLUtil.doXMLParse(resXml);
        String urlCode = (String) map.get("code_url");
        return urlCode;
    }


    /**
     * 从微信获取支付状态
     *
     * @param orderId
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    private JSONObject getPayStateFromWx(String orderId) throws JDOMException, IOException {
        SortedMap<Object, Object> packageParams = new TreeMap<>();

        String currTime = PayCommonUtil.getCurrTime();
        String strTime = currTime.substring(8, currTime.length());
        String strRandom = PayCommonUtil.buildRandom(4) + "";
        String nonce_str = strTime + strRandom;
        String out_trade_no = orderId;

        packageParams.put("appid", PayConfigUtil.appid);
        packageParams.put("mch_id", PayConfigUtil.MCH_ID);
        packageParams.put("out_trade_no", out_trade_no);
        packageParams.put("nonce_str", nonce_str);
        String sign = PayCommonUtil.createSign("UTF-8", packageParams, PayConfigUtil.API_KEY);
        packageParams.put("sign", sign);
        String requestXML = PayCommonUtil.getRequestXml(packageParams);
        String resXml = HttpUtil.postData(PayConfigUtil.QUERY_ORDER_STATE, requestXML);
        System.out.println(resXml);
        Map xml2map = XMLUtil.doXMLParse(resXml);

        if (xml2map.get("return_code").toString() != "SUCCESS" && !"SUCCESS".equals(xml2map.get("return_code").toString())) {
            return RespMsg.FAIL(-7, xml2map.get("return_msg"));
        } else if (xml2map.get("result_code").toString() != "SUCCESS" && !"SUCCESS".equals(xml2map.get("result_code").toString())) {
            return RespMsg.FAIL(-7, xml2map.get("err_code_des"));
        } else if (xml2map.get("trade_state").toString() != "SUCCESS" && !"SUCCESS".equals(xml2map.get("trade_state").toString())) {
            if (xml2map.get("trade_state").toString().equals("NOTPAY")) {
                return RespMsg.FAIL(-6, "订单未支付，请重新支付");
            } else if (xml2map.get("trade_state").toString().equals("CLOSED")) {
                return RespMsg.FAIL(-7, "订单已经关闭,请重新支付");
            } else if (xml2map.get("trade_state").toString().equals("USERPAYING")) {
                return RespMsg.FAIL(-6, "正在支付中");
            } else if (xml2map.get("trade_state").toString().equals("PAYERROR")) {
                logger.info("微信支付失败== == == " + orderId);
                return RespMsg.FAIL(-7, "支付失败，请重新申请支付");
            }
        } else {
            return RespMsg.SUCCESS("微信支付成功");
        }
        return RespMsg.FAIL();
    }

    @RequestMapping(value = "/weixinPayQr", method = {RequestMethod.POST, RequestMethod.GET})
    public void weixinPayQr(HttpServletRequest request, HttpServletResponse response) {
        // 判断用户是否登录
        try {
            logger.info("微信生成二维码,{}");
            String url = request.getParameter("url");
            if (StringUtils.isEmpty(url)) {
                return;
            }
            int width = 300;
            int height = 300;
            //二维码的图片格式
            String format = "gif";
            Hashtable hints = new Hashtable();
            //内容所使用编码
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url,
                    BarcodeFormat.QR_CODE, width, height, hints);
            //生成二维码
            File outputFile = new File("d:" + File.separator + "new.gif");
            BufferedImage bufImg = QRUtil.toBufferedImage(bitMatrix);
            ImageIO.write(bufImg, "jpg", response.getOutputStream());

        } catch (Exception e) {
            logger.error("微信生成二维码,{}", e.getMessage());
            e.printStackTrace();
        }

    }
}
