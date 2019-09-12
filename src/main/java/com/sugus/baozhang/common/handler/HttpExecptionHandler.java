package com.sugus.baozhang.common.handler;

import com.sugus.baozhang.common.domain.Result;
import com.sugus.baozhang.common.utils.ResultUtil;
import com.sugus.baozhang.login.exception.LoginException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.management.ServiceNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

@EnableWebMvc
@ControllerAdvice
public class HttpExecptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpExecptionHandler.class);

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String msg = "缺少请求参数！";
        LOGGER.error(msg, e);
        return ResultUtil.failure(msg);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        String msg = e.getMessage();
        LOGGER.error("参数解析失败：", e);
        return ResultUtil.failure(msg);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = handleBindingResult(e.getBindingResult());
        LOGGER.error("方法参数无效: ", e);
        return ResultUtil.failure(msg);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result handleBindException(BindException e) {
        String msg = handleBindingResult(e.getBindingResult());
        LOGGER.error("参数绑定失败:", e);
        return ResultUtil.failure(msg);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleServiceException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String msg = violations.iterator().next().getMessage();
        LOGGER.error("参数验证失败:", e);
        return ResultUtil.failure(msg);
    }

    /**
     * 400 - Bad Request
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public Result handleValidationException(ValidationException e) {
        String msg = e.getMessage();
        LOGGER.error("参数验证失败：", e);
        return ResultUtil.failure(msg);
    }

    /**
     * 401 - Unauthorized
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(LoginException.class)
    public Result handleLoginException(LoginException e) {
        String msg = e.getMessage();
        LOGGER.error("登录异常：", e);
        return ResultUtil.failure(msg);
    }

    /**
     * 403 - Unauthorized
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(UnauthorizedException.class)
    public Result handleLoginException(UnauthorizedException e) {
        LOGGER.error("用户无权限：", e);
        return ResultUtil.failure(HttpStatus.FORBIDDEN.value(), "用户无权限!", null);
    }

    /**
     * 405 - Method Not Allowed
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        String msg = "不支持当前请求方法！";
        LOGGER.error(msg, e);
        return ResultUtil.failure(msg);
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public Result handleHttpMediaTypeNotSupportedException(Exception e) {
        String msg = "不支持当前媒体类型！";
        LOGGER.error(msg, e);
        return ResultUtil.failure(msg);
    }

    /**
     * 422 - UNPROCESSABLE_ENTITY
     */
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public Result handleMaxUploadSizeExceededException(Exception e) {
        String msg = "所上传文件大小超过最大限制，上传失败！";
        LOGGER.error(msg, e);
        return ResultUtil.failure(msg);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(ServiceNotFoundException.class)
    public Result handleServiceException(ServiceNotFoundException e) {
        String msg = "服务内部异常：" + e.getMessage();
        LOGGER.error(msg, e);
        return ResultUtil.failure(msg);
    }

    /**
     * 500 - Internal Server Error
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        String msg = "服务内部异常！" + e.getMessage();
        LOGGER.error(msg, e);
        return ResultUtil.failure(msg);
    }

    /**
     * 处理参数绑定异常，并拼接出错的参数异常信息。
     * @param result result
     */
    private String handleBindingResult(BindingResult result) {
        if (result.hasErrors()) {
            final List<FieldError> fieldErrors = result.getFieldErrors();
            return fieldErrors.iterator().next().getDefaultMessage();
        }
        return null;
    }

}
