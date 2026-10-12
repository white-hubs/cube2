package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 腾讯云用户鉴权返回信息
 *
 * @author lanchun
 * @date 2025/2/18 10:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TencentSigVO {

    /**
     * TRTC的SDKAppId。
     */
    private Long sdkAppId;

    /**
     * 用户鉴权信息
     */
    private String sig;
}
