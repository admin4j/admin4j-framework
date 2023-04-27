package com.admin4j.framework.xss.filter;


import com.admin4j.framework.xss.property.XssProperties;
import com.admin4j.framework.xss.utils.XssUtils;
import com.admin4j.framework.xss.wrapper.XssRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.PathContainer;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 跨站工具 过滤器
 *
 * @author andanyang
 * @since 2022/3/17 11:24
 */
@Slf4j
public class XssFilter implements Filter {

    static final String[] DEFAULT_IGNORES = new String[]{
            "/favicon.ico",
            "/doc.html", "/swagger-ui.html", "/webjars*", "/v2/*", "/swagger-resources/*", "/resources/*", "/static/*", "/public/*", "/actuator/*"
    };

    List<PathPattern> defaultIgnorePatterns = new ArrayList<>(DEFAULT_IGNORES.length + 8);
    List<PathPattern> ignorePatterns = new ArrayList<>();
    List<PathPattern> includePatterns = new ArrayList<>();

    private XssProperties xssProperties;

    public XssFilter(XssProperties xssProperties) {
        this.xssProperties = xssProperties;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        refresh();
    }

    /**
     * 刷新配置
     */
    public void refresh() {
        XssUtils.initPolicy(xssProperties.getAntisamyPolicy());

        //缓存 PathPattern
        defaultIgnorePatterns.clear();
        for (String uri : DEFAULT_IGNORES) {
            if (StringUtils.isNotEmpty(uri)) {
                defaultIgnorePatterns.add(PathPatternParser.defaultInstance.parse(uri));
            }
        }

        ignorePatterns.clear();
        for (String uri : xssProperties.getIgnoreUris()) {
            if (StringUtils.isNotEmpty(uri)) {
                ignorePatterns.add(PathPatternParser.defaultInstance.parse(uri));
            }
        }

        includePatterns.clear();
        for (String uri : xssProperties.getIncludeUris()) {
            if (StringUtils.isNotEmpty(uri)) {
                includePatterns.add(PathPatternParser.defaultInstance.parse(uri));
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        log.debug("XSS fiter [XSSFilter] starting");
        // 判断uri是否包含项目名称
        String uriPath = ((HttpServletRequest) request).getRequestURI();
        if (isIgnorePath(uriPath)) {

            chain.doFilter(request, response);
            return;
        } else {

            //传入重写后的Request
            chain.doFilter(new XssRequestWrapper((HttpServletRequest) request, xssProperties.getIgnoreParam()), response);
        }
        log.debug("XSS fiter [XSSFilter] stop");
    }

    @Override
    public void destroy() {
        log.debug("XSS fiter [XSSFilter] destroy");
    }


    /**
     * 是否忽略该路径
     *
     * @param servletPath
     * @return
     */
    private boolean isIgnorePath(String servletPath) {
        if (xssProperties.getMatchPattern() == 0) {
            return true;
        }

        if (StringUtils.isEmpty(servletPath)) {
            return true;
        }

        //默认需要忽略的
        PathContainer pathContainer = PathContainer.parsePath(servletPath);
        for (PathPattern ignorePattern : defaultIgnorePatterns) {
            if (ignorePattern.matches(pathContainer)) {
                return true;
            }
        }


        if (xssProperties.getMatchPattern() == 1) {
            if (CollectionUtils.isEmpty(xssProperties.getIgnoreUris())) {
                return false;
            } else {

                for (PathPattern ignorePattern : ignorePatterns) {
                    if (ignorePattern.matches(pathContainer)) {
                        return true;
                    }
                }
            }

            return false;
        } else if (xssProperties.getMatchPattern() == 2) {

            if (CollectionUtils.isEmpty(xssProperties.getIncludeUris())) {
                return true;
            } else {

                for (PathPattern includePattern : includePatterns) {
                    if (includePattern.matches(pathContainer)) {
                        return false;
                    }
                }
            }

            return true;
        }
        return false;
    }
}
