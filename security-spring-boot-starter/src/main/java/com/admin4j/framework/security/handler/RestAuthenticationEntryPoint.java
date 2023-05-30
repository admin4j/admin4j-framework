package com.admin4j.framework.security.handler;

import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import com.admin4j.common.util.ServletUtils;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author andanyang
 * @since 2022/3/24 16:34
 * 自定义返回结果：未登录或登录过期
 */
@Slf4j
@ConditionalOnMissingBean(AuthenticationEntryPoint.class)
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final IResponse FAIL_AUTH = new SimpleResponse(ResponseEnum.FAIL_AUTH_TOKEN);

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException)
            throws IOException, ServletException {

        log.warn("请求访问：{}，认证失败: {}", request.getRequestURL().toString(), authException.getLocalizedMessage());

        ServletUtils.renderString(response, JSON.toJSONString(FAIL_AUTH));
    }
}
