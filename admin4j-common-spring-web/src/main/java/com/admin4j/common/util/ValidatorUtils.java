package com.admin4j.common.util;

import com.admin4j.common.exception.ValidateException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.validator.HibernateValidator;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * 验证器，工具类
 *
 * @author andanyang
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtils {

    private static final Validator VALIDATOR_FAST = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    private static final Validator VALIDATOR_ALL = Validation.byProvider(HibernateValidator.class).configure().failFast(false).buildValidatorFactory().getValidator();

    /**
     * @param <T>    class
     * @param domain 需要校验的实体
     * @return 错误内容
     * @Time 2020年6月22日 上午11:36:13
     */
    public static <T> Set<ConstraintViolation<T>> validateFast(T domain, Class<?>... groups) {
        return VALIDATOR_FAST.validate(domain, groups);
    }

    /**
     * 校验遇到第一个不合法的字段直接返回不合法字段，后续字段不再校验
     *
     * @param <T>           class
     * @param domain        需要校验的实体
     * @param showException 是否显示异常
     * @Time 2020年6月22日 上午11:36:13
     */
    public static <T> void validateFast(T domain, boolean showException, Class<?>... groups) {
        Set<ConstraintViolation<T>> validate = validateFast(domain, groups);
        if (showException && !CollectionUtils.isEmpty(validate)) {
            throw new ValidateException(validate.stream().map(i -> i.getPropertyPath() + i.getMessage()).reduce((m1, m2) -> m1 + "；" + m2).orElse(""));
        }
    }

    /**
     * 校验所有字段并返回不合法字段
     *
     * @param <T>    需要校验的实体类型
     * @param domain 需要校验的实体
     * @return
     * @throws Exception
     * @Time 2020年6月22日 上午11:36:55
     */
    public static <T> Set<ConstraintViolation<T>> validateAll(T domain, Class<?>... groups) {
        return VALIDATOR_ALL.validate(domain, groups);
    }

    /**
     * 校验所有字段并返回不合法字段
     *
     * @param <T>           需要校验的实体类型
     * @param domain        需要校验的实体
     * @param showException 是否抛出异常
     * @return
     * @throws Exception
     * @Time 2020年6月22日 上午11:36:55
     */
    public static <T> void validateAll(T domain, boolean showException, Class<?>... groups) {
        Set<ConstraintViolation<T>> validate = validateAll(domain, groups);
        if (showException && !CollectionUtils.isEmpty(validate)) {
            throw new ValidateException(validate.stream().map(i -> i.getPropertyPath() + i.getMessage()).reduce((m1, m2) -> m1 + "；" + m2).orElse(""));
        }
    }
}
