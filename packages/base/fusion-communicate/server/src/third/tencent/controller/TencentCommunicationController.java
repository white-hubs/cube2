package com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.controller;

import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.TencentRealTimeCommunicationApiService;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.api.config.TencentConfig;
import com.guhecloud.cube.biz.fusion.communicate.server.third.tencent.controller.vo.TencentSigVO;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 腾讯实时音视频接口
 *
 * @author lanchun
 * @date 2025/2/11 15:40
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TencentCommunicationController {

    private final TencentRealTimeCommunicationApiService tencentRealTimeCommunicationApiService;

    /**
     * 获取用户鉴权信息
     *
     * @return TencentSigVO
     */
    @GetMapping("/tencent/communication/sig")
    public TencentSigVO getTencentSig() {
        TencentConfig tenantConfig = TencentConfig.getTenantConfig();
        String sig = tencentRealTimeCommunicationApiService.getSig(SecurityContextHolder.getPrincipal().getId().toString());
        return TencentSigVO.builder()
                .sdkAppId(tenantConfig.getSdkAppId())
                .sig(sig)
                .build();
    }
}
