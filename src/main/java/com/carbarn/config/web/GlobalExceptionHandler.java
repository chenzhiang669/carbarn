package com.carbarn.config.web;

import cn.dev33.satoken.exception.NotLoginException;
import com.carbarn.common.exception.ServiceException;
import com.carbarn.common.exception.enums.ErrorCode;
import com.carbarn.common.pojo.CommonResult;
import com.carbarn.inter.utils.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public AjaxResult handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        return AjaxResult.error("Missing required parameter: " + ex.getParameterName());
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseBody
    public AjaxResult handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        return AjaxResult.error("Missing required parameter: " + ex.getRequestPartName());
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    @ResponseBody
    public AjaxResult handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        return AjaxResult.error("headers Missing required parameter: " + ex.getHeaderName());
    }

    @ExceptionHandler(value = Exception.class)
    public CommonResult<String> handleException(Exception ex, HttpServletRequest request) {
        if (ex instanceof NotLoginException) {
            LOGGER.warn("登录信息失效，请重新登录", ex);
            return CommonResult.error(ErrorCode.UNAUTHORIZED);
        }
        LOGGER.error("===========全局统一异常处理==========={},异常请求==={},参数{}", ex, request.getRequestURI(), request.getQueryString());
        CommonResult<String> result;
        if (ex instanceof HttpRequestMethodNotSupportedException) {
            result = CommonResult.error(ErrorCode.METHOD_NOT_ALLOWED);
        } else if (ex instanceof ServiceException) {
            result = CommonResult.error(((ServiceException) ex).getCode(), ex.getMessage());
        } else if (ex instanceof MissingServletRequestParameterException) {
            result = CommonResult.error(ErrorCode.BAD_REQUEST.getCode(), ex.getMessage());
        } else if (ex instanceof IllegalArgumentException) {
            result = CommonResult.error(ErrorCode.BAD_REQUEST.getCode(), ex.getMessage());
        } else if (ex instanceof MethodArgumentNotValidException) {
            result = CommonResult.error(ErrorCode.BAD_REQUEST.getCode(),
                    Objects.requireNonNull(((MethodArgumentNotValidException) ex).getBindingResult().getFieldError()).getDefaultMessage());
        } else if (ex instanceof MultipartException) {
            result = CommonResult.error(ErrorCode.BAD_REQUEST.getCode(), "文件上传格式错误");
        } else {
            result = CommonResult.error(ErrorCode.UNKNOWN);
        }
        return result;
    }


}