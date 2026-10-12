package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response;

import lombok.Data;

@Data
public class BasicResponse {

    /**
     * 状态
     */
    protected boolean bStatus;

    /**
     * API成功或者失败的具体描述
     */
    protected String strCode;
}
