package com.study.boardvue3.encoder;

import java.security.NoSuchAlgorithmException;

public interface Encryptor {

    /**
     * string을 암호화한다.
     *
     * @param string 암호화할 문자열
     * @return
     * @throws NoSuchAlgorithmException
     */
    String encrypt(String string) throws NoSuchAlgorithmException;
}
