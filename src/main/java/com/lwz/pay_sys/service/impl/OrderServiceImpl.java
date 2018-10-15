package com.lwz.pay_sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lwz.pay_sys.constant.Constants;
import com.lwz.pay_sys.dao.OrderMapper;
import com.lwz.pay_sys.dao.PartMapper;
import com.lwz.pay_sys.entity.OrderEntity;
import com.lwz.pay_sys.entity.Part;
import com.lwz.pay_sys.entity.vo.Response;
import com.lwz.pay_sys.service.OrderService;
import com.lwz.pay_sys.utils.http.HttpTool;
import com.lwz.pay_sys.utils.response.ConstVar;
import com.lwz.pay_sys.utils.response.RespMsg;
import com.lwz.pay_sys.utils.uuid.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service(value = "orderService")
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private PartMapper partMapper;

    @Override
    public OrderEntity createOrder(String itemId, String itemName, Double itemAmount, Integer orderType, String appId) {
        OrderEntity order = new OrderEntity();
        order.setOrderId(UUIDUtil.getUUID());
        order.setCreateTime(new Date());
        order.setItemId(itemId);
        order.setItemName(itemName);
        order.setItemAmount(itemAmount);
        order.setOrderType(orderType);
        order.setAppId(appId);
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
    public boolean transferOrderResult(String orderId) {
        if (StringUtils.isEmpty(orderId)) {
            return false;
        }
        Part part = partMapper.getPartInfoByOrderInfo(orderId);
        String notify_url = part.getNotifyUrl();
        if (StringUtils.isEmpty(notify_url)) {
            logger.error(String.format("订单:%s 系统：%s 没有配置回调地址", orderId, part.getApplicationName()));
            return false;
        }
        String result = HttpTool.httpPostRequest(notify_url + "?orderId=" + orderId);
        Response response = JSONObject.parseObject(result,Response.class);
        if(response != null && response.getCode() == ConstVar.SUCCESS.getCode() && response.getData().equals(ConstVar.SUCCESS.getMsg())){
            return true;
        }
        return false;
    }

    /**
     * 更新本地订单状态
     *
     * @param order
     * @param payType 支付方式  1：微信支付   2：支付宝支付
     * @return
     */
    @Override
    public boolean updateOrderPayState(OrderEntity order, Integer payType) {
        order.setPayType(payType);
        order.setPayState(Constants.ORDER_STATE_PAYED);
        order.setPayTime(new Date());
        //更新订单状态，
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
