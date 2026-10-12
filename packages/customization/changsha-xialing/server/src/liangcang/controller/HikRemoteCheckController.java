package com.guhecloud.cube.customization.xialing.server.liangcang.controller;

import com.guhecloud.cube.customization.xialing.server.liangcang.service.HikRemoteCheckService;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.HikRemoteCheckRequest;
import com.guhecloud.cube.customization.xialing.server.liangcang.vo.HikRemoteCheckResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** 海康门禁远程核验回调接口。 */
@RestController
@RequiredArgsConstructor
public class HikRemoteCheckController {

    private final HikRemoteCheckService hikRemoteCheckService;

    /**
     * 接收海康门禁远程核验回调。
     *
     * @param tenant 租户 ID
     * @param request AccessControllerEvent JSON 部分
     * @param picture Picture 图片部分
     * @return 是否接收成功
     */
    @PostMapping(value = "/remoteCheck/{tenant}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public HikRemoteCheckResponse remoteCheck(@PathVariable("tenant") Integer tenant,
                                              @RequestPart("AccessControllerEvent") HikRemoteCheckRequest request,
                                              @RequestPart(value = "Picture", required = false) MultipartFile picture) {
        return hikRemoteCheckService.receive(tenant, request, picture);
    }
}
