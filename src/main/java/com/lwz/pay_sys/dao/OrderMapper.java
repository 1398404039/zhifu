package com.lwz.pay_sys.dao;

import com.lwz.pay_sys.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface OrderMapper {
    int deleteByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);

    int insert(OrderEntity record);

    OrderEntity selectByPrimaryKey(@Param("id") Integer id, @Param("orderId") String orderId);

    List<OrderEntity> selectAll();

    int updateByPrimaryKey(OrderEntity record);

    int updateSelective(OrderEntity order);
}