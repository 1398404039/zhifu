package com.lwz.pay_sys.service;

import com.lwz.pay_sys.entity.OrderEntity;

public interface OrderService {
    OrderEntity createOrder(String itemId,String itemName,Double itemAmount,Integer orderType);

    OrderEntity getOrderInfoByOrderId(String orderId);

    boolean updateOrderPayState(OrderEntity orderInfo, int orderPayChannelWx);
}
