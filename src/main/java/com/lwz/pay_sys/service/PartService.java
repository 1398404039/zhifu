package com.lwz.pay_sys.service;

import com.lwz.pay_sys.entity.Page;
import com.lwz.pay_sys.entity.Part;

public interface PartService {
    boolean insert(Part part);

    boolean deleteById(Integer id);

    Page<Part> getPartsPage(Integer pageNum, Integer pageSize, String applicationName);

    Part getPartInfo(Integer id);

    boolean update(Part part);

    Part getPartInfoByAppId(String appId);
}
