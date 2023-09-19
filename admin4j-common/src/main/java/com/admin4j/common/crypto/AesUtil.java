package com.admin4j.common.crypto;

import com.admin4j.common.exception.EncryptException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * AES-128-CBC 算法
 * iv 默认取key
 *
 * @author andanyang
 * @since 2023/9/19 10:04
 */
public class AesUtil {

    /**
     * AES
     */
    static String ALGORITHM = "AES";
    /**
     * AES 算法
     */
    static String AES_CBC_CIPHER = "AES/CBC/PKCS5Padding";
    /**
     * 偏移量
     */
    private static final int OFFSET = 16;


    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key  加密密码
     * @return
     */
    public static byte[] encrypt(byte[] data, byte[] key) {
        try {
            if (key.length < OFFSET) {
                byte[] tmp = new byte[OFFSET];
                System.arraycopy(key, 0, tmp, 0, key.length);
                key = tmp;
            }
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(key, 0, OFFSET);
            Cipher cipher = Cipher.getInstance(AES_CBC_CIPHER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new EncryptException("aes encrypt error:" + e.getMessage(), e);
        }
    }

    /**
     * 解密
     *
     * @param data 待解密内容
     * @param key  解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] data, byte[] key) {
        try {
            if (key.length < 16) {
                byte[] tmp = new byte[16];
                System.arraycopy(key, 0, tmp, 0, key.length);
                key = tmp;
            }
            SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec secretKeySpec = new SecretKeySpec(enCodeFormat, ALGORITHM);
            Cipher cipher = Cipher.getInstance(AES_CBC_CIPHER);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(key));
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new EncryptException("ase decrypt error", e);
        }
    }

    /**
     * 加密
     *
     * @param data 需要加密的内容
     * @param key  加密密码
     * @return
     */
    public static String encrypt(String data, String key) {
        byte[] valueByte = encrypt(data.getBytes(StandardCharsets.UTF_8), key.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(valueByte);
    }

    /**
     * 解密
     *
     * @param data 待解密内容 base64 字符串
     * @param key  解密密钥
     * @return
     */
    public static String decrypt(String data, String key) {
        byte[] originalData = Base64.getDecoder().decode(data.getBytes());
        byte[] valueByte = decrypt(originalData, key.getBytes(StandardCharsets.UTF_8));
        return new String(valueByte);
    }
}
