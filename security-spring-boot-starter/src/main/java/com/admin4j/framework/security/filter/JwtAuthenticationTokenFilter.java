package com.admin4j.framework.security.filter;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.security.AuthenticationResult;
import com.admin4j.framework.security.UserTokenService;
import com.admin4j.framework.security.jwt.JwtUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * token过滤器 验证token有效性
 *
 * @author andanyang
 * @since 2023/3/27 15:32
 */
@Slf4j
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    UserTokenService userTokenService;
    //@Autowired
    //UserDetailsService userDetailsService;
    @Autowired
    AuthenticationResult authenticationResult;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = userTokenService.getToken(request);
        if (StringUtils.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            UserDetails userDetails = userTokenService.getUserDetails(token);

            //userDetailsService.loadUserByUsername(userName);

            if (userDetails != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                //设置登录
                JwtUserDetails jwtUserDetails = (JwtUserDetails) userDetails;
                AuthenticationUser authenticationUser = new AuthenticationUser();
                authenticationUser.setUserId(jwtUserDetails.getUserId());
                authenticationUser.setTenantId(jwtUserDetails.getTenant());
                authenticationUser.setUsername(jwtUserDetails.getUsername());
                authenticationUser.setAdmin(jwtUserDetails.isAdmin());

                UserContextUtil.setUser(authenticationUser);
            }
        } catch (Exception e) {
            log.error("onAuthenticationFailure {}", e.getMessage(), e);
            authenticationResult.onAuthenticationFailure(request, response, e);
            return;
        }

        filterChain.doFilter(request, response);

        //清除登录信息
        UserContextUtil.clear();
    }
}
