package com.lwz.pay_sys.controller.alipay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.lwz.pay_sys.constant.Constants;
import com.lwz.pay_sys.entity.OrderEntity;
import com.lwz.pay_sys.service.OrderService;
import com.lwz.pay_sys.utils.aliUtils.AlipayConfig;
import com.lwz.pay_sys.utils.response.RespMsg;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/aliPay")
@Controller
public class AlipayController {

    private Logger logger = LoggerFactory.getLogger(AlipayController.class);

    @Autowired
    private OrderService orderService;

    /**
     * 阿里支付申请
     * 参数详情 官网：https://docs.open.alipay.com/common/105901
     */
    @RequestMapping(value = "/pay", method = {RequestMethod.POST})
    public void alipay(HttpServletRequest httpRequest, HttpServletResponse httpResponse, @ApiParam(name = "orderId", value = "orderId", required = true) @RequestParam("orderId") String orderId) {
        //订单id
        if (StringUtils.isEmpty(orderId)) {
            return;
        }
        OrderEntity order = orderService.getOrderInfoByOrderId(orderId);
        if (null == order) {
            return;
        }
        String out_trade_no = order.getOrderId();
        String total_amount = order.getItemAmount() + "";
        String subject = order.getItemName();
        String body = order.getItemName();
        String passback_params = order.getOrderId();
        try {
            AlipayClient alipayClient = new DefaultAlipayClient(
                    AlipayConfig.gatewayUrl,
                    AlipayConfig.app_id,
                    AlipayConfig.merchant_private_key,
                    AlipayConfig.format,
                    AlipayConfig.charset,
                    AlipayConfig.alipay_public_key,
                    AlipayConfig.sign_type); //获得初始化的AlipayClient
            AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();//创建API对应的request
            alipayRequest.setReturnUrl(AlipayConfig.toSucessPage);
            alipayRequest.setNotifyUrl(AlipayConfig.paySuccessCall);//在公共参数中设置回跳和通知地址
            alipayRequest.setBizContent("{" +
                    "    \"out_trade_no\":\"" + out_trade_no + "\"," +
                    "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\"," +
                    "    \"total_amount\":" + total_amount + "," +
                    "    \"subject\":\"" + subject + "\"," +
                    "    \"body\":\"" + body + "\"," +
                    "    \"passback_params\":\"" + URLEncoder.encode(passback_params, "utf-8") + "\"" +
                    "  }");//填充业务参数
            String form = "";
            try {
                form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            } catch (AlipayApiException e) {
                e.printStackTrace();
            }
            httpResponse.setContentType("text/html;charset=" + AlipayConfig.charset);
            httpResponse.getWriter().write(form);//直接将完整的表单html输出到页面
            httpResponse.getWriter().flush();
            httpResponse.getWriter().close();
        } catch (IOException e) {
            logger.error("支付宝支付异常" + e);
            e.printStackTrace();
        }
    }

