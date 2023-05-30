package com.admin4j.framework.security.handler;

import com.admin4j.common.pojo.ResponseEnum;
import com.admin4j.common.pojo.SimpleResponse;
import com.admin4j.common.util.ServletUtils;
import com.admin4j.framework.security.UserTokenService;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author andanyang
 * @since 2023/5/30 15:31
 */
@ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
public class RestfulAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserTokenService userTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {


        String token = userTokenService.createToken((UserDetails) authentication.getDetails());

        SimpleResponse simpleResponse = new SimpleResponse(ResponseEnum.SUCCESS);
        simpleResponse.setData(token);
        ServletUtils.renderString(response, JSON.toJSONString(simpleResponse));
    }
}
