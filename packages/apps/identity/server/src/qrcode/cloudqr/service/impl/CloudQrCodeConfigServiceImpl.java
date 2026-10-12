package com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.dao.CloudQrCodeConfigMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.CloudQrCodeConfigEnabledEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.param.ModifyCloudQrCodeConfigParam;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.po.CloudQrCodeConfigPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.service.CloudQrCodeConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 云二维码配置服务实现
 *
 * @author system
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeConfigServiceImpl extends ServiceImpl<CloudQrCodeConfigMapper, CloudQrCodeConfigPO>
        implements CloudQrCodeConfigService {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public CloudQrCodeConfigPO getConfig() {
        return this.baseMapper.selectOne(
                Wrappers.<CloudQrCodeConfigPO>lambdaQuery()
                        .last("LIMIT 1")
                                        );
    }

    @Override
    public CloudQrCodeConfigPO getEnabledConfig() {
        return this.baseMapper.selectOne(
                Wrappers.<CloudQrCodeConfigPO>lambdaQuery()
                        .eq(CloudQrCodeConfigPO::getEnabled, true)
                        .last("LIMIT 1")
                                        );
    }

    @Override
    public List<CloudQrCodeConfigPO> getAllEnabledConfigs() {
        return this.baseMapper.selectList(
                Wrappers.<CloudQrCodeConfigPO>lambdaQuery()
                        .eq(CloudQrCodeConfigPO::getEnabled, true)
                                         );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveConfig(ModifyCloudQrCodeConfigParam param) {
        var cloudQrCodeConfigPO = this.baseMapper.selectOne(null);
        var isNew = Objects.isNull(cloudQrCodeConfigPO);
        var wasEnabled = !isNew && Boolean.TRUE.equals(cloudQrCodeConfigPO.getEnabled());

        if (isNew) {
            cloudQrCodeConfigPO = new CloudQrCodeConfigPO();
        }

        cloudQrCodeConfigPO.setConfigName(param.getConfigName());
        cloudQrCodeConfigPO.setAppId(param.getAppId());
        cloudQrCodeConfigPO.setAppSecret(param.getAppSecret());
        cloudQrCodeConfigPO.setServerUrl(param.getServerUrl());
        cloudQrCodeConfigPO.setEnabled(param.getEnabled());
        cloudQrCodeConfigPO.setRemark(param.getRemark());

        this.saveOrUpdate(cloudQrCodeConfigPO);

        // 如果启用了云二维码（新增时启用 或 从禁用切换为启用），发布配置启用事件
        var shouldInitialize = Boolean.TRUE.equals(param.getEnabled()) && !wasEnabled;

        if (shouldInitialize) {
            log.info("云二维码配置已启用，发布配置启用事件，租户={}", cloudQrCodeConfigPO.getTenant());
            eventPublisher.publishEvent(new CloudQrCodeConfigEnabledEvent(this, cloudQrCodeConfigPO));
        } else if (Boolean.TRUE.equals(param.getEnabled()) && wasEnabled) {
            log.info("云二维码配置已更新，配置仍为启用状态，不触发同步");
        }
    }
}
