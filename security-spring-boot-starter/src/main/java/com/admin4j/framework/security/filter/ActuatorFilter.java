package com.admin4j.framework.security.filter;

import com.admin4j.framework.security.properties.ActuatorProperties;
import com.admin4j.spring.util.IpUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author andanyang
 * @since 2023/6/7 10:23
 */
public class ActuatorFilter extends OncePerRequestFilter {

    @Value("${management.endpoints.web.basePath:/actuator}")
    private String actuatorBasePath;
    private AntPathRequestMatcher antPathRequestMatcher;

    @Autowired
    ActuatorProperties actuatorProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        if (getAntPathRequestMatcher().matches(request) && canAccess(request)) {

            ActuatorAuthenticationToken actuatorAuthenticationToken = new ActuatorAuthenticationToken(request);
            SecurityContextHolder.getContext().setAuthentication(actuatorAuthenticationToken);
        }
        filterChain.doFilter(request, response);
    }

    public AntPathRequestMatcher getAntPathRequestMatcher() {
        if (antPathRequestMatcher == null) {
            antPathRequestMatcher = new AntPathRequestMatcher(actuatorBasePath + "/**");
        }
        return antPathRequestMatcher;
    }

    /**
     * @return 是否可以访问
     */
    private boolean canAccess(HttpServletRequest request) {

        if (actuatorProperties == null || actuatorProperties.getIps() == null || actuatorProperties.getIps().length == 0) {
            return false;
        }
        String ipAddr = IpUtils.getIpAddr(request);
        if (StringUtils.isBlank(ipAddr)) {
            return true;
        }

        for (String ipPattern : actuatorProperties.getIps()) {

            if (Pattern.matches(ipPattern, ipAddr)) {
                return true;
            }
        }
        return false;
    }

    static class ActuatorAuthenticationToken extends AbstractAuthenticationToken {

        private static List<GrantedAuthority> roleAnonymous = AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS");
        private HttpServletRequest request;

        public ActuatorAuthenticationToken(HttpServletRequest request) {
            super(roleAnonymous);
            setAuthenticated(true);
            this.request = request;
        }

        @Override
        public Object getCredentials() {
            return IpUtils.getIpAddr(request);
        }


        @Override
        public Object getPrincipal() {
            return "anonymousUser";
        }
    }
}
