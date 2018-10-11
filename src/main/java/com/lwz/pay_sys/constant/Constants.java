package com.lwz.pay_sys.constant;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Constants {

    /**
     * 订单支付状态   1：已支付    0：未支付
     */
    public static final int ORDER_STATE_PAYED = 1;
    public static final int ORDER_STATE_NOT_PAY = 0;

    /**
     * 订单支付方式  1：微信     2：支付宝
     */
    public static final int ORDER_PAY_CHANNEL_WX = 1;
    public static final int ORDER_PAY_CHANNEL_ZFB = 2;


    public static final String LOCALHOST = "http://" + "39.106.145.162" + ":" + "8080";
    /**
     * 共有线程池，发送短信
     */
    public static final ExecutorService threadPool = Executors.newFixedThreadPool(10);
}
