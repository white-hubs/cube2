package com.guhecloud.cube.base.message2.server.handler.handler.verifier;

import com.guhecloud.cube.base.message2.client.dto.CodeResponseDTO;
import com.guhecloud.cube.base.message2.server.handler.dto.CodeVerifyDTO;

public interface CodeVerify {
    default CodeResponseDTO verifyCode(CodeVerifyDTO codeVerifyDTO) {throw new UnsupportedOperationException();}
}
