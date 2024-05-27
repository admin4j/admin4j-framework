package com.admin4j.framework.alert.listener;

import com.admin4j.common.pojo.event.GlobalExceptionEvent;
import com.admin4j.framework.alert.AlertMessageService;
import com.admin4j.framework.alert.props.AlertProperties;
import com.admin4j.limiter.core.constant.LimiterType;
import com.admin4j.limiter.core.util.RateLimiterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author andanyang
 * @since 2024/2/22 11:17
 */
public class GlobalExceptionListener {

    @Autowired
    AlertProperties alertProperties;
    @Autowired
    private AlertMessageService alertMessageService;

    /**
     * 监听全局异常
     *
     * @param event
     */
    @TransactionalEventListener(fallbackExecution = true, phase = TransactionPhase.AFTER_COMMIT)
    public void globalException(GlobalExceptionEvent event) {


        // 限流
        String key = event.getName() + ":" + event.getE().getClass().getName() + ":" + event.getE().getMessage();

        key = "GlobalException:" + DigestUtils.md5DigestAsHex(key.getBytes(StandardCharsets.UTF_8));

        if (alertProperties.getRateLimiterCapacity() == 0 || alertProperties.getRateLimiterInterval() == 0) {
            alertMessageService.sendMsg(event.getName(), event.getMessage(), event.getE());
        } else if (RateLimiterUtil.rateLimiter(LimiterType.TOKEN_BUCKET, key, 20, 1)) {
            alertMessageService.sendMsg(event.getName(), event.getMessage(), event.getE());
        }

    }


}
