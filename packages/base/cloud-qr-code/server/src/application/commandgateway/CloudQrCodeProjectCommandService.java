package com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateCloudQrCodeProjectCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateCloudQrCodeProjectCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.CloudQrCodeProject;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.CloudQrCodeProjectConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeProjectRepository;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.param.TenantClientModifyParam;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 云二维码项目命令服务
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeProjectCommandService {

    private final CloudQrCodeProjectRepository repository;
    private final TenantClient tenantClient;

    @Transactional(rollbackFor = Exception.class)
    public CloudQrCodeProject create(CreateCloudQrCodeProjectCommand command) {
        // 转换为实体
        var entity = CloudQrCodeProjectConverters.commandToEntity(command);
        // 调用TenantClient新增租户客户端
        try {
            var param = new TenantClientModifyParam();
            param.setName(command.getName());
            param.setEnabled(true);
            param.setScopes("select");
            param.setGrantTypes("client_credentials");
            param.setAccessTokenTimeout(7200000);
            var clientDTO = tenantClient.innerAddTenantClient(param);
            entity.setId(clientDTO.getId());
            entity.setCode(clientDTO.getCode());
            entity.setSecret(clientDTO.getSecretPlain());
            // 保存到数据库
            repository.save(entity);
            log.info("成功创建租户客户端，code={}", entity.getCode());
        } catch (Exception e) {
            log.error("创建租户客户端失败", e);
            throw new RuntimeException("创建租户客户端失败", e);
        }
        return entity;
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, UpdateCloudQrCodeProjectCommand command) {
        var entity = repository.findById(id);
        Assert.isNotFound(entity == null, "云二维码项目不存在");
        // 更新实体
        CloudQrCodeProjectConverters.commandToEntity(command, entity);
        repository.update(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        var entity = repository.findById(id);
        Assert.isNotFound(entity == null, "云二维码项目不存在");
        // 删除数据库记录
        repository.deleteById(id);
        // 调用TenantClient移除租户客户端
        try {
            tenantClient.removeClient(entity.getCode());
            log.info("成功移除租户客户端，code={}", entity.getCode());
        } catch (Exception e) {
            log.error("移除租户客户端失败，code={}", entity.getCode(), e);
            // 不抛出异常，允许删除成功但移除客户端失败
        }
    }
}
