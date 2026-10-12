package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.param.ModifyCloudQrCodeConfigParam;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.po.CloudQrCodeConfigPO;

import java.util.List;

/**
 * 云二维码配置服务
 *
 * @author system
 */
public interface CloudQrCodeConfigService extends IService<CloudQrCodeConfigPO> {

    CloudQrCodeConfigPO getConfig();

    /**
     * 获取当前租户的启用配置
     *
     * @return 云二维码配置，未启用返回null
     */
    CloudQrCodeConfigPO getEnabledConfig();

    /**
     * 获取所有租户的启用配置列表
     *
     * @return 启用的配置列表
     */
    List<CloudQrCodeConfigPO> getAllEnabledConfigs();

    /**
     * 保存或更新配置
     *
     * @param param 配置对象
     */
    void saveConfig(ModifyCloudQrCodeConfigParam param);
}
