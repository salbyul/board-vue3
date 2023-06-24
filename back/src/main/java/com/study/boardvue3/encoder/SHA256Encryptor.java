package com.study.boardvue3.encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256Encryptor implements Encryptor {

    /**
     * string을 SHA256 알고리즘으로 암호화한다.
     *
     * @param string 암호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     */
    @Override
    public String encrypt(String string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(string.getBytes());
        return bytesToHex(md.digest());
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(String.format("%02x", b));
        }
        return stringBuilder.toString();
    }
}
