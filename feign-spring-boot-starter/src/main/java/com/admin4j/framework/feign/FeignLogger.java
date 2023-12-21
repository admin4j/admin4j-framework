package com.admin4j.framework.feign;

import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.util.StringUtils;

/**
 * @author andanyang
 * @since 2022/5/10 14:24
 */
public class FeignLogger implements HttpLoggingInterceptor.Logger {
    private static final Marker MARKER = MarkerFactory.getMarker("Feign");

    private final Logger logger;

    public FeignLogger() {
        this.logger = LoggerFactory.getLogger(FeignLogger.class);
    }

    public FeignLogger(Logger logger) {
        if (logger != null) {
            this.logger = logger;
        } else {
            this.logger = LoggerFactory.getLogger(FeignLogger.class);
        }
    }

    public FeignLogger(String logName) {

        if (StringUtils.hasLength(logName)) {
            this.logger = LoggerFactory.getLogger(FeignLogger.class);
        } else {
            this.logger = LoggerFactory.getLogger(logName);
        }
    }

    @Override
    public void log(String message) {

        logger.debug(MARKER, message);
    }
}
