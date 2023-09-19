package com.admin4j.common.crypto;

import junit.framework.TestCase;

/**
 * @author andanyang
 * @since 2023/9/19 10:22
 */
public class AesUtilTest extends TestCase {

    public void testEncrypt() {
        String data = "AES加密解密";
        String key = "KEYBYACSJAVAZXLr";
        String encrypt = AesUtil.encrypt(data, key);
        System.out.println("encrypt = " + encrypt);


        String decrypt = AesUtil.decrypt(encrypt, key);
        System.out.println("decrypt = " + decrypt);
    }

    public void testDecrypt() {


    }
}