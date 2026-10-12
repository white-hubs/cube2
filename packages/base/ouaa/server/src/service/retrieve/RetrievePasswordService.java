package com.guhecloud.cube.biz.ouaa.server.service.retrieve;

import com.guhecloud.cube.biz.ouaa.server.dto.retrieve.RetrievePasswordResetDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.retrieve.RetrievePasswordTokenDTO;
import com.guhecloud.cube.biz.ouaa.server.param.retrieve.RetrievePasswordResetParam;
import com.guhecloud.cube.biz.ouaa.server.param.retrieve.RetrievePasswordTokenParam;

public interface RetrievePasswordService {
    RetrievePasswordTokenDTO getToken(RetrievePasswordTokenParam retrievePasswordTokenParam);

    RetrievePasswordResetDTO resetPassword(RetrievePasswordResetParam retrievePasswordResetParam);
}
