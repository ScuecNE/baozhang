package com.sugus.baozhang.login.service;

import com.sugus.baozhang.login.bean.CacheUser;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {

    CacheUser login(String userName, String password, HttpServletRequest request);

    void logout();
}
