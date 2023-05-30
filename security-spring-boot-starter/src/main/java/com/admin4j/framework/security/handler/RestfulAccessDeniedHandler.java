package com.admin4j.framework.security.handler;

import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import com.admin4j.common.util.ServletUtils;
import com.alibaba.fastjson2.JSON;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author andanyang
 * @since 2023/3/24 16:34
 * 自定义返回结果：没有权限访问时
 */
@ConditionalOnMissingBean(AccessDeniedHandler.class)
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {

    private static final IResponse FAIL_AUTH_FORBIDDEN = new SimpleResponse(ResponseEnum.FAIL_AUTH_FORBIDDEN);

    @Override
    public void handle(
            HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e)
            throws IOException, ServletException {

        ServletUtils.renderString(response, JSON.toJSONString(FAIL_AUTH_FORBIDDEN));
    }
}
