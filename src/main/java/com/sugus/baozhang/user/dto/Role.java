package com.sugus.baozhang.user.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "role")
public class Role {

    /**
     * 主键id
     */
    @Id
    private Integer roleId;

    /**
     * 角色标识程序中判断使用,如"admin",这个是唯一的
     */
    private String role;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 角色 -- 权限关系：多对多关系
     */
    private List<Permission> permissions;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}
