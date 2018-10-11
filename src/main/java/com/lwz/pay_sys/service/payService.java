package com.lwz.pay_sys.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface payService {
    void payNotify(HttpServletRequest request, HttpServletResponse response);

    boolean queryPayState(String orderId);
}
