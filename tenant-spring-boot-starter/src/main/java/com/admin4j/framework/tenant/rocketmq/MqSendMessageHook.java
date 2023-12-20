package com.admin4j.framework.tenant.rocketmq;

import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.service.IUserContextHolder;
import com.admin4j.framework.tenant.TenantConstant;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.client.hook.SendMessageContext;
import org.apache.rocketmq.client.hook.SendMessageHook;

import java.util.HashMap;
import java.util.Map;

/**
 * @author andanyang
 * @since 2023/12/1 11:04
 */
@RequiredArgsConstructor
public class MqSendMessageHook implements SendMessageHook {

    private final IUserContextHolder userContextHolder;

    @Override
    public String hookName() {
        return "tenant";
    }

    @Override
    public void sendMessageBefore(SendMessageContext context) {
        
        AuthenticationUser user = userContextHolder.getAuthenticationUser();
        if (user != null) {

            Map<String, String> props = context.getProps();
            if (props == null) {
                props = new HashMap<>();
            }
            props.put(TenantConstant.USER_INFO, JSONObject.toJSONString(user));
            context.setProps(props);
        }
    }

    @Override
    public void sendMessageAfter(SendMessageContext context) {

    }
}
