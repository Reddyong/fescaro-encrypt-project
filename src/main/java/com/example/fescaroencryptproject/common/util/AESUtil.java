package com.example.fescaroencryptproject.common.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * 암호화 / 복호화 참고 블로그 : https://junyharang.tistory.com/364
 */
public class AESUtil {

    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int AES_KEY_SIZE = 16;
    private static final String SECRET_KEY = "0123456789abcdef";    // 우선 해당 프로젝트에선 임시로 비밀키 지정

    /**
     * AES secret key 생성
     */
    public static SecretKey getSecretKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
    }

    /**
     * 랜덤 IV 값 생성
     */
    public static byte[] generateIV() {
        byte[] iv = new byte[AES_KEY_SIZE];
        new SecureRandom().nextBytes(iv);   // 안전한 랜덤 난수 생성기로 IV값 채움

        return iv;
    }

    /**
     * AES 암호화
     */
    public static byte[] encrypt(byte[] file, byte[] iv, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        return cipher.doFinal(file);
    }

    /**
     * AES 복호화
     */
    public static byte[] decrypt(byte[] encryptedFile, byte[] iv, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        return cipher.doFinal(encryptedFile);
    }
}
