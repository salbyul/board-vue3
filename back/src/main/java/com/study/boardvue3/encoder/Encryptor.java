package com.study.boardvue3.encoder;

import java.security.NoSuchAlgorithmException;

public interface Encryptor {

    String encrypt(String string) throws NoSuchAlgorithmException;
}
