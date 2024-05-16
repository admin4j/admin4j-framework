package com.admin4j.framework.alert.send;

import com.admin4j.common.exception.BizException;
import com.admin4j.common.pojo.AuthenticationUser;
import com.admin4j.common.util.ServletUtils;
import com.admin4j.common.util.UserContextUtil;
import com.admin4j.framework.alert.AlertMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 发送报警消息
 *
 * @author andanyang
 * @since 2024/5/8 14:13
 */
public abstract class AbstractSendAlertMessageService implements AlertMessageService {

    /**
     * 需要排除的异常
     */
    protected static final Set<Class<?>> EXCLUDE_EXS = new HashSet<Class<?>>() {
        private static final long serialVersionUID = -3265602846432901507L;

        {
            add(IllegalArgumentException.class);
            add(MaxUploadSizeExceededException.class);
            add(BindException.class);
            add(BizException.class);
        }
    };


    @Value("${spring.application.name:''}")
    protected String applicationName;
    @Value("${spring.profiles.active:''}")
    protected String applicationEnv;

    @Autowired
    protected Executor executor;

    /**
     * 发送报警消息
     *
     * @param title   报警名称
     * @param e       异常
     * @param message 显示消息详情
     */
    @Override
    public void sendMsg(String title, String message, Throwable e) {


        if (e == null) {
            String alertMsg = title + "\n" +
                    "- appname: " + applicationName + "\n" +
                    "- env: " + applicationEnv + "\n" +
                    (message == null ? "" : "> message: " + message + "\n");
            doSendMsg(title, alertMsg);
            return;
        } else if (EXCLUDE_EXS.contains(e.getClass())) {
            return;
        }

        AuthenticationUser user = UserContextUtil.getUser();

        String url = "";

        HttpServletRequest request = ServletUtils.getRequest();
        if (request != null) {
            url = request.getMethod() + " " + request.getRequestURI();
        }

        //错误堆栈
        StringBuilder cause = new StringBuilder();
        StringBuilder stackTraceSb = new StringBuilder();
        Throwable et = e;
        while (et != null) {

            cause.append(et.getClass().getSimpleName()).append(" message: ").append(et.getMessage()).append("\n");

            StackTraceElement[] stackTrace = et.getStackTrace();
            stackTraceSb.append("- ")
                    .append(Arrays.stream(stackTrace).limit(6).map(String::valueOf).collect(Collectors.joining("\n")));
            et = et.getCause();
        }

        String alertMsg = title + "\n" +

                "- appname: " + applicationName + "\n" +
                "- env: " + applicationEnv + "\n" +
                (message == null ? "" : "- message: " + message + "\n") +
                "- errorMsg: " + e.getMessage() + "\n" +
                "- Exception: " + e.getClass().getName() + "\n" +
                "- Cause: " + cause +
                "- url: " + url + "\n" +
                "- UserId: " + (user == null ? "" : user.getUserId()) + "\n" +
                "> " + stackTraceSb;

        doSendMsg(title, alertMsg);
    }

    abstract void doSendMsg(String title, String alertMsg);
}
