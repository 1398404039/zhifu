package com.lwz.pay_sys.service.impl;

import com.lwz.pay_sys.dao.OrderMapper;
import com.lwz.pay_sys.entity.OrderEntity;
import com.lwz.pay_sys.service.OrderService;
import com.lwz.pay_sys.utils.uuid.UUIDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public OrderEntity createOrder(String itemId, String itemName, Double itemAmount, Integer orderType) {
        OrderEntity order = new OrderEntity();
        order.setOrderId(UUIDUtil.getUUID());
        order.setCreateTime(new Date());
        order.setItemId(itemId);
        order.setItemName(itemName);
        order.setItemAmount(itemAmount);
        order.setOrderType(orderType);
        boolean flag = orderMapper.insert(order) == 1;
        if(flag){
            return order;
        }
        return null;
    }
}
