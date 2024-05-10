package com.admin4j.framework.web.jackson.imp;

import com.admin4j.common.service.IUserConfigInfoService;
import com.admin4j.framework.web.jackson.TimeZoneFormatService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author andanyang
 * @since 2024/3/27 19:13
 */
public class TimeZoneFormatServiceImp implements TimeZoneFormatService {


    private final IUserConfigInfoService userConfigInfoService;

    public TimeZoneFormatServiceImp(@Autowired(required = false) IUserConfigInfoService userConfigInfoService) {
        this.userConfigInfoService = userConfigInfoService;
    }

    @Override
    public LocalDateTime serialize(Long timestamp) {
        if (timestamp == null) {
            return null;
        } else {

            Instant instant = Instant.ofEpochMilli(timestamp);
            String timeZone = userConfigInfoService == null ? null : userConfigInfoService.getConfig(USER_TIME_ZONE_KEY);
            return LocalDateTime.ofInstant(instant, StringUtils.isBlank(timeZone) ? ZoneId.systemDefault() : ZoneId.of(timeZone));
        }
    }
}
