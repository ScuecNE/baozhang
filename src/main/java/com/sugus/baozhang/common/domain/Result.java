package com.sugus.baozhang.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Result {

    private Integer code;

    private String msg;

    private Object data;

}
