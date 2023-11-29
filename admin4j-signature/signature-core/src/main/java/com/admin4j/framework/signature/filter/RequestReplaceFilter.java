package com.admin4j.framework.signature.filter;

import com.admin4j.framework.signature.BodyReaderHttpServletRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 包装HttpServletRequest对象
 * @author zhougang
 * @since 2023/11/12 10:23
 */
public class RequestReplaceFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!(request instanceof ServletRequestWrapper)) {
            // 包装HttpServletRequest对象，缓存body数据，再次读取的时候将缓存的值写出,解决HttpServetRequest读取body只能一次的问题
            request = new BodyReaderHttpServletRequestWrapper(request);
        }
        filterChain.doFilter(request, response);
    }
}
