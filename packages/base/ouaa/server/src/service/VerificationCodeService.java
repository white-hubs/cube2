package com.guhecloud.cube.biz.ouaa.server.service;

/**
 * @author pengjian
 * @date 2022/10/9 14:23
 * @description
 */
public interface VerificationCodeService {

    String PWD_CODE_KEY = "pwdCode:";

    boolean verify(String inputCode, String key);

}
