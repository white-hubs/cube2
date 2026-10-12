package com.guhecloud.cube.biz.ouaa.server.verifier;

import com.guhecloud.cube.biz.ouaa.server.captcha.model.common.ResponseModel;
import com.guhecloud.cube.biz.ouaa.server.captcha.model.vo.CaptchaVO;
import com.guhecloud.cube.biz.ouaa.server.captcha.service.CaptchaService;
import com.guhecloud.cube.biz.ouaa.server.enumeration.CaptchaTypeEnum;
import com.guhecloud.cube.biz.ouaa.server.service.VerificationCodeService;
import com.guhecloud.cube.biz.ouaa.server.verifier.dto.CaptchaVerifyDTO;
import com.guhecloud.platform.base.standard.exception.ErrorTypeEnum;
import com.guhecloud.platform.base.standard.exception.OuaaException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CaptchaVerifier {
    private final VerificationCodeService verificationCodeService;
    private final CaptchaService captchaService;

    public void verify(CaptchaVerifyDTO captchaVerifyDTO) {
        CaptchaTypeEnum captchaType = captchaVerifyDTO.captchaType();
        boolean success = false;
        String msg = "";
        String code = "";
        if (captchaType == null || captchaType.equals(CaptchaTypeEnum.CODE)) {
            success = verificationCodeService.verify(captchaVerifyDTO.code(), captchaVerifyDTO.captchaKey());
            msg = "验证码错误";
            code = ErrorTypeEnum.CAPTCHA_ERROR.getCode();
        } else if (captchaType.equals(CaptchaTypeEnum.BLOCK_PUZZLE)) {
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(captchaVerifyDTO.captchaKey());
            ResponseModel responseModel = captchaService.verification(captchaVO);
            success = responseModel.isSuccess();
            code = responseModel.getRepCode();
            msg = responseModel.getRepMsg();
        }

        if (!success) {
            log.error("验证码错误信息:{}", msg);
            throw new OuaaException(msg, code, ErrorTypeEnum.CAPTCHA_ERROR.getErrType());
        }
    }
}
