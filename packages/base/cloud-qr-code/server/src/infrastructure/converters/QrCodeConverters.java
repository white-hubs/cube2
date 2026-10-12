package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.BatchCreateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.CreateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.commandgateway.command.UpdateQrCodeCommand;
import com.guhecloud.cube.base.cloud.qr.code.server.application.querygateway.CloudAddressConfigQueryService;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.QrCode;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.enumeration.QrCodeContentTypeEnum;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQrCodePO;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.valueobject.QrCodeType;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.generator.ShortCodeService;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.CloudQrCodeTypeRepository;
import com.guhecloud.cube.base.cloud.qr.code.server.interfaces.vo.CloudQrCodeVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class QrCodeConverters {
    private QrCodeConverters() {
    }

    public static QrCode commandToEntity(CreateQrCodeCommand command) {
        if (command == null) {
            return null;
        }
        var shortCodeService = ApplicationContextHolder.getBean(ShortCodeService.class);
        var entity = new QrCode();
        entity.setId(IdWorker.getId());
        // 支持自定义code，如果没有提供则自动生成
        if (StringUtils.isNotBlank(command.getCode())) {
            entity.setCode(command.getCode());
        } else {
            entity.setCode(IdWorker.getIdStr());
        }
        entity.setName(command.getName());
        entity.setShortCode(shortCodeService.getNextShortCode());
        entity.setQrContentType(command.getQrContentType());
        entity.setQrContent(command.getQrContent());
        entity.setMode(command.getMode());
        entity.setModeContent(command.getModeContent());
        entity.setGrantClientId(command.getGrantClientId());
        entity.setEnabled(command.getEnabled());
        entity.setCreatedTime(LocalDateTime.now());
        entity.setCreatedBy(SecurityContextHolder.getPrincipal().getAccount());
        entity.setUpdatedTime(LocalDateTime.now());
        entity.setUpdatedBy(SecurityContextHolder.getPrincipal().getAccount());
        if (Objects.nonNull(command.getRoute())) {
            entity.setQrRoute(QrRouteConverters.commandToEntity(command.getRoute()));
        }
        if (command.getQrContentType().equals(QrCodeContentTypeEnum.LINK.getCode())) {
            var domainUrlService = ApplicationContextHolder.getBean(CloudAddressConfigQueryService.class);
            var addressConfig = domainUrlService.queryAddressConfig();
            Assert.isConflict(Objects.isNull(addressConfig), "云地址配置不存在");
            entity.setQrContent(addressConfig.getIpAddress() + entity.getShortCode());
        }
        // 支持通过qrTypeCode查询类型ID
        var qrTypeId = command.getQrTypeId();
        if (qrTypeId == null && StringUtils.isNotBlank(command.getQrTypeCode())) {
            var typeRepository = ApplicationContextHolder.getBean(CloudQrCodeTypeRepository.class);
            var qrType = typeRepository.findByGrantClientIdAndCode(
                    command.getGrantClientId(),
                    command.getQrTypeCode()
                                                                  );
            if (qrType != null) {
                qrTypeId = qrType.getId();
            }
        }
        entity.setQrTypeId(qrTypeId);
        return entity;
    }

    public static List<QrCode> commandToEntities(BatchCreateQrCodeCommand command) {
        if (CollectionUtils.isEmpty(command.getItems())) {
            return Collections.emptyList();
        }
        var clientId = SecurityContextHolder.getPrincipal().getAccount();
        var shortCodeService = ApplicationContextHolder.getBean(ShortCodeService.class);
        var domainUrlService = ApplicationContextHolder.getBean(CloudAddressConfigQueryService.class);
        var addressConfig = domainUrlService.queryAddressConfig();
        var typeRepository = ApplicationContextHolder.getBean(CloudQrCodeTypeRepository.class);
        var qrTypeMap = typeRepository.findByGrantClientId(clientId).stream()
                .collect(Collectors.toMap(QrCodeType::getCode, n -> n));
        return command.getItems().stream().map(item -> {
            var entity = new QrCode();
            entity.setId(IdWorker.getId());
            // 支持自定义code，如果没有提供则自动生成
            if (StringUtils.isNotBlank(item.getCode())) {
                entity.setCode(item.getCode());
            } else {
                entity.setCode(IdWorker.getIdStr());
            }
            entity.setName(item.getName());
            entity.setShortCode(shortCodeService.getNextShortCode());
            entity.setQrContentType(item.getQrContentType());
            entity.setQrContent(item.getQrContent());
            entity.setMode(item.getMode());
            entity.setModeContent(item.getModeContent());
            entity.setGrantClientId(clientId);
            entity.setEnabled(item.getEnabled());
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(SecurityContextHolder.getPrincipal().getAccount());
            entity.setUpdatedTime(LocalDateTime.now());
            entity.setUpdatedBy(SecurityContextHolder.getPrincipal().getAccount());
            if (Objects.nonNull(item.getRoute())) {
                entity.setQrRoute(QrRouteConverters.commandToEntity(item.getRoute()));
            }
            if (item.getQrContentType().equals(QrCodeContentTypeEnum.LINK.getCode())) {
                entity.setQrContent(addressConfig.getIpAddress() + entity.getShortCode());
            }
            // 支持通过qrTypeCode查询类型ID
            var qrCodeType = qrTypeMap.get(item.getQrTypeCode());
            entity.setQrTypeId(qrCodeType.getId());
            return entity;
        }).collect(Collectors.toList());
    }

    public static QrCode commandToEntity(UpdateQrCodeCommand command, QrCode existingEntity) {
        if (command == null || existingEntity == null) {
            return null;
        }
        existingEntity.setName(command.getName());
        existingEntity.setQrContentType(command.getQrContentType());
        existingEntity.setQrContent(command.getQrContent());
        existingEntity.setMode(command.getMode());
        existingEntity.setModeContent(command.getModeContent());
        existingEntity.setEnabled(command.getEnabled());
        existingEntity.setUpdatedTime(LocalDateTime.now());
        existingEntity.setUpdatedBy(SecurityContextHolder.getPrincipal().getAccount());
        if (Objects.nonNull(command.getRoute())) {
            existingEntity.setQrRoute(QrRouteConverters.commandToEntity(command.getRoute()));
        }
        if (command.getQrContentType().equals(QrCodeContentTypeEnum.LINK.getCode())) {
            var domainUrlService = ApplicationContextHolder.getBean(CloudAddressConfigQueryService.class);
            var addressConfig = domainUrlService.queryAddressConfig();
            Assert.isConflict(Objects.isNull(addressConfig), "云地址配置不存在");
            existingEntity.setQrContent(addressConfig.getIpAddress() + existingEntity.getShortCode());
        }
        return existingEntity;
    }

    public static CloudQrCodePO entityToPO(QrCode entity) {
        if (entity == null) {
            return null;
        }
        var po = new CloudQrCodePO();
        po.setId(entity.getId());
        po.setCode(entity.getCode());
        po.setName(entity.getName());
        po.setShortCode(entity.getShortCode());
        po.setQrTypeId(entity.getQrTypeId());
        po.setQrContentType(entity.getQrContentType());
        po.setQrContent(entity.getQrContent());
        po.setMode(entity.getMode());
        po.setModeContent(entity.getModeContent());
        po.setGrantClientId(entity.getGrantClientId());
        po.setEnabled(entity.getEnabled());
        po.setCreateTime(entity.getCreatedTime());
        po.setCreateBy(entity.getCreatedBy());
        po.setUpdateTime(entity.getUpdatedTime());
        po.setUpdateBy(entity.getUpdatedBy());
        return po;
    }

    public static QrCode poToEntity(CloudQrCodePO po) {
        if (po == null) {
            return null;
        }
        var qrCode = new QrCode();
        qrCode.setId(po.getId());
        qrCode.setCode(po.getCode());
        qrCode.setShortCode(po.getShortCode());
        qrCode.setName(po.getName());
        qrCode.setQrTypeId(po.getQrTypeId());
        qrCode.setQrContentType(po.getQrContentType());
        qrCode.setQrContent(po.getQrContent());
        qrCode.setMode(po.getMode());
        qrCode.setModeContent(po.getModeContent());
        qrCode.setGrantClientId(po.getGrantClientId());
        qrCode.setEnabled(po.getEnabled());
        qrCode.setCreatedTime(po.getCreateTime());
        qrCode.setCreatedBy(po.getCreateBy());
        qrCode.setUpdatedTime(po.getUpdateTime());
        qrCode.setUpdatedBy(po.getUpdateBy());
        return qrCode;
    }

    public static CloudQrCodeVO entityToVO(QrCode entity) {
        if (entity == null) {
            return null;
        }
        var vo = new CloudQrCodeVO();
        vo.setId(entity.getId());
        vo.setCode(entity.getCode());
        vo.setShortCode(entity.getShortCode());
        vo.setName(entity.getName());
        vo.setQrTypeId(entity.getQrTypeId());
        vo.setQrContentType(entity.getQrContentType());
        vo.setQrContent(entity.getQrContent());
        vo.setMode(entity.getMode());
        vo.setModeContent(entity.getModeContent());
        vo.setGrantClientId(entity.getGrantClientId());
        vo.setEnabled(entity.getEnabled());
        vo.setUpdateTime(entity.getUpdatedTime());
        if (Objects.nonNull(entity.getQrRoute())) {
            vo.setRoute(QrRouteConverters.entityToVO(entity.getQrRoute()));
        }
        return vo;
    }
}

