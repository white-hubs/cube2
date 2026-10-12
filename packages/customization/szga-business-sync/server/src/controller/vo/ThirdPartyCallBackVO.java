package com.guhecloud.cube.srv.gen.business.info.sync.server.controller.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/27 16:18
 * @Description:
 */
@Data
public class ThirdPartyCallBackVO {
    private String code;

    private String message;

    private String data;
}
