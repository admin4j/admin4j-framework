package com.admin4j.framework.security.ignoringUrl;

import com.admin4j.framework.security.annotation.AnonymousAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.*;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * @author andanyang
 * @since 2023/3/27 13:29
 */
@Slf4j
public class AnonymousAccessUrl implements ApplicationContextAware {

    private ApplicationContext applicationContext;


    /**
     * 获取支持匿名访问的链接
     * 搜寻匿名标记 url： @AnonymousAccess
     *
     * @return
     */
    public Map<HttpMethod, String[]> getAnonymousUrl() {

        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap =
                applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods();

        Map<HttpMethod, String[]> anonymousUrls = new HashMap<>(32);
        Set<String> get = new HashSet<>(32);
        Set<String> post = new HashSet<>(32);
        Set<String> put = new HashSet<>(32);
        Set<String> patch = new HashSet<>(32);
        Set<String> delete = new HashSet<>(32);
        for (Map.Entry<RequestMappingInfo, HandlerMethod> infoEntry : handlerMethodMap.entrySet()) {
            log.debug("AnonymousAccess getAnonymousUrl: {} -> {}", infoEntry.getKey(), infoEntry.getValue());

            HandlerMethod handlerMethod = infoEntry.getValue();
            AnonymousAccess anonymousAccess = handlerMethod.getMethodAnnotation(AnonymousAccess.class);
            if (null != anonymousAccess) {
                Set<String> patterns;
                PathPatternsRequestCondition pathPatternsCondition = infoEntry.getKey().getPathPatternsCondition();

                if (pathPatternsCondition != null) {
                    patterns = pathPatternsCondition.getPatternValues();
                } else {
                    PatternsRequestCondition patternsCondition = infoEntry.getKey().getPatternsCondition();
                    if (null != patternsCondition) {
                        patterns = patternsCondition.getPatterns();
                    } else {
                        continue;
                    }
                }

                Iterable<RequestMethod> requestMethods;
                if (anonymousAccess.method().length == 0) {

                    requestMethods = infoEntry.getKey().getMethodsCondition().getMethods();
                } else {
                    requestMethods = Arrays.asList(anonymousAccess.method());
                }
                for (RequestMethod method : requestMethods) {

                    switch (method) {
                        case GET:
                            get.addAll(patterns);
                            break;
                        case POST:
                            post.addAll(patterns);
                            break;
                        case PUT:
                            put.addAll(patterns);
                            break;
                        case PATCH:
                            patch.addAll(patterns);
                            break;
                        case DELETE:
                            delete.addAll(patterns);
                            break;
                        default:
                            break;
                    }
                }

            }
        }
        anonymousUrls.put(HttpMethod.GET, get.toArray(new String[get.size()]));
        anonymousUrls.put(HttpMethod.POST, post.toArray(new String[post.size()]));
        anonymousUrls.put(HttpMethod.PUT, put.toArray(new String[put.size()]));
        anonymousUrls.put(HttpMethod.PATCH, patch.toArray(new String[patch.size()]));
        anonymousUrls.put(HttpMethod.DELETE, delete.toArray(new String[delete.size()]));

        log.debug("AnonymousAccess anonymousUrls: {}", anonymousUrls);
        return anonymousUrls;
    }

    /**
     * Set the ApplicationContext that this object runs in.
     * Normally this call will be used to initialize the object.
     * <p>Invoked after population of normal bean properties but before an init callback such
     * as {@link InitializingBean#afterPropertiesSet()}
     * or a custom init-method. Invoked after {@link ResourceLoaderAware#setResourceLoader},
     * {@link ApplicationEventPublisherAware#setApplicationEventPublisher} and
     * {@link MessageSourceAware}, if applicable.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws ApplicationContextException in case of context initialization errors
     * @throws BeansException              if thrown by application context methods
     * @see BeanInitializationException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
