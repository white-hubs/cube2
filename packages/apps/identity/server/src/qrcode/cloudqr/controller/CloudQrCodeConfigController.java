package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.controller;

import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.param.ModifyCloudQrCodeConfigParam;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.po.CloudQrCodeConfigPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service.CloudQrCodeConfigService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.vo.CloudQrCodeConfigVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeConfigController {

    private final CloudQrCodeConfigService cloudQrCodeConfigService;

    /**
     * 保存或更新云二维码配置
     *
     * @param param 配置对象
     */
    @PostMapping("/cloud-qr-cloud-config")
    public void save(@Valid @RequestBody ModifyCloudQrCodeConfigParam param) {
        this.cloudQrCodeConfigService.saveConfig(param);
    }

    /**
     * 获取当前租户的启用配置
     *
     * @return 云二维码配置，未启用返回null
     */
    @GetMapping("/cloud-qr-cloud-config")
    public CloudQrCodeConfigVO getQrCodeConfig() {
        CloudQrCodeConfigPO config = this.cloudQrCodeConfigService.getConfig();
        if (config == null) {
            return null;
        }
        var vo = new CloudQrCodeConfigVO();
        vo.setId(config.getId());
        vo.setConfigName(config.getConfigName());
        vo.setAppId(config.getAppId());
        vo.setAppSecret(config.getAppSecret());
        vo.setServerUrl(config.getServerUrl());
        vo.setEnabled(config.getEnabled());
        vo.setRemark(config.getRemark());
        return vo;
    }
}
