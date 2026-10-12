package com.guhecloud.cube.srv.gen.adapter.video.server.application.hikvision.isc.facade.dto.response;

import lombok.Data;

/**
 * ISC抓拍返回参数
 *
 * @author lanchun
 * @date 2023/11/10 10:38
 */
@Data
public class IscVideoSnapCaptureResponse {

    /**
     * 图片地址
     */
    private String picUrl;
}
