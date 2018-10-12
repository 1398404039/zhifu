package com.lwz.pay_sys.utils.response;

import com.alibaba.fastjson.JSONObject;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RespMsg {


    /**
     * 操作成功
     */
    public static JSONObject SUCCESS() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.SUCCESS.getCode());
        RS.put("msg", ConstVar.SUCCESS.getMsg());
        return RS;
    }

    /**
     * 操作成功，带指定消息、参数返回
     *
     * @param msg
     * @param data
     * @return
     */
    public static JSONObject SUCCESS(String msg, Object data) {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.SUCCESS.getCode());
        RS.put("msg", msg);
        RS.put("data", data);
        return RS;
    }

    /**
     * 操作成功，带指定消息、参数返回
     *
     * @param data
     * @return
     */
    public static JSONObject SUCCESS(Object data) {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.SUCCESS.getCode());
        RS.put("msg", ConstVar.SUCCESS.getMsg());
        RS.put("data", data);
        return RS;

    }

    /**
     * 用户已存在
     *
     * @return
     */
    public static JSONObject EXISTSUSER() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.EXISTSUSER.getCode());
        RS.put("msg", ConstVar.EXISTSUSER.getMsg());
        RS.put("data", null);
        return RS;
    }

    /**
     * 手机号已经被注册
     *
     * @return
     */
    public static JSONObject EXISTPHONENUMBER() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.EXISTPHONENUMBER.getCode());
        RS.put("msg", ConstVar.EXISTPHONENUMBER.getMsg());
        RS.put("data", null);
        return RS;
    }

    /**
     * 操作失败
     *
     * @return
     */
    public static JSONObject FAIL() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.FAIL.getCode());
        RS.put("msg", ConstVar.FAIL.getMsg());
        RS.put("data", null);
        return RS;
    }

    /**
     * 请登陆
     *
     * @return
     */
    public static JSONObject PLEASELOGIN() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.PLEASELOGIN.getCode());
        RS.put("msg", ConstVar.PLEASELOGIN.getMsg());
        RS.put("data", null);
        return RS;
    }

    /**
     * 请登陆
     *
     * @return
     */
    public static JSONObject NOAUTHORIZATION() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.NOAUTHORIZATION.getCode());
        RS.put("msg", ConstVar.NOAUTHORIZATION.getMsg());
        RS.put("data", null);
        return RS;
    }

    /**
     * 请登陆，指定返回信息
     *
     * @param msg
     * @return
     */
    public static JSONObject FAIL(Object msg) {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.PLEASELOGIN.getCode());
        RS.put("msg", msg);
        RS.put("data", null);
        return RS;
    }

    /**
     * 登录信息已经失效，请重新登录
     *
     * @return
     */
    public static JSONObject FAILNOTSESSION() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.PLEASELOGIN.getCode());
        RS.put("msg", "登录信息已经失效，请重新登录");
        RS.put("data", null);
        return RS;
    }

    /**
     * 操作失败，返回指定返回码、消息
     *
     * @param code
     * @param msg
     * @return
     */
    public static JSONObject FAIL(int code, Object msg) {
        JSONObject RS = new JSONObject();
        RS.put("code", code);
        RS.put("msg", msg);
        RS.put("data", null);
        return RS;
    }

    /**
     * 申请次数超限，返回指定返回码、消息
     */
    public static JSONObject TIMEOVER() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.TIMEOVER.getCode());
        RS.put("msg", ConstVar.TIMEOVER.getMsg());
        RS.put("data", null);
        return RS;
    }

    /**
     * 该国家已申请过，不能再次申请
     */
    public static JSONObject COUNTRYSAME() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.COUNTRYSAME.getCode());
        RS.put("msg", ConstVar.COUNTRYSAME.getMsg());
        RS.put("data", null);
        return RS;
    }

    /**
     * 参数不能为空
     *
     * @return
     */
    public static JSONObject NOTNULLPARAM() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.NOTNULLPARAM.getCode());
        RS.put("msg", ConstVar.NOTNULLPARAM.getMsg());
        RS.put("data", null);
        return RS;
    }

    /**
     * 参数不能为空,返回指定消息
     *
     * @return
     */
    public static JSONObject NOTNULLPARAM(String msg) {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.NOTNULLPARAM.getCode());
        RS.put("msg", msg);
        RS.put("data", null);
        return RS;
    }

    /**
     * 参数不能为空,返回指定消息
     *
     * @return
     */
    public static JSONObject SIGNERROTR() {
        JSONObject RS = new JSONObject();
        RS.put("code", ConstVar.SIGNERROR.getCode());
        RS.put("msg", ConstVar.SIGNERROR.getMsg());
        RS.put("data", null);
        return RS;
    }



//	/*private static void putRS(int code,Object value){
//		RS.clear();
//		RS.put("code", code);
//		RS.put("data", value);
//	}
//
//	public static JSONObject putPage(Page page, Object value) {
//		JSONObject RS = new JSONObject();
//		RS.put("code", 200);
//		RS.put("totalResult", page.getTotalResult());//总记录数
//		RS.put("totalPage", page.getTotalPage());//总页数
//		RS.put("pageSize", page.getPageSize());//每次多少条
//		RS.put("pageNum", page.getPageNum());//当前第几页
//		RS.put("data", value);//当前页的数据
//		return RS;
//	}

    public static void main(String[] args) {

        long time = System.nanoTime();

        Date date = new Date(time);
        Format f = new SimpleDateFormat("YY-mm-dd");
        System.out.println(f.format(date));

        System.out.println(SUCCESS());
    }
}
