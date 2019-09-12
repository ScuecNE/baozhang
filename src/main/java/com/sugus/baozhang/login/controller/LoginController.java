package com.sugus.baozhang.login.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.sugus.baozhang.common.domain.Result;
import com.sugus.baozhang.common.utils.ResultUtil;
import com.sugus.baozhang.login.bean.CacheUser;
import com.sugus.baozhang.login.service.LoginService;
import com.sugus.baozhang.user.dto.User;
import com.sugus.baozhang.user.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
public class LoginController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Resource
    DefaultKaptcha defaultKaptcha;

    @PostMapping("/login")
    public Result login(User user, HttpServletRequest request) {
        LOGGER.info("进入登录.....");

        String email = user.getEmail();
        String password = user.getPassword();

        if (StringUtils.isBlank(email)) {
            return ResultUtil.failure("邮箱为空！");
        }
        if (StringUtils.isBlank(password)) {
            return ResultUtil.failure("密码为空！");
        }

        User userByEmail = userService.findByEmail(email);
        if (null == userByEmail) {
            return ResultUtil.failure( "用户名为空");
        }
        String userName = userByEmail.getUserName();
        CacheUser loginUser = loginService.login(userName, password, request);
        // 登录成功返回用户信息
        return ResultUtil.success("登录成功！", loginUser);
    }

    @GetMapping("/logout")
    public Result logOut() {
        loginService.logout();
        return ResultUtil.success("登出成功！");
    }

    @RequestMapping("/un_auth")
    public Result unAuth() {
        return ResultUtil.failure(HttpStatus.UNAUTHORIZED.value(), "用户未登录！", null);
    }

    @RequestMapping("/unauthorized")
    public Result unauthorized() {
        return ResultUtil.failure(HttpStatus.UNAUTHORIZED.value(), "用户无权限！", null);
    }

    @RequestMapping("/getVerifyCode")
    public void defaultKaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] bytesCaptchaImg;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 产生验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            request.getSession().setAttribute("verifyCode", createText);
            request.getSession().setAttribute("verifyCodeTTL", System.currentTimeMillis());
            // 使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage bufferedImage = defaultKaptcha.createImage(createText);
            ImageIO.write(bufferedImage, "jpg", jpegOutputStream);
        } catch (Exception e) {
            LOGGER.error(ResultUtil.DEAFAULT_FAILURE_MSG, e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            jpegOutputStream.close();
            return;
        }
        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        bytesCaptchaImg = jpegOutputStream.toByteArray();
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(bytesCaptchaImg);
        responseOutputStream.flush();
        responseOutputStream.close();
        jpegOutputStream.close();
    }

}
