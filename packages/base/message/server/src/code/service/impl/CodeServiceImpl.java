package com.guhecloud.cube.base.message2.server.code.service.impl;

import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.param.CodeVerifyParam;
import com.guhecloud.cube.base.message2.client.param.SendVerifyCodeParam;
import com.guhecloud.cube.base.message2.server.code.holder.CodeServiceHolder;
import com.guhecloud.cube.base.message2.server.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class CodeServiceImpl implements CodeService {

    private final CodeServiceHolder codeServiceHolder;

    @Override
    public CodeResponseDTO sendCode(SendVerifyCodeParam param) {

        CodeService codeService = codeServiceHolder.getCodeService(param.getChannel());
        return codeService.sendCode(param);
    }

    @Override
    public CodeResponseDTO verifyCode(CodeVerifyParam param) {
        CodeService codeService = codeServiceHolder.getCodeService(param.getChannel());
        return codeService.verifyCode(param);

    }

}
