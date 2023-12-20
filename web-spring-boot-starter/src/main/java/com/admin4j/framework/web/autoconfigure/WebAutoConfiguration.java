package com.admin4j.framework.web.autoconfigure;

import com.admin4j.framework.web.controller.Admin4jErrorController;
import com.admin4j.framework.web.exception.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author andanyang
 * @since 2023/11/16 13:20
 */
@AutoConfiguration(before = ErrorMvcAutoConfiguration.class)
@RequiredArgsConstructor
public class WebAutoConfiguration {

    private final ServerProperties serverProperties;

    /**
     * 404 错误
     *
     * @param errorAttributes
     * @param errorViewResolvers
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(
            value = {ErrorController.class}
    )
    public Admin4jErrorController basicErrorController(ErrorAttributes errorAttributes, ObjectProvider<ErrorViewResolver> errorViewResolvers) {
        return new Admin4jErrorController(errorAttributes, this.serverProperties.getError(), (List) errorViewResolvers.orderedStream().collect(Collectors.toList()));
    }

    /**
     * 全局错误
     *
     * @return
     */
    @Bean("admin4jGlobalExceptionHandler")
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
