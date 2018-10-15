package com.lwz.pay_sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lwz.pay_sys.dao.PartMapper;
import com.lwz.pay_sys.entity.Page;
import com.lwz.pay_sys.entity.Part;
import com.lwz.pay_sys.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PartServiceImpl implements PartService {

    @Autowired
    private PartMapper partMapper;

    @Override
    public boolean insert(Part part) {
        return partMapper.insert(part) > 0;
    }

    @Override
    public boolean deleteById(Integer id) {
        return partMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public Page<Part> getPartsPage(Integer pageNum, Integer pageSize, String applicationName) {
        PageHelper.startPage(pageNum,pageSize);
        List<Part> list = partMapper.selectPartByName(applicationName);
        long totalSize = new PageInfo<>(list).getTotal();
        return Page.build(pageNum,pageSize,totalSize,list);
    }

    @Override
    public Part getPartInfo(Integer id) {
        return partMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean update(Part part) {
        return partMapper.updateByPrimaryKey(part) > 0;
    }

    @Override
    public Part getPartInfoByAppId(String appId) {
        if(StringUtils.isEmpty(appId)){
            return null;
        }
        return partMapper.getPartByAppId(appId);
    }
}
