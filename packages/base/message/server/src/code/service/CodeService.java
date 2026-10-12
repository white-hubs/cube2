package com.guhecloud.cube.base.message2.server.code.service;

import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.client.param.CodeVerifyParam;
import com.guhecloud.cube.base.message2.client.param.SendVerifyCodeParam;

public interface CodeService {

    CodeResponseDTO sendCode(SendVerifyCodeParam param);

    CodeResponseDTO verifyCode(CodeVerifyParam param);
}
