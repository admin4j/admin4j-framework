package com.admin4j.common.crypto;

import junit.framework.TestCase;

/**
 * @author andanyang
 * @since 2023/9/19 10:22
 */
public class DesUtilTest extends TestCase {

    public void testEncrypt() {

        String data = "DES加密解密";
        String key = "ee7733r5";
        String encrypt = DesUtil.encrypt(data, key);
        System.out.println("encrypt = " + encrypt);
        String decrypt = DesUtil.decrypt(encrypt, key);
        System.out.println("decrypt = " + decrypt);
    }

    public void testDecrypt() {
    }
}