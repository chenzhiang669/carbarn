package com.carbarn.inter.utils.qixiubao.update;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

public class LocalRunForbidenLog4j {
    public static void forbidenlog() {
        Logger wireLogger = (Logger) LoggerFactory.getLogger("org.apache.http.wire");
        wireLogger.setLevel(Level.OFF); // 禁用日志输出

        Logger headersLogger = (Logger) LoggerFactory.getLogger("org.apache.http.headers");
        headersLogger.setLevel(Level.OFF); // 禁用日志输出
        Logger headersimpl = (Logger) LoggerFactory.getLogger("org.apache.http.impl");
        headersimpl.setLevel(Level.OFF); // 禁用日志输出
        Logger headersclient = (Logger) LoggerFactory.getLogger("org.apache.http.client");
        headersclient.setLevel(Level.OFF); // 禁用日志输出
    }
}
