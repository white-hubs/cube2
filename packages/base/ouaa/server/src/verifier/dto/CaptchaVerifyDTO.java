package com.guhecloud.cube.biz.ouaa.server.verifier.dto;

import com.guhecloud.cube.biz.ouaa.server.enumeration.CaptchaTypeEnum;

public record CaptchaVerifyDTO(String code, String captchaKey, CaptchaTypeEnum captchaType) {
}
