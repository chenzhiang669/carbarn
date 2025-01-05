package com.carbarn.inter.utils;

import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@ControllerAdvice
public class GlobalExceptionHandler {

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



}