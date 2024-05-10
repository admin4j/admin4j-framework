package com.admin4j.common.exception;

import javax.validation.ConstraintViolation;
import java.util.Collection;

/**
 * 验证异常
 *
 * @author andanyang
 * @since 2023/6/15 10:33
 */
public class ValidateException extends BizException {


    private static final long serialVersionUID = 2676549821878113088L;
    Collection<ConstraintViolation<Object>> violations;


    public ValidateException(Collection<ConstraintViolation<Object>> violations) {
        super(violations.stream().map(i -> i.getPropertyPath() + i.getMessage()).reduce((m1, m2) -> m1 + "；" + m2).orElse(""));
        this.violations = violations;
    }
}
