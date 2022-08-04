//package com.admin4j.common.exception.assertion;
//
//import com.ces.common.core.api.IResponse;
//import com.ces.common.core.exception.BaseException;
//import com.ces.common.core.exception.BusinessException;
//import org.apache.commons.lang3.ArrayUtils;
//import org.apache.commons.lang3.StringUtils;
//
//import java.text.MessageFormat;
//
///**
// * <p>业务异常断言</p>
// */
//public interface BusinessExceptionAssert extends IResponse, Assert {
//
//    @Override
//    default BaseException newException(Object... args) {
//        String msg = this.getMsg();
//        if (ArrayUtils.isNotEmpty(args)) {
//            msg = MessageFormat.format(this.getMsg(), args);
//        }
//
//        return new BusinessException(this, args, msg);
//    }
//
//    @Override
//    default BaseException newException(Throwable t, Object... args) {
//        String msg = this.getMsg();
//        if (StringUtils.isNotEmpty(t.getMessage())) {
//            msg = t.getMessage();
//        }
//        if (ArrayUtils.isNotEmpty(args)) {
//            msg = MessageFormat.format(this.getMsg(), args);
//        }
//
//        return new BusinessException(this, args, msg, t);
//    }
//
//}
