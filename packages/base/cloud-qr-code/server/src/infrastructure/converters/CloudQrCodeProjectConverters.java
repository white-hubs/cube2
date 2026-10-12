package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateCloudQrCodeProjectCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateCloudQrCodeProjectCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.CloudQrCodeProject;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQRCodeProjectPO;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudQrCodeProjectVO;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.time.LocalDateTime;

public final class CloudQrCodeProjectConverters {
    private CloudQrCodeProjectConverters() {
    }

    public static CloudQrCodeProject commandToEntity(CreateCloudQrCodeProjectCommand command) {
        if (command == null) {
            return null;
        }
        var entity = new CloudQrCodeProject();
        entity.setName(command.getName());
        entity.setCreatedTime(LocalDateTime.now());
        entity.setCreatedBy(SecurityContextHolder.getPrincipal().getAccount());
        entity.setUpdatedTime(LocalDateTime.now());
        entity.setUpdatedBy(SecurityContextHolder.getPrincipal().getAccount());
        return entity;
    }

    public static CloudQrCodeProject commandToEntity(UpdateCloudQrCodeProjectCommand command, CloudQrCodeProject existingEntity) {
        if (command == null || existingEntity == null) {
            return null;
        }
        existingEntity.setName(command.getName());
        existingEntity.setUpdatedTime(LocalDateTime.now());
        existingEntity.setUpdatedBy(SecurityContextHolder.getPrincipal().getAccount());
        return existingEntity;
    }

    public static CloudQRCodeProjectPO entityToPO(CloudQrCodeProject entity) {
        if (entity == null) {
            return null;
        }
        var po = new CloudQRCodeProjectPO();
        po.setId(entity.getId());
        po.setName(entity.getName());
        po.setCode(entity.getCode());
        po.setSecret(entity.getSecret());
        po.setDeleted(false);
        po.setCreateTime(entity.getCreatedTime());
        po.setCreateBy(entity.getCreatedBy());
        po.setUpdateTime(entity.getUpdatedTime());
        po.setUpdateBy(entity.getUpdatedBy());
        return po;
    }

    public static CloudQrCodeProject poToEntity(CloudQRCodeProjectPO po) {
        if (po == null) {
            return null;
        }
        var entity = new CloudQrCodeProject();
        entity.setId(po.getId());
        entity.setName(po.getName());
        entity.setCode(po.getCode());
        entity.setSecret(po.getSecret());
        entity.setCreatedTime(po.getCreateTime());
        entity.setCreatedBy(po.getCreateBy());
        entity.setUpdatedTime(po.getUpdateTime());
        entity.setUpdatedBy(po.getUpdateBy());
        return entity;
    }

    public static CloudQrCodeProjectVO entityToVO(CloudQrCodeProject entity) {
        if (entity == null) {
            return null;
        }
        var vo = new CloudQrCodeProjectVO();
        vo.setId(entity.getId());
        vo.setName(entity.getName());
        vo.setCode(entity.getCode());
        vo.setSecret(entity.getSecret());
        vo.setUpdateTime(entity.getUpdatedTime());
        return vo;
    }
}
