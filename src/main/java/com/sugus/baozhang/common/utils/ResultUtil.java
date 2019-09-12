package com.sugus.baozhang.common.utils;

import com.sugus.baozhang.common.domain.Result;
import org.springframework.http.HttpStatus;

public class ResultUtil {

    /**
     * 默认成功响应码
     */
    public static final Integer DEAFAULT_SUCCESS_CODE = HttpStatus.OK.value();
    /**
     * 默认成功响应信息
     */
    public static final String DEAFAULT_SUCCESS_MSG = "请求/处理成功！";
    /**
     * 默认失败响应码
     */
    public static final Integer DEAFAULT_FAILURE_CODE = HttpStatus.INTERNAL_SERVER_ERROR.value();
    /**
     * 默认失败响应信息
     */
    public static final String DEAFAULT_FAILURE_MSG = "请求/处理失败！";


    public static Result success(Object data) {
        return success(DEAFAULT_SUCCESS_CODE, DEAFAULT_SUCCESS_MSG, data);
    }

    public static Result success(Integer code) {
        return success(code, DEAFAULT_SUCCESS_MSG, null);
    }

    public static Result success(String msg) {
        return success(DEAFAULT_SUCCESS_CODE, msg, null);
    }

    public static Result success(String msg, Object data) {
        return success(DEAFAULT_SUCCESS_CODE, msg, data);
    }

    public static Result success(Integer code, Object data) {
        return success(code, DEAFAULT_SUCCESS_MSG, data);
    }

    public static Result success(Integer code, String msg, Object object) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(object);
        return result;
    }

    public static Result failure(String msg) {
        return failure(DEAFAULT_FAILURE_CODE, msg, null);
    }

    public static Result failure(Integer code) {
        return failure(code, DEAFAULT_FAILURE_MSG, null);
    }

    public static Result failure(Object data) {
        return failure(DEAFAULT_FAILURE_CODE, DEAFAULT_FAILURE_MSG, data);
    }

    public static Result failure(Integer code, String msg) {
        return failure(code, msg, null);
    }

    public static Result failure() {
        return failure(DEAFAULT_FAILURE_CODE, DEAFAULT_FAILURE_MSG, null);
    }

    public static Result failure(Integer code, String msg, Object data) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

}
