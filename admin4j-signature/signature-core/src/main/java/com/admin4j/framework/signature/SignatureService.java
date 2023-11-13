package com.admin4j.framework.signature;

import com.admin4j.framework.signature.annotation.Signature;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author zhougang
 * @since 2023/11/10 9:42
 */
public interface SignatureService {

    /**
     * 验证签名是否通过
     *
     * @return 是否限速
     */
    boolean verify(Signature signature, HttpServletRequest request) throws IOException;
}
