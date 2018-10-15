package com.lwz.pay_sys.dao;

import com.lwz.pay_sys.entity.Part;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface PartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Part record);

    Part selectByPrimaryKey(Integer id);

    List<Part> selectAll();

    int updateByPrimaryKey(Part record);

    List<Part> selectPartByName(@Param("applicationName") String applicationName);

    Part getPartByAppId(@Param("appId") String appId);
}