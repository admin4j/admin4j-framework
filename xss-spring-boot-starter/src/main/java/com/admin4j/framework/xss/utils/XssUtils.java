package com.admin4j.framework.xss.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.owasp.validator.html.*;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * XSS 工具类， 用于过滤特殊字符
 * * @author andanyang
 * * @since 2021/10/22 16:38
 */
@Slf4j
public class XssUtils {

    private static Policy policy = null;
    private static AntiSamy antiSamy = new AntiSamy();

    private XssUtils() {
    }

    /**
     * 初始化策略
     *
     * @param policyName
     */
    public static void initPolicy(String policyName) {

        String policyFile = StringUtils.endsWith(policyName, ".xml") ? policyName : ("antisamy-" + policyName + ".xml");
        if (log.isDebugEnabled()) {
            log.debug(" start read XSS configfile [" + policyFile + "]");
        }

        try (InputStream inputStream = XssUtils.class.getClassLoader().getResourceAsStream(policyFile)) {
            assert inputStream != null;
            policy = Policy.getInstance(inputStream);
            if (log.isDebugEnabled()) {
                log.debug("read XSS configFile [" + policyName + "] success");
            }
        } catch (PolicyException | IOException e) {
            log.error("read XSS configFile [" + policyName + "] fail , reason:", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 跨站攻击语句过滤 方法
     *
     * @param paramValue           待过滤的参数
     * @param ignoreParamValueList 忽略过滤的参数列表
     * @return
     */
    public static String xssClean(String paramValue, List<String> ignoreParamValueList) {

        try {

            if (isIgnoreParamValue(paramValue, ignoreParamValueList)) {

                return paramValue;
            } else {
                final CleanResults cr = antiSamy.scan(paramValue, policy);
                cr.getErrorMessages().forEach(log::debug);
                String str = cr.getCleanHTML();
                return str;
            }
        } catch (ScanException e) {
            log.error("scan failed armter is [" + paramValue + "]", e);
        } catch (PolicyException e) {
            log.error("antisamy convert failed  armter is [" + paramValue + "]", e);
        }
        return paramValue;
    }

    private static boolean isIgnoreParamValue(String paramValue, List<String> ignoreParamValueList) {
        if (StringUtils.isEmpty(paramValue)) {
            return true;
        }
        if (CollectionUtils.isEmpty(ignoreParamValueList)) {
            return false;
        } else {
            for (String ignoreParamValue : ignoreParamValueList) {
                if (paramValue.contains(ignoreParamValue)) {
                    return true;
                }
            }
        }
        return false;
    }
}
