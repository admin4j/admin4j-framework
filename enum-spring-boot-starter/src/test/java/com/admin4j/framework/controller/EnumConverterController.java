package com.admin4j.framework.controller;

import com.admin4j.framework.constant.UserStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andanyang
 * @since 2023/11/1 13:12
 */
@RestController
@RequestMapping
public class EnumConverterController {
    @RequestMapping("UserStatus")
    public UserStatus userStatus(@RequestParam UserStatus userStatus) {

        return userStatus;
    }

}
