package com.guhecloud.cube.srv.gen.adapter.video.server.application.h5s.facade.dto.response;

import lombok.Data;

@Data
public class H5sVideoLoginResponse extends BasicResponse {

    /**
     * 登录session
     */
    private String strSession;
}
