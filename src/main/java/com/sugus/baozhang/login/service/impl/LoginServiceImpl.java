package com.sugus.baozhang.login.service.impl;

import com.sugus.baozhang.login.bean.CacheUser;
import com.sugus.baozhang.login.exception.LoginException;
import com.sugus.baozhang.login.exception.VarifyException;
import com.sugus.baozhang.login.service.LoginService;
import com.sugus.baozhang.user.dto.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    private static final Logger LOOGER = LoggerFactory.getLogger(LoginServiceImpl.class);

    private static final long VERIFY_TTL = 60;

    @Override
    public CacheUser login(String userName, String password, HttpServletRequest request) {
// 获取Subject实例对象，用户实例
        Subject currentUser = SecurityUtils.getSubject();

        // 将用户名和密码封装到UsernamePasswordToken
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password);

        CacheUser cacheUser;
        // 4、认证
        try {
            // 验证码校验
            varify(request);
            // 传到 MyShiroRealm 类中的方法进行认证
            currentUser.login(token);
            // 构建缓存用户信息返回给前端
            User user = (User) currentUser.getPrincipals().getPrimaryPrincipal();
            cacheUser = new CacheUser();
            cacheUser.setToken(currentUser.getSession().getId().toString());
            BeanUtils.copyProperties(user, cacheUser);
            LOOGER.warn("CacheUser is {}", cacheUser.toString());
        } catch (UnknownAccountException e) {
            LOOGER.error("账户不存在异常：", e);
            throw new LoginException("账号不存在!", e);
        } catch (IncorrectCredentialsException e) {
            LOOGER.error("凭据错误（密码错误）异常：", e);
            throw new LoginException("密码不正确!", e);
        } catch (AuthenticationException e) {
            LOOGER.error("身份验证异常:", e);
            throw new LoginException("用户验证失败!", e);
        } catch (VarifyException e) {
            LOOGER.error("验证码输入异常:", e);
            throw new LoginException("验证码输入异常!", e);
        }
        return cacheUser;
    }

    /**
     * 验证码校验
     * @param request   请求参数
     * @throws VarifyException  VarifyException
     */
    private void varify(HttpServletRequest request) throws VarifyException {
        String verifyCode = request.getParameter("verifyCode");
        String rightCode = (String) request.getSession().getAttribute("verifyCode");
        Long verifyCodeTTL = (Long) request.getSession().getAttribute("verifyCodeTTL");
        Long currentMillis = System.currentTimeMillis();
        if (rightCode == null || verifyCodeTTL == null) {
            throw new VarifyException("请刷新图片，输入验证码！");
        }
        long expiredTime = (currentMillis - verifyCodeTTL) / 1000;
        if (expiredTime > VERIFY_TTL) {
            throw new VarifyException("验证码过期，请刷新图片重新输入！");
        }
        if (!verifyCode.equalsIgnoreCase(rightCode)) {
            throw new VarifyException("验证码错误，请刷新图片重新输入！");
        }
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }
}
