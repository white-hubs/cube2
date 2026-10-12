package com.guhecloud.cube.base.message2.server.code.controller;

import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.param.CodeVerifyParam;
import com.guhecloud.cube.base.message2.client.param.SendVerifyCodeParam;
import com.guhecloud.cube.base.message2.server.code.service.CodeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台消息发送接口
 *
 * @author ht
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CodeController {
    private final CodeService codeService;

    /**
     * 发送验证码
     */
    @PostMapping("send/code")
    public CodeResponseDTO sendCode(@Valid @RequestBody SendVerifyCodeParam param) {
        return codeService.sendCode(param);

    }

    /**
     * 验证码校验
     */
    @PostMapping("verify/code")
    public CodeResponseDTO verifyCode(@Valid @RequestBody CodeVerifyParam param) {
        return codeService.verifyCode(param);
    }

}