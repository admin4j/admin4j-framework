package com.admin4j.framework.web.controller;

import com.admin4j.common.pojo.ResponseEnum;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/5/29 17:44
 */
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@ConditionalOnMissingBean(ErrorController.class)
public class Admin4jErrorController extends BasicErrorController {
    private final ServerProperties serverProperties;

    public Admin4jErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, serverProperties.getError(), errorViewResolvers);
        this.serverProperties = serverProperties;
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == 404) {

            return error404(request);
        }

        return super.error(request);
    }

    private static ResponseEntity<Map<String, Object>> error404;

    public ResponseEntity<Map<String, Object>> error404(HttpServletRequest request) {

        if (error404 == null) {
            Map<String, Object> map = new HashMap<>();

            map.put("msg", ResponseEnum.NOT_FOUND.getMsg());
            map.put("code", ResponseEnum.NOT_FOUND.getCode());
            error404 = ResponseEntity.status(404).body(map);
        }
        return error404;
    }

    @Override
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        return super.errorHtml(request, response);
    }
}
