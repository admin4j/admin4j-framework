package com.admin4j.framework.security.handler;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.pojo.IResponse;
import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import com.admin4j.common.util.ServletUtils;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.security.AuthenticationHandler;
import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.event.AuthenticationSuccessEvent;
import com.admin4j.framework.security.factory.AuthenticationUserFactory;
import com.admin4j.framework.security.jwt.JwtUserDetails;
import com.admin4j.spring.util.SpringUtils;
import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/5/31 18:00
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultAuthenticationHandler implements AuthenticationHandler {


    protected static final IResponse FAIL_AUTH_FORBIDDEN = new SimpleResponse(ResponseEnum.FAIL_AUTH_FORBIDDEN);
    protected static final IResponse FAIL_AUTH = new SimpleResponse(ResponseEnum.FAIL_AUTH_TOKEN);
    protected final UserTokenService userTokenService;

    /**
     * 认证成功回调
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        Object o = authentication.getPrincipal();
        if (o instanceof JwtUserDetails) {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) o;

            AuthenticationUser jwtUser = AuthenticationUserFactory.getByJwtUser(jwtUserDetails);

            UserContextUtil.setUser(jwtUser);
        }

        SpringUtils.getApplicationContext().publishEvent(new AuthenticationSuccessEvent(request, response, authentication));

        SimpleResponse simpleResponse = new SimpleResponse(ResponseEnum.SUCCESS);
        Map<String, Object> map = new HashMap<>();
        simpleResponse.setData(map);

        if (userTokenService != null) {
            String token = userTokenService.createToken((JwtUserDetails) authentication.getPrincipal());
            map.put("token", token);
        }
        ServletUtils.renderString(response, JSON.toJSONString(simpleResponse), 200);
    }

    /**
     * 认证失败回调
     * <p>
     * ExceptionHandlerExceptionResolver.resolveException 可以模拟这个方法
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        SimpleResponse simpleResponse = new SimpleResponse(ResponseEnum.FAIL_AUTH);
        simpleResponse.setMsg(exception.getMessage());
        ServletUtils.renderString(response, JSON.toJSONString(simpleResponse), 401);
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        SimpleResponse simpleResponse = new SimpleResponse(ResponseEnum.SUCCESS);
        simpleResponse.setMsg(ResponseEnum.SUCCESS.getMsg());
        ServletUtils.renderString(response, JSON.toJSONString(simpleResponse), 200);
    }


    /**
     * 没有权限访问时
     */
    @Override
    public void accessDeniedHandler(HttpServletRequest httpServletRequest, HttpServletResponse response, AccessDeniedException e) {
        ServletUtils.renderString(response, JSON.toJSONString(FAIL_AUTH_FORBIDDEN), 403);
    }

    /**
     * 未登录或登录过期 token 认证失败
     *
     * @param request
     * @param response
     * @param authException
     */
    @Override
    public void authenticationEntryPoint(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {


        log.warn("请求访问：{}，认证失败: {}", request.getRequestURL().toString(), authException.getLocalizedMessage());
        ServletUtils.renderString(response, JSON.toJSONString(FAIL_AUTH), 401);
    }
}