    //商户页面点支付后的操作验证接口
    @ResponseBody
    @RequestMapping(value = "/aliPayValidation", method = {RequestMethod.POST})
    public Object aliPayValidation(@RequestParam("orderId") String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            return RespMsg.NOTNULLPARAM();
        }
        logger.info("支付宝支付aliPayValidation接口：当前系统主动查询,请求参数:" + orderId);
        try {
            OrderEntity order = orderService.getOrderInfoByOrderId(orderId);
            if (null == order) {
                return RespMsg.FAIL("参数错误，未查询到该订单");
            }
            if (order.getPayState() == Constants.ORDER_STATE_PAYED) {
                return RespMsg.SUCCESS("订单已支付");
            }

            //商户订单号，商户网站订单系统中唯一订单号，必填
            AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.gatewayUrl, AlipayConfig.app_id, AlipayConfig.merchant_private_key, AlipayConfig.format, AlipayConfig.charset, AlipayConfig.alipay_public_key, AlipayConfig.sign_type);
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            request.setBizContent("{\"out_trade_no\":\"" + orderId + "\"}");
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            System.out.println("请求后测试" + response.isSuccess() + "%%%===" + response.getTradeStatus());
            if (!response.isSuccess()) {
                return RespMsg.FAIL(-2, "未支付");
            }
            //如果支付成功更新 订单状态
            if ("TRADE_SUCCESS".equals(response.getTradeStatus())) {
                Boolean flag = orderService.updateOrderPayState(order, Constants.ORDER_PAY_CHANNEL_ZFB);
                if (flag) {
                    return RespMsg.SUCCESS(orderId);
                }
            }
            return RespMsg.FAIL(-2, "未支付,请联系管理员");
        } catch (Exception e) {
            if ("orderUpdateRepeated".equals(e.getMessage())) {
                return RespMsg.SUCCESS(orderId);
            } else {
                logger.error("支付宝支付aliPayValidation接口异常 " + e);
            }
            e.printStackTrace();
        }
        return RespMsg.FAIL("订单支付失败");
    }

    //支付宝支付成功后回调接口
    @ResponseBody
    @ApiOperation(value = "paySuccessCall", httpMethod = "GET")
    @RequestMapping(value = "/paySuccessCall", method = {RequestMethod.POST, RequestMethod.GET})
    public Object AlipayAsyCallback(HttpServletRequest request) {
        //将异步通知中收到的所有参数都存放到map中
        Map<String, String> paramsMap = getPersonParameterMap(request);
        try {
            //验签
            boolean signVerified = AlipaySignature.rsaCheckV1(paramsMap, AlipayConfig.alipay_public_key, AlipayConfig.charset, AlipayConfig.sign_type); //调用SDK验证签名
            if (!signVerified) {
                logger.error("支付宝支付验签失败 requestMap=" + paramsMap);
                return "failure";
            }
            //支付成功
            if (!"TRADE_SUCCESS".equals(paramsMap.get("trade_status"))) {
                return "failure";
            }
            logger.info("支付宝支付验签成功 requestMap=" + paramsMap);
            //获取请求参数中的商户订单号和该订单总金额
            String orderId = paramsMap.get("out_trade_no");
            //在商户系统根据订单号和订单金额验证该订单是否存在
            OrderEntity order = orderService.getOrderInfoByOrderId(orderId);
            //如果存在,并且该订单是未支付的,那么获取付款状态，并更新商户系统中的订单状态
            if (null == order) {
                return "failure";
            }
            if (order.getPayState() == Constants.ORDER_STATE_PAYED) {
                return "success";
            }
            Boolean flag = orderService.updateOrderPayState(order, Constants.ORDER_PAY_CHANNEL_ZFB);
            if (flag) {
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "failure";
    }


    //公共接口，验证订单是否已支付
    @ResponseBody
    @RequestMapping(value = "/checkOrderAlreadyPay", method = {RequestMethod.POST})
    public Object checkOrderAlreadyPay(@Param("orderId") String orderId) {
        try {
            OrderEntity order = orderService.getOrderInfoByOrderId(orderId);
            if (null == order) {
                return RespMsg.FAIL("订单不存在");
            }
            if (order.getPayState() == Constants.ORDER_STATE_PAYED) {
                return RespMsg.SUCCESS("订单已支付");
            }
        } catch (Exception e) {
            logger.error("checkOrderAlreadyPay 接口查询异常 requestMap=" + orderId, e);
            e.printStackTrace();
        }
        return RespMsg.FAIL("订单未支付");
    }


    /**
     * 内部方法，从request中获得参数Map，并返回可读的Map
     *
     * @param request
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    private Map<String, String> getPersonParameterMap(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        if (CollectionUtils.isEmpty(map)) {
            return Collections.EMPTY_MAP;
        }
        Map<String, String> resMap = new HashMap<>();
        String[] array = null;
        for (String key : map.keySet()) {
            array = map.get(key);
            resMap.put(key, convertArray2String(array));
        }
        return resMap;
    }

    /**
     * 内部方法，数组转string
     *
     * @param arr
     * @return
     */
    private String convertArray2String(String[] arr) {
        if (arr == null || arr.length == 0) {
            return "";
        }
        if (arr.length == 1) {
            return arr[0];
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            builder.append(arr[i]).append(",");
        }
        String result = builder.toString();
        result = result.substring(0, result.lastIndexOf(","));
        return result;
    }
}
