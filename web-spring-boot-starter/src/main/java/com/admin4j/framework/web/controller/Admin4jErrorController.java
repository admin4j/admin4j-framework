package com.admin4j.framework.web.controller;

import com.admin4j.common.pojo.ResponseEnum;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class Admin4jErrorController extends BasicErrorController {

    private ResponseEntity<Map<String, Object>> error404;


    public Admin4jErrorController(ErrorAttributes errorAttributes, ErrorProperties error, List collect) {
        super(errorAttributes, error, collect);
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {

        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if (statusCode == 404) {

            return error404(request);
        }

        return super.error(request);
    }

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
