package com.admin4j.framework.tenant.openfeign;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.tenant.TenantConstant;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * @author andanyang
 * @since 2023/9/20 9:29
 */
@RequiredArgsConstructor
@WebFilter(filterName = "UserContextHolderFilter",
        urlPatterns = "/*"
)
public class UserContextHolderFilter extends GenericFilterBean {

    private final IUserContextHolder userContextHolder;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        // TODO 限制IP
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            String header = request.getHeader(TenantConstant.USER_INFO);
            if (StringUtils.isNotBlank(header)) {

                String userInfo = URLDecoder.decode(header, "UTF-8");
                AuthenticationUser authenticationUser = userContextHolder.decode(userInfo);
                if (authenticationUser != null) {
                    UserContextUtil.setUser(authenticationUser);
                }
            }

            filterChain.doFilter(request, response);
        } finally {
            UserContextUtil.clear();
        }
    }
}
