package com.lwz.pay_sys.service;

import com.lwz.pay_sys.entity.OrderEntity;
import com.lwz.pay_sys.entity.vo.Response;

public interface OrderService {
    OrderEntity createOrder(String itemId, String itemName, Double itemAmount, Integer orderType,String appId);

    OrderEntity getOrderInfoByOrderId(String orderId);

    boolean updateOrderPayState(OrderEntity orderInfo, Integer orderPayChannelWx);

    boolean transferOrderResult(String orderId);
}
