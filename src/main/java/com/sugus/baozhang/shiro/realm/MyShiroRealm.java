package com.sugus.baozhang.shiro.realm;

import com.sugus.baozhang.user.dto.Permission;
import com.sugus.baozhang.user.dto.Role;
import com.sugus.baozhang.user.dto.User;
import com.sugus.baozhang.user.service.UserService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        User user = (User) principalCollection.getPrimaryPrincipal();
        User currUsers = userService.findByUserName(user.getUserName());
        if (null == currUsers || CollectionUtils.isEmpty(currUsers.getRoles())) {
            return simpleAuthorizationInfo;
        }
        List<Role> userRoleList = currUsers.getRoles();
        userRoleList.forEach(userRole -> {
            simpleAuthorizationInfo.addRole(userRole.getRole());
            List<Permission> permissions = userRole.getPermissions();
            if (CollectionUtils.isEmpty(permissions)) {
                return;
            }
            permissions.forEach(permission -> simpleAuthorizationInfo.addStringPermission(permission.getPermissionName()));
        });
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();

        User user = userService.findByUserName(userName);
        if (null == user) {
            return null;
        }
        return new SimpleAuthenticationInfo(
                // 这里传入的是user对象，比对的是用户名，直接传入用户名也没错，但是在授权部分就需要自己重新从数据库里取权限
                user,
                // 密码
                user.getPassword(),
                // salt
                ByteSource.Util.bytes(user.getSalt()),
                // realm name
                getName()
        );
    }
}
