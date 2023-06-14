package com.admin4j.spring.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 解析SPEL 表达式
 *
 * @author andanyang
 * @since 2018/1/20 13:50
 */
public class SpelUtil {

    private SpelUtil() {
    }

    private static final LocalVariableTableParameterNameDiscoverer U = new LocalVariableTableParameterNameDiscoverer();
    //使用SPEL进行key的解析
    private static final ExpressionParser EL_PARSER = new SpelExpressionParser();


    public static String parse(String spel, Method method, Object[] args) {
        //获取被拦截方法参数名列表(使用Spring支持类库)
        String[] paraNameArr = U.getParameterNames(method);

        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        if (paraNameArr != null) {
            for (int i = 0; i < paraNameArr.length; i++) {
                context.setVariable(paraNameArr[i], args[i]);
            }
        }

        try {
            return EL_PARSER.parseExpression(spel).getValue(context, String.class);
        } catch (ParseException | SpelEvaluationException e) {
            return spel;
        }
    }

    /**
     * 支持 #p0 参数索引的表达式解析
     *
     * @param rootObject 根对象,method 所在的对象
     * @param spel       表达式
     * @param method     ，目标方法
     * @param args       方法入参
     * @return 解析后的字符串
     */
    public static String parse(Object rootObject, String spel, Method method, Object[] args) {
        if (StringUtils.isEmpty(spel)) {
            return "";
        } else if (!spel.contains("#")) {
            return parse(spel, method, args);
        }
        //获取被拦截方法参数名列表(使用Spring支持类库)
        String[] paraNameArr = U.getParameterNames(method);

        //SPEL上下文
        StandardEvaluationContext context = new MethodBasedEvaluationContext(rootObject, method, args, U);
        //把方法参数放入SPEL上下文中
        if (paraNameArr != null) {
            for (int i = 0; i < paraNameArr.length; i++) {
                context.setVariable(paraNameArr[i], args[i]);
            }
        }

        try {
            return EL_PARSER.parseExpression(spel).getValue(context, String.class);
        } catch (ParseException | SpelEvaluationException e) {

            return spel;
        }
    }


    /**
     * 计算SpEL表达式的值
     *
     * @param expression 表达式字符串
     * @param context    变量上下文
     * @param clazz      返回值类型
     * @return 表达式计算结果
     */
    public static <T> T getValue(String expression, Map<String, Object> context, Class<T> clazz) {
        StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
        if (context != null && !context.isEmpty()) {
            for (Map.Entry<String, Object> entry : context.entrySet()) {
                evaluationContext.setVariable(entry.getKey(), entry.getValue());
            }
        }
        try {
            return EL_PARSER.parseExpression(expression).getValue(evaluationContext, clazz);
        } catch (ParseException e) {
            return null;
        }
    }
}
