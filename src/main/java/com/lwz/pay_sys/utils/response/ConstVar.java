package com.lwz.pay_sys.utils.response;

import java.nio.charset.Charset;

/**
 * 一些状态变量
 *
 * @author lwz
 * @date 2018年5月29日
 */
public enum ConstVar {

    SUCCESS(200, "成功"), FAIL(-1, "失败"), PWDERROR(-2, "密码错误"), OPERSUCC(100, "操作成功"), OPERFAIL(-100, "操作失败"),
    EXISTSUSER(101, "用户已存在"), SMSFAIL(102, "验证码错误"), SMSOVERDUE(103, "验证码已过期"),
    UNSMSOVERDUE(104, "您的验证码未失效"), NOTNULLPARAM(105, "参数不能为空"), USERDISABLE(106, "该帐号被禁用"), TIMEOVER(999, "申请次数超限"), COUNTRYSAME(990, "该国家已申请，不能重复申请"),
    LOGINFAIL(107, "用户名或密码错误"), PLEASELOGIN(108, "请登录账户"), PWDCOMPARE(109, "两次密码不一致"), COMMON_PARAMETER_ERROR(420, "参数异常"), COMMON_SERVER_ERROR(500, "后台异常"),
    EXISTPHONENUMBER(110, "手机号已注册"), PHONENOTREGISTERED(111, "手机号未注册"), NOAUTHORIZATION(112, "没有权限"),
    SIGNERROR(204, "签名失败");
    /*常量~*/
    public static final String ENCODING = Charset.forName("UTF-8").name();

    public static final String SESSIONUSER = "session-user";
    public static final String SMSCODE = "sms";
    public static final String SMSOFForGetPWD = "sms_pwd";

    public static final int SUCCESSCODE = 0xC8;

    public static final int FAILCODE = -1;
    public static final int ZERO = 0x0;

    private ConstVar(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String msg;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static void main(String[] args) {
        System.out.println(ConstVar.FAIL.getCode() + "   " + ConstVar.SUCCESS.getMsg());
        System.out.println(ConstVar.ENCODING);
        System.out.println(ConstVar.SESSIONUSER);
        System.out.println(ConstVar.SMSCODE);
        System.out.println(ConstVar.SMSOFForGetPWD);
        System.out.println(ConstVar.SUCCESSCODE);
        System.out.println(ConstVar.FAILCODE);
        System.out.println(ConstVar.ZERO);
    }

}
