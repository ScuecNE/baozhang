package com.sugus.baozhang.login.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CacheUser {

    private String email;

    private String name;

    private Integer state;

    private String userName;

    private String token;

}
