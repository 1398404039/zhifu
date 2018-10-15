package com.lwz.pay_sys.service.impl;

import com.lwz.pay_sys.constant.Constants;
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
        if (flag) {
            return order;
        }
        return null;
    }

    @Override
    public OrderEntity getOrderInfoByOrderId(String orderId) {
        return orderMapper.selectByPrimaryKey(null, orderId);
    }

    @Override
    public boolean updateOrderPayState(OrderEntity orderInfo, int orderPayChannelWx) {
        return false;
    }

    /**
     * 更新本地订单状态
     *
     * @param order
     * @param payType 支付方式  1：微信支付   2：支付宝支付
     * @return
     */
    public boolean updateOrderPayState(OrderEntity order, Integer payType) {
        String redeemCode = UUIDUtil.createRedeemCode();
        order.setPayType(payType);
        order.setPayState(Constants.ORDER_STATE_PAYED);
        order.setPayTime(new Date());
        //更新订单状态，添加生成的兑换码
        boolean flag = updateOrderPayStateLocal(order);
        if (!flag) {
            return false;
        }
        return true;
    }

    private boolean updateOrderPayStateLocal(OrderEntity order) {
        return orderMapper.updateSelective(order) > 0;
    }

}
