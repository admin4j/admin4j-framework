package com.admin4j.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.validator.HibernateValidator;

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

    private static Validator validatorFast = Validation.byProvider(HibernateValidator.class).configure().failFast(true).buildValidatorFactory().getValidator();
    private static Validator validatorAll = Validation.byProvider(HibernateValidator.class).configure().failFast(false).buildValidatorFactory().getValidator();

    /**
     * 校验遇到第一个不合法的字段直接返回不合法字段，后续字段不再校验
     *
     * @param <T>
     * @param domain
     * @return
     * @Time 2020年6月22日 上午11:36:13
     */
    public static <T> Set<ConstraintViolation<T>> validateFast(T domain) {
        return validatorFast.validate(domain);
    }

    /**
     * 校验所有字段并返回不合法字段
     *
     * @param <T>
     * @param domain
     * @return
     * @throws Exception
     * @Time 2020年6月22日 上午11:36:55
     */
    public static <T> Set<ConstraintViolation<T>> validateAll(T domain) {
        return validatorAll.validate(domain);
    }
}
