package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters;

import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.SaveAddressConfigCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.AddressConfig;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudAddressConfigPO;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudAddressConfigVO;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.time.LocalDateTime;

/**
 * 云地址配置转换工具类
 */
public final class AddressConfigConverters {

    private AddressConfigConverters() {
    }

    public static AddressConfig commandToEntity(SaveAddressConfigCommand command, AddressConfig existingEntity) {
        if (command == null) {
            return null;
        }
        if (existingEntity == null) {
            existingEntity = new AddressConfig();
            existingEntity.setCreatedTime(LocalDateTime.now());
            existingEntity.setCreatedBy(SecurityContextHolder.getPrincipal().getAccount());
        }
        existingEntity.setIpAddress(command.getIpAddress());
        existingEntity.setUpdatedTime(LocalDateTime.now());
        existingEntity.setUpdatedBy(SecurityContextHolder.getPrincipal().getAccount());
        return existingEntity;
    }

    public static CloudAddressConfigPO entityToPO(AddressConfig entity) {
        if (entity == null) {
            return null;
        }
        var po = new CloudAddressConfigPO();
        po.setId(entity.getId());
        po.setIpAddress(entity.getIpAddress());
        po.setCreateTime(entity.getCreatedTime());
        po.setCreateBy(entity.getCreatedBy());
        po.setUpdateTime(entity.getUpdatedTime());
        po.setUpdateBy(entity.getUpdatedBy());
        return po;
    }

    public static AddressConfig poToEntity(CloudAddressConfigPO po) {
        if (po == null) {
            return null;
        }
        var entity = new AddressConfig();
        entity.setId(po.getId());
        entity.setIpAddress(po.getIpAddress());
        entity.setCreatedTime(po.getCreateTime());
        entity.setCreatedBy(po.getCreateBy());
        entity.setUpdatedTime(po.getUpdateTime());
        entity.setUpdatedBy(po.getUpdateBy());
        return entity;
    }

    public static CloudAddressConfigVO entityToVO(AddressConfig entity) {
        if (entity == null) {
            return null;
        }
        var vo = new CloudAddressConfigVO();
        vo.setId(entity.getId());
        vo.setIpAddress(entity.getIpAddress());
        return vo;
    }
}
