package com.sugus.baozhang.user.dto;

import com.sugus.baozhang.common.utils.EncryptedUtil;
import lombok.*;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {

    /**
     * 用户名（uuid）
     */
    @Id
    private String userName;

    /**
     * 登录名/邮箱地址
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 电话
     */
    private String phone;

    /**
     * 用户名称/昵称
     */
    private String name;

    /**
     * 生日日期
     */
    private Date birthday;

    /**
     * 年龄（通过生日日期计算）
     */
    private Integer age;

    /**
     * 性别（0：女，1：男）
     */
    private Integer sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 个性标签，（,分割）
     */
    private String tags;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 用户状态,0:创建未认证（未激活，没有输入验证码等等）, 1:正常状态,2：用户被锁定.
     */
    private Integer state;

    /**
     * 角色列表，多对多
     */
    private List<Role> roles;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * User初始化方法
     * @param user  user
     */
    public static void init(User user) {
        user.setUserName(UUID.randomUUID().toString());
        user.setSalt(EncryptedUtil.getMD5(user.getUserName()));
        user.setPassword(new Md5Hash(user.getPassword(), user.getSalt(), 2).toString());
        user.setVerificationCode(String.valueOf(new Random().ints(1, 1000, 10000).toArray()[0]));
        user.setState(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
    }

}
