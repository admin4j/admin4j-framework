package com.admin4j.framework.tenant.openfeign;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.tenant.TenantConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * @author andanyang
 * @since 2023/9/20 9:29
 */
@RequiredArgsConstructor
public class UserContextHolderFilter extends GenericFilterBean {

    private final IUserContextHolder userContextHolder;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            String userInfo = URLDecoder.decode(request.getHeader(TenantConstant.USER_INFO), "UTF-8");
            AuthenticationUser authenticationUser = userContextHolder.decode(userInfo);
            if (authenticationUser != null) {
                UserContextUtil.setUser(authenticationUser);
            }
            filterChain.doFilter(request, response);
        } finally {
            UserContextUtil.clear();
        }
    }
}
