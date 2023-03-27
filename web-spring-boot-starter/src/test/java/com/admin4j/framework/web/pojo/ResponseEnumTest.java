package com.admin4j.framework.web.pojo;

import com.admin4j.common.pojo.ResponseEnum;
import org.junit.jupiter.api.Test;

/**
 * @author andanyang
 * @since 2023/3/21 14:26
 */


class ResponseEnumTest {

    @Test
    void isNull() {

        ResponseEnum.ASSERT_ERROR.notNull(null, "是空的");
        ResponseEnum.ASSERT_ERROR.isNull(1, "是空的,,{0}", 12);
    }

    @Test
    void isTrue() {

        ResponseEnum.ASSERT_ERROR.isTrue(1 == 1, "true");
        ResponseEnum.ERROR_E.isTrue(1 == 2, "fa bi {0}", "a ");
    }

    @Test
    void notEmpty() {

        ResponseEnum.ASSERT_ERROR.notEmpty("", "notEmpty {0}", "notEmpty");
        ResponseEnum.ERROR_E.isTrue(1 == 2, "fa bi {0}", "a ");
    }
}