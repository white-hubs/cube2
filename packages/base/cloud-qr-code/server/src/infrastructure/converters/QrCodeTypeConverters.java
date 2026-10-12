package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeTypeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQrCodeTypePO;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.valueobject.QrCodeType;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudQrCodeTypeVO;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * 二维码类型转换工具类
 */
public final class QrCodeTypeConverters {

    private QrCodeTypeConverters() {
    }

    public static QrCodeType commandToEntity(CreateQrCodeTypeCommand command) {
        if (command == null) {
            return null;
        }
        var entity = new QrCodeType();
        entity.setCode(command.getCode());
        entity.setName(command.getName());
        entity.setGrantClientId(command.getGrantClientId());
        entity.setRemark(command.getRemark());
        entity.setCreatedTime(LocalDateTime.now());
        entity.setCreatedBy(SecurityContextHolder.getPrincipal().getAccount());
        entity.setUpdatedTime(LocalDateTime.now());
        entity.setUpdatedBy(SecurityContextHolder.getPrincipal().getAccount());
        return entity;
    }

    public static QrCodeType commandToEntity(UpdateQrCodeTypeCommand command, QrCodeType existingEntity) {
        if (command == null || existingEntity == null) {
            return null;
        }
        existingEntity.setCode(command.getCode());
        existingEntity.setName(command.getName());
        existingEntity.setRemark(command.getRemark());
        existingEntity.setUpdatedTime(LocalDateTime.now());
        existingEntity.setUpdatedBy(SecurityContextHolder.getPrincipal().getAccount());
        return existingEntity;
    }

    public static CloudQrCodeTypePO entityToPO(QrCodeType entity) {
        if (entity == null) {
            return null;
        }
        var po = new CloudQrCodeTypePO();
        po.setId(entity.getId());
        po.setCode(entity.getCode());
        po.setName(entity.getName());
        po.setGrantClientId(entity.getGrantClientId());
        po.setRemark(entity.getRemark());
        po.setCreateTime(entity.getCreatedTime());
        po.setCreateBy(entity.getCreatedBy());
        po.setUpdateTime(entity.getUpdatedTime());
        po.setUpdateBy(entity.getUpdatedBy());
        return po;
    }

    public static QrCodeType poToEntity(CloudQrCodeTypePO po) {
        if (po == null) {
            return null;
        }
        var entity = new QrCodeType();
        entity.setId(po.getId());
        entity.setCode(po.getCode());
        entity.setName(po.getName());
        entity.setGrantClientId(po.getGrantClientId());
        entity.setRemark(po.getRemark());
        entity.setCreatedTime(po.getCreateTime());
        entity.setCreatedBy(po.getCreateBy());
        entity.setUpdatedTime(po.getUpdateTime());
        entity.setUpdatedBy(po.getUpdateBy());
        return entity;
    }

    public static CloudQrCodeTypeVO entityToVO(QrCodeType entity) {
        if (entity == null) {
            return null;
        }
        var vo = new CloudQrCodeTypeVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getCode());
        vo.setName(entity.getName());
        vo.setRemark(entity.getRemark());
        vo.setGrantClientId(entity.getGrantClientId());
        vo.setUpdateTime(entity.getUpdatedTime());
        return vo;
    }
}
