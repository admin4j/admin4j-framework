package com.admin4j.common.exception.assertion;

import com.admin4j.common.exception.BaseException;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author andanyang
 * @since 2023/3/21 13:52
 */
public interface Assert {

    /**
     * 创建异常
     *
     * @param t      异常
     * @param errMsg msg
     * @return
     */
    BaseException newException(Throwable t, String errMsg);

    BaseException newException(Throwable t);

    BaseException newException(String errMsg);

    /**
     * 创建异常
     *
     * @param errMsg 异常msg
     * @return
     */
    default BaseException newException(String errMsg, Object... args) {
        if (args != null && args.length > 0) {
            errMsg = MessageFormat.format(errMsg, args);
        }
        return newException(errMsg);
    }


    /**
     * 创建异常. 先使用 {@code errMsg}
     * 创建一个 {@link com.admin4j.common.exception.WrapMessageException} 异常, 再以入参的形式传给
     * {@link #newException(Throwable, String)}, 作为最后创建的异常的 cause 属性.
     *
     * @param errMsg 自定义的错误信息
     * @param args
     * @return
     */
    default BaseException newException(Throwable t, String errMsg, Object... args) {
        if (args != null && args.length > 0) {
            errMsg = MessageFormat.format(errMsg, args);
        }

        throw newException(t, errMsg);
    }


    /**
     * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * @param obj    待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void notNull(Object obj, String errMsg) {
        if (obj == null) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj    待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args   message占位符对应的参数列表
     */
    default void notNull(Object obj, String errMsg, Object... args) {
        if (obj == null) {
            throw newException(errMsg, args);
        }
    }


    /**
     * 断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj    待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args   message占位符对应的参数列表
     */
    default void notNull(Object obj, Supplier<String> errMsg, Object... args) {
        if (obj == null) {
            throw newException(errMsg.get(), args);
        }
    }


    /**
     * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     *
     * @param str    待判断字符串
     * @param errMsg 自定义的错误信息
     */
    default void notEmpty(String str, String errMsg) {
        if (null == str || "".equals(str.trim())) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言字符串<code>str</code>不为空串（长度为0）。如果字符串<code>str</code>为空串，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param str    待判断字符串
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args   message占位符对应的参数列表
     */
    default void notEmpty(String str, String errMsg, Object... args) {
        if (str == null || "".equals(str.trim())) {
            throw newException(errMsg, args);
        }
    }


    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void notEmpty(Object[] arrays, String errMsg) {
        if (arrays == null || arrays.length == 0) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言数组<code>arrays</code>大小不为0。如果数组<code>arrays</code>大小不为0，则抛出异常
     *
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param arrays 待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args   message占位符对应的参数列表
     */
    default void notEmpty(Object[] arrays, String errMsg, Object... args) {
        if (arrays == null || arrays.length == 0) {
            throw newException(errMsg, args);
        }
    }


    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c      待判断数组
     * @param errMsg 自定义的错误信息
     */
    default void notEmpty(Collection<?> c, String errMsg) {
        if (c == null || c.isEmpty()) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言集合<code>c</code>大小不为0。如果集合<code>c</code>大小不为0，则抛出异常
     *
     * @param c      待判断数组
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args   message占位符对应的参数列表
     */
    default void notEmpty(Collection<?> c, String errMsg, Object... args) {
        if (c == null || c.isEmpty()) {
            throw newException(errMsg, args);
        }
    }


    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map    待判断Map
     * @param errMsg 自定义的错误信息
     */
    default void notEmpty(Map<?, ?> map, String errMsg) {
        if (map == null || map.isEmpty()) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言Map<code>map</code>大小不为0。如果Map<code>map</code>大小不为0，则抛出异常
     *
     * @param map    待判断Map
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args   message占位符对应的参数列表
     */
    default void notEmpty(Map<?, ?> map, String errMsg, Object... args) {
        if (map == null || map.isEmpty()) {
            throw newException(errMsg, args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg     自定义的错误信息
     */
    default void isFalse(boolean expression, String errMsg) {
        if (expression) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为false。如果布尔值<code>expression</code>为true，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg     自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args       message占位符对应的参数列表
     */
    default void isFalse(boolean expression, String errMsg, Object... args) {
        if (expression) {
            throw newException(errMsg, args);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg     自定义的错误信息
     */
    default void isTrue(boolean expression, String errMsg) {
        if (!expression) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言布尔值<code>expression</code>为true。如果布尔值<code>expression</code>为false，则抛出异常
     *
     * @param expression 待判断布尔变量
     * @param errMsg     自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args       message占位符对应的参数列表
     */
    default void isTrue(boolean expression, String errMsg, Object... args) {
        if (!expression) {
            throw newException(errMsg, args);
        }
    }


    /**
     * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj    待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void isNull(Object obj, String errMsg) {
        if (obj != null) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言对象<code>obj</code>为<code>null</code>。如果对象<code>obj</code>不为<code>null</code>，则抛出异常
     *
     * @param obj    待判断布尔变量
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args   message占位符对应的参数列表
     */
    default void isNull(Object obj, String errMsg, Object... args) {
        if (obj != null) {
            throw newException(errMsg, args);
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
     *
     * @param o1     待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2     待判断对象
     * @param errMsg 自定义的错误信息
     */
    default void equals(Object o1, Object o2, String errMsg) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newException(errMsg);
        }
    }

    /**
     * 断言对象<code>o1</code>与对象<code>o2</code>相等，此处的相等指（o1.equals(o2)为true）。 如果两对象不相等，则抛出异常
     *
     * @param o1     待判断对象，若<code>o1</code>为null，也当作不相等处理
     * @param o2     待判断对象
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param args   message占位符对应的参数列表
     */
    default void equals(Object o1, Object o2, String errMsg, Object... args) {
        if (o1 == o2) {
            return;
        }
        if (o1 == null || !o1.equals(o2)) {
            throw newException(errMsg, args);
        }
    }


    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param t 原始异常
     */
    default void fail(Throwable t) {
        throw newException(t);
    }


    /**
     * 直接抛出异常
     *
     * @param errMsg 自定义的错误信息
     */
    default void fail(String errMsg) {
        throw newException(errMsg);
    }


    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息
     * @param t      原始异常
     */
    default void fail(String errMsg, Throwable t) {
        throw newException(t, errMsg);
    }

    /**
     * 直接抛出异常，并包含原异常信息
     *
     * <p>当捕获非运行时异常（非继承{@link RuntimeException}）时，并该异常进行业务描述时， 必须传递原始异常，作为新异常的cause
     *
     * @param errMsg 自定义的错误信息. 支持 {index} 形式的占位符, 比如: errMsg-用户[{0}]不存在, args-1001, 最后打印-用户[1001]不存在
     * @param t      原始异常
     * @param args   message占位符对应的参数列表
     */
    default void fail(String errMsg, Throwable t, Object... args) {
        throw newException(t, errMsg, args);
    }
}
