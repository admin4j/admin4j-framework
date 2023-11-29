// package com.admin4j.common.crypto;
//
// import org.junit.Before;
// import org.junit.Test;
//
// import java.security.NoSuchAlgorithmException;
// import java.util.Base64;
//
// /**
//  * @author andanyang
//  * @since 2023/11/27 16:26
//  */
// public class RSAUtilTest {
//     private static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqLh8XuAeLvjCly5hesbWtjanpgjVOwNGMPX8lx2937pdP48KlaacrW576lconObZahf60CuNohFTyZrRf9y770tXrC1NF2iDAxZpFjsZChlbftwmk94Okvq/9AkJxyprXZniLZ7fU9gI77sVsKx1wASpaAsLFwSu/y4pgsMa+hESr3r0mRZ1hoVSvKr53EbJfqgtoeeqRYzTZJzbpwKyPhrP2fEIG/9NGgiehp0vIzyFtKET9FcLZZzPi0nA6pVIohZUMS1t/qFdwWuebjBDqfH86ZapqUBYqhWBMr8xBI4vYYH3y02eJwYILpKMlJlEXH55C360+g+Zxg8A9mLZ9QIDAQAB";
//     private static final String PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCouHxe4B4u+MKXLmF6xta2NqemCNU7A0Yw9fyXHb3ful0/jwqVppytbnvqVyic5tlqF/rQK42iEVPJmtF/3LvvS1esLU0XaIMDFmkWOxkKGVt+3CaT3g6S+r/0CQnHKmtdmeItnt9T2AjvuxWwrHXABKloCwsXBK7/LimCwxr6ERKvevSZFnWGhVK8qvncRsl+qC2h56pFjNNknNunArI+Gs/Z8Qgb/00aCJ6GnS8jPIW0oRP0VwtlnM+LScDqlUiiFlQxLW3+oV3Ba55uMEOp8fzplqmpQFiqFYEyvzEEji9hgffLTZ4nBggukoyUmURcfnkLfrT6D5nGDwD2Ytn1AgMBAAECggEAX/zdXQi6g1SpOGN9t+EJ5I6BoJdj4HgDZfV8p+iWzoLzbCaQCgXJG25A91hw1ZsMVCyiV+5/XJXpCkiXKdxF22UM0vrO4iNmbcpBLRGgNDuq7yiGlhd+SSZ5MHg35OSAQrc6k2PQgJ3mr6TLOUFUmFLOok+uFoFmoez8VtVzMpK5dSWJwDETOzjYFIR85dI3V+ZJr4EodtcXlLPQnQnAts0J829Wu+N/LH6PGayRLm3HOe1AVCfIy4uW2EwhMVg3BLE7u2omITWgXwNIe/fHii7uokZkB2uKAHSaBKeyfdynvrZplKpWDsDH9Cz4u2MXQGSeuD+JeMvpKiMKgTFk3QKBgQDVT/V9hDjtmTCPhAEhejJ2Cldo8FSF/rpQB0+WHsai5pUaqKTk6tH62vCXG0QnW1pQVBp8Z8U8ZKfCyKBRdmGShNZ6vjcC0LGfR51cTh3/Rl/ywsfMkK3WcPSn1mDpzse9/zJ1HhGAdIXOe1OXMO9AAuGll4IPY/2nR2mHvF2lLwKBgQDKfBWFpzBFRsY+BlMeF4GzZIMufKVGbaVaNDn9V/3JZpMHkygvjmcF4exz+8GmLF5HsplZJdbXDGg5j+GAATFzMRHmJZOLSFTJlme0Lpye/kzU0JkWOQDnm2XO6LD6f7iomuD1Xcnd/Jto91uD1+E6rzufb9oOw8EJxu/6tGTyGwKBgQDJZKOfLJ3e3Xn2lafHpqpLvfnG7tiuZdAbzLs8PbRGirMNp1l/c6BqWhk6YRjYm6xKGQ2klQinu1SUV3zdTIpUniwtWLdxZf29Jw0P4AT8RcJC3dlrbtFhm+WxLHr1ZDA7VtyZrJjTka/fQZqrLR1FbzMBd2jpBPuv2oFtEM/NKwKBgArQTaXxo9ZPTU8Kr22v+7FE8OyOo5T7ThVfLKmnBVq4K6n/5emERWQ/CI25KEJjpDVYCHCGYM7jTr2kPXrElYt9V2NfJl4N4tlROwCYbKzhD+Fdso9JRA8acXl3W9xE7euzOchg1eMRFouoii6kXNbxfNGq+45GTgzjnvVYpPt5AoGAZ1fE7Dj+y5bhyIl0mQq7DV3Zeoi97E2h7a8Z2SAvQjgk9EPnU4oQLbOdSwsxVSC/jivfFY82tckMcirBxQjz8VAzFy/DNgnlWyN76WeCZgzgyTuOPnRjOCPvvPMWDApSzUY9UwxAY4eqvCaziRMFEMRI+M1Nr0+ICYGC0YuSvec=";
//     static RSAUtil.RSAKeyPair rsaKeyPair;
//
//     @Before
//     public void init() throws NoSuchAlgorithmException {
//         rsaKeyPair = RSAUtil.generateKeyPair(2048);
//         System.out.println("getPublicKey = " + rsaKeyPair.getPublicKey());
//         System.out.println("getPrivateKey = " + rsaKeyPair.getPrivateKey());
//     }
//
//     @Test
//     public void testEncrypt() {
//
//         // RSAUtil.
//         String data = "bec36c76-d315-40bd-af6d-cf32feee49ae-221";
//         byte[] keyBytes = PRIVATE_KEY.getBytes();
//
//         byte[] encrypt = RSAUtil.encrypt(data.getBytes(), RSAUtil.getPrivateKey(keyBytes));
//         System.out.println("encrypt = " + Base64.getEncoder().encodeToString(encrypt));
//
//         // // // TODO TEST
//         // String decrypt = RSAUtil.decrypt(encrypt, PUBLIC_KEY);
//         // System.out.println("decrypt = " + decrypt);
//         // assert encrypt.equals(decrypt);
//     }
//
//     public void testDecrypt() {
//     }
//
//
// }