package com.lwz.pay_sys.controller.orderpublic;

import com.alibaba.fastjson.JSONObject;
import com.lwz.pay_sys.entity.OrderEntity;
import com.lwz.pay_sys.service.OrderService;
import com.lwz.pay_sys.utils.response.RespMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/createOrder",method = RequestMethod.POST)
    public JSONObject createOrder(@RequestParam(value = "itemId") String itemId,
                                  @RequestParam(value = "itemName") String itemName,
                                  @RequestParam(value = "itemAmount") Double itemAmount,
                                  @RequestParam(value = "orderType") Integer orderType){
        if(StringUtils.isEmpty(itemId) || StringUtils.isEmpty(itemName) || null == itemAmount || null == orderType){
            return RespMsg.NOTNULLPARAM();
        }
        OrderEntity order = orderService.createOrder(itemId,itemName,itemAmount,orderType);
        return RespMsg.SUCCESS(order);
    }
}
