package com.guhecloud.cube.srv.gen.business.info.sync.server.controller.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/27 15:57
 * @Description: 第三方业务回调参数
 */
@Data
public class ThirdPartyWebhookParam {

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 业务数据
     */
    private String data;
}
