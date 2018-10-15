package com.lwz.pay_sys.controller.part;

import com.alibaba.fastjson.JSONObject;
import com.lwz.pay_sys.entity.Page;
import com.lwz.pay_sys.entity.Part;
import com.lwz.pay_sys.service.PartService;
import com.lwz.pay_sys.utils.response.RespMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/part")
public class PartController {

    @Autowired
    private PartService partService;


    @RequestMapping(value = "/addPartInfo",method = RequestMethod.POST)
    public JSONObject addPartInfo(@RequestParam("appId") String appId,
                                  @RequestParam("appSecret") String appSecret,
                                  @RequestParam("applicationName") String applicationName,
                                  @RequestParam("notifyUrl") String notifyUrl){
        if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret) || StringUtils.isEmpty(applicationName) || StringUtils.isEmpty(notifyUrl)){
            return RespMsg.NOTNULLPARAM();
        }
        Part part = partService.getPartInfoByAppId(appId);
        if(part != null){
            return RespMsg.FAIL("指定app_id已存在，请更换app_id");
        }
        part = new Part();
        part.setAppId(appId);
        part.setAppSecrect(appSecret);
        part.setApplicationName(applicationName);
        part.setNotifyUrl(notifyUrl);
        boolean flag = partService.insert(part);
        if(flag){
            return RespMsg.SUCCESS();
        }
        return RespMsg.FAIL();
    }

    @RequestMapping(value = "/delPartInfo",method = RequestMethod.POST)
    public JSONObject delPartInfo(@RequestParam("id") Integer id){
        if(StringUtils.isEmpty(id) || id <= 0){
            return RespMsg.NOTNULLPARAM();
        }
        boolean flag = partService.deleteById(id);
        if(flag){
            return RespMsg.SUCCESS();
        }
        return RespMsg.FAIL();
    }

    @RequestMapping(value = "/getParts",method = RequestMethod.POST)
    public JSONObject getParts(@RequestParam("pageNum") Integer pageNum,
                               @RequestParam("pageSize") Integer pageSize,
                               @RequestParam("applicationName") String applicationName){
        if(pageNum == null || pageNum <= 0){
            pageNum = 1;
        }
        if(pageSize == null || pageSize <= 0){
            pageSize = 10;
        }
        Page<Part> page = partService.getPartsPage(pageNum,pageSize,applicationName);
        return RespMsg.SUCCESS(page);
    }

    @RequestMapping(value = "/getPartInfo",method = RequestMethod.POST)
    public JSONObject getPartInfo(@RequestParam("id") Integer id){
        if(StringUtils.isEmpty(id) || id <= 0){
            return RespMsg.NOTNULLPARAM();
        }
        Part part = partService.getPartInfo(id);
        return RespMsg.SUCCESS(part);
    }


    @RequestMapping(value = "/addPartInfo",method = RequestMethod.POST)
    public JSONObject addPartInfo(@RequestParam("id") Integer id,
                                  @RequestParam("appId") String appId,
                                  @RequestParam("appSecret") String appSecret,
                                  @RequestParam("applicationName") String applicationName,
                                  @RequestParam("notifyUrl") String notifyUrl){
        if(StringUtils.isEmpty(appId) || StringUtils.isEmpty(appSecret) || StringUtils.isEmpty(applicationName) || StringUtils.isEmpty(notifyUrl) || StringUtils.isEmpty(id) || id <= 0){
            return RespMsg.NOTNULLPARAM();
        }
        Part part = partService.getPartInfo(id);
        if(part == null){
            return RespMsg.FAIL("未查询到该部门");
        }
        if(!appId.equals(part.getAppId())){
            Part partDB = partService.getPartInfoByAppId(appId);
            if(partDB != null){
                return RespMsg.FAIL("指定app_id已存在，请更换app_id");
            }
        }
        part.setAppId(appId);
        part.setAppSecrect(appSecret);
        part.setApplicationName(applicationName);
        part.setNotifyUrl(notifyUrl);
        boolean flag = partService.update(part);
        if(flag){
            return RespMsg.SUCCESS();
        }
        return RespMsg.FAIL();
    }

}
