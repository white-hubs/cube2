package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.converter.CloudQrcodeItemConverter;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeBatchCreatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeBatchUpdatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeCreatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeDeletedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeUpdatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeCommandService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.EntityInfoCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeBatchGenerateCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.dto.QrcodeEntityInfo;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeStyleMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeThirdLinkMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.enumeration.QrcodeEnum;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.PropertyQrCodeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeApplicationVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeApplicationPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeThirdLinkPO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.QrcodeStyleQueryService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.handler.QrcodeStyleInstHandler;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.validator.QrcodeValidator;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.PinYinUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author bq
 * @date 2022/7/20
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeCommandServiceImpl extends ServiceImpl<QrcodeMapper, QrcodePO> implements QrcodeCommandService {

    private final QrcodeValidator validator;
    private final TenantClient tenantClient;
    private final QrcodeStyleMapper styleMapper;
    private final QrcodeStyleInstHandler qrcodeStyleInstHandler;
    private final QrcodeThirdLinkMapper qrcodeThirdLinkMapper;
    private final ApplicationClient applicationClient;
    private final QrcodeStyleQueryService qrcodeStyleQueryService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public synchronized String generateCode(TenantDTO tenantDTO) {
        String tenantHead = "";
        if (null != tenantDTO) {
            tenantHead = PinYinUtils.getHead(tenantDTO.getName());
        }
        return BizCodeUtils.generate(tenantHead).toUpperCase();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@Nonnull QrcodeCommand command) {
        // 验证是否存在相同的名称和code
        validator.verifyIsExistByQrcodeName(null, command.getName());
        validator.verifyIsExistByCode(null, command.getName());
        validator.verifyHasEntityAndType(command);
        validator.verifyEntityIsOnly(command.getEntityType(), command.getEntityId(), null);
        QrcodePO qrcodePO = ObjectUtils.copying(command, QrcodePO.class);
        // 保存和修改的时候就把二维码的内容实例化
        qrcodePO.setConfigJson(this.fillQrcodeStyle(command));
        // 保存数据
        this.save(qrcodePO);

        // 发布创建事件，由监听器异步处理云同步
        var qrTypeCode = String.valueOf(command.getEntityType());
        var routeUrl = qrcodeStyleInstHandler.getQrCodeUrl(qrcodePO.getCode());
        applicationEventPublisher.publishEvent(
                new QrcodeCreatedEvent(this, qrcodePO.getCode(), qrcodePO.getName(), qrTypeCode, routeUrl)
                                              );

        //保存第三方链接
        this.saveThirdLinks(qrcodePO.getId(), command);
    }

    private void saveThirdLinks(Long id, QrcodeCommand command) {
        //先删除二维码下的三方链接
        qrcodeThirdLinkMapper.delete(Wrappers.<QrcodeThirdLinkPO>lambdaQuery()
                                             .eq(QrcodeThirdLinkPO::getQrcodeId, id));

        if (CollectionUtils.isNotEmpty(command.getThirdLinks())) {
            command.getThirdLinks().forEach(link -> {
                QrcodeThirdLinkPO copying = ObjectUtils.copying(link, QrcodeThirdLinkPO.class);
                copying.setQrcodeId(id);
                qrcodeThirdLinkMapper.insert(copying);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, QrcodeCommand command) {
        if (null == id) {
            return;
        }
        QrcodePO qrcodePO = this.getById(id);
        // 验证是否存在相同的名称和code
        validator.verifyIsExistByQrcodeName(id, command.getName());
        validator.verifyIsExistByCode(id, command.getName());
        validator.verifyHasEntityAndType(command);
        validator.verifyEntityIsOnly(command.getEntityType(), command.getEntityId(), id);
        ObjectUtils.copying(command, qrcodePO);
        // 在保存和修改的时候就把二维码的内容实例化
        qrcodePO.setConfigJson(this.fillQrcodeStyle(command));
        this.updateById(qrcodePO);

        // 发布更新事件，由监听器异步处理云同步
        var routeUrl = qrcodeStyleInstHandler.getQrCodeUrl(qrcodePO.getCode());
        applicationEventPublisher.publishEvent(
                new QrcodeUpdatedEvent(this, qrcodePO.getCode(), qrcodePO.getName(), routeUrl)
                                              );

        //保存第三方链接
        this.saveThirdLinks(id, command);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(Long id) {
        QrcodePO qrcodePO = this.getById(id);
        if (qrcodePO != null) {
            // 发布删除事件，由监听器异步处理云同步
            applicationEventPublisher.publishEvent(
                    new QrcodeDeletedEvent(this, qrcodePO.getCode())
                                                  );
        }
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(String ids) {
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                this.remove(Long.valueOf(id));
            }
        } else {
            this.remove(Long.valueOf(ids));
        }
    }

    @Override
    public void updateQrcodeByStyle(Long styleId, String json) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        new Thread(() -> {
            SessionContextHolder.requestTenant(tenant);
            List<QrcodePO> qrcodePOS = this.lambdaQuery().eq(QrcodePO::getStyleId, styleId).list();
            qrcodePOS.forEach(qrcodePO -> {
                try {
                    EntityInfoCommand entityInfoCommand = EntityInfoCommand.builder().entityId(qrcodePO.getEntityId())
                            .entityName(qrcodePO.getEntityName())
                            .build();
                    String styleToInst = qrcodeStyleInstHandler.qrcodeStyleToInst(entityInfoCommand, qrcodePO.getCode(), json);
                    qrcodePO.setConfigJson(styleToInst);
                    this.updateById(qrcodePO);
                } catch (Exception e) {
                    log.error("二维码样式更新异常,code:" + qrcodePO.getCode() + ",异常信息:", e);
                }
            });
        }, "updateQrcodeByStyle").start();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PropertyQrCodeVO> batchGenerateQRCodeImage(QrcodeBatchGenerateCommand command) {
        Assert.parameterIsNull(command, "生成参数不能为空");
        Assert.parameterIsNull(command.getEntityType(), "实体类型不能为空");
        Assert.parameterIsNull(command.getEntityInfo(), "实体对象id不能为空");
        List<PropertyQrCodeVO> vos = new ArrayList<>();
        List<QrcodePO> savePos = new ArrayList<>();
        List<QrcodePO> updatePos = new ArrayList<>();
        List<Long> entityIds = command.getEntityInfo().stream()
                .map(EntityInfoCommand::getEntityId)
                .filter(ObjectUtils::isNotNull)
                .distinct()
                .collect(Collectors.toList());
        Assert.parameterIsNull(entityIds, "实体对象id不能为空");
        List<QrcodePO> list = this.lambdaQuery()
                .eq(QrcodePO::getEntityType, command.getEntityType())
                .in(QrcodePO::getEntityId, entityIds)
                .list();
        Map<Long, List<QrcodePO>> map = new HashMap<>();
        if (ObjectUtils.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.groupingBy(QrcodePO::getEntityId));
        }
        Map<Long, List<QrcodePO>> finalMap = map;
        Long styleId = qrcodeStyleQueryService.getDefaultStyleIdByEntityType(command.getEntityType());
        Assert.isNotFound(styleId, "该实体类型没有默认样式！");
        QrcodeStylePO stylePO = styleMapper.selectById(styleId);
        Assert.isNotFound(stylePO, "样式不存在或已被删除！");
        //获取实体业务信息
        Map<Long, Object> entityInfofoMap = qrcodeStyleInstHandler.batchGetEntityInfo(entityIds, command.getEntityType());
        command.setStyleId(styleId);
        command.setStylePO(stylePO);
        TenantDTO tenantDTO = getTenantIfNeedCreate(command.getEntityInfo(), finalMap);
        command.getEntityInfo().forEach(entity -> {
            entity.setBusinessEntity(entityInfofoMap.get(entity.getEntityId()));
            List<QrcodePO> qrcodePOS = finalMap.get(entity.getEntityId());
            QrcodePO po = new QrcodePO();
            if (ObjectUtils.isNotEmpty(qrcodePOS)) {
                po = qrcodePOS.getFirst();
                po.setName(entity.getEntityName());
                po.setEntityName(entity.getEntityName());
                po.setEntityType(command.getEntityType());
                po.setStyleId(styleId);
                po.setConfigJson(createQRCode(entity, po.getCode(), command));
                updatePos.add(po);
            } else {
                po.setCode(generateCode(tenantDTO));
                po.setName(entity.getEntityName());
                po.setEntityType(command.getEntityType());
                po.setEntityId(entity.getEntityId());
                po.setEntityName(entity.getEntityName());
                po.setStyleId(styleId);
                po.setConfigJson(createQRCode(entity, po.getCode(), command));
                savePos.add(po);
            }
            vos.add(getQrCodeVO(po, entity.getSerialNumber()));
        });
        if (ObjectUtils.isNotEmpty(updatePos)) {
            //批量更新二维码
            this.updateBatchById(updatePos);
            // 发布批量更新事件
            var updateItems = CloudQrcodeItemConverter.buildQrcodeUpdateItems(updatePos);
            var qrcodeBatchUpdatedEvent = new QrcodeBatchUpdatedEvent(this, updateItems);
            applicationEventPublisher.publishEvent(qrcodeBatchUpdatedEvent);
        }
        if (ObjectUtils.isNotEmpty(savePos)) {
            //批量保存二维码
            this.saveBatch(savePos);
            // 发布批量创建事件
            var createItems = CloudQrcodeItemConverter.buildQrcodeCreateItems(savePos);
            var qrcodeBatchCreatedEvent = new QrcodeBatchCreatedEvent(this, createItems);
            applicationEventPublisher.publishEvent(qrcodeBatchCreatedEvent);
        }
        return vos;
    }

    @Override
    public void batchDownloadQRCodeImage(QrcodeBatchGenerateCommand command, HttpServletResponse response) throws IOException {
        Assert.parameterIsNull(command, "下载参数不能为空");
        List<PropertyQrCodeVO> qrCodes;
        if (CollectionUtils.isNotEmpty(command.getQrcodeIds())) {
            qrCodes = getQrCodesByIds(command.getQrcodeIds());
        } else {
            Assert.parameterIsNull(command.getEntityType(), "实体类型不能为空");
            Assert.parameterIsNull(command.getEntityInfo(), "实体对象id不能为空");
            qrCodes = batchGenerateQRCodeImage(command);
        }
        Assert.parameterIsNull(qrCodes, "未找到可下载的二维码");
        writeQrCodeZip(qrCodes, buildQrCodeContentMap(qrCodes), response);
    }

    @Override
    public String oldQRCodeCompatible(String type, Long objId) {
        List<QrcodePO> list = this.lambdaQuery().eq(QrcodePO::getEntityId, objId).list();
        if (ObjectUtils.isNotEmpty(list)) {
            return list.get(0).getCode();
        }
        QrcodeEntityInfo entityInfo = qrcodeStyleInstHandler.getObjInfo(type, objId);
        Assert.parameterIsNull(entityInfo, "对象类型无法识别");

        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TenantDTO tenantDTO = tenantClient.getByCode(tenant);
        Assert.isNotFound(tenantDTO, "租户不存在或已被删除！");
        QrcodePO po = new QrcodePO();
        po.setCode(generateCode(tenantDTO));
        po.setName(entityInfo.getEntityName());
        po.setEntityName(entityInfo.getEntityName());
        po.setEntityId(entityInfo.getEntityId());
        po.setEntityType(entityInfo.getEntityType());
        po.setStyleId(entityInfo.getStyleId());
        po.setConfigJson(createQRCode(entityInfo, po.getCode()));
        this.save(po);
        return po.getCode();
    }

    @Override
    public List<QrcodeApplicationVO> permissionAppFilter(List<QrcodeApplicationPO> qrcodeApplications) {
        List<QrcodeApplicationVO> finalApplication = ObjectUtils.copying(qrcodeApplications, QrcodeApplicationVO.class);
        if (ObjectUtils.isEmpty(finalApplication)) {
            finalApplication = new ArrayList<>();
        } else {
            List<ApplicationDTO> applicationsByUserId;
            try {
                SecurityContextHolder.getPrincipal().getId();
                applicationsByUserId = applicationClient.getApplicationsByUserId(SecurityContextHolder.getPrincipal().getId());
            } catch (Exception exception) {
                applicationsByUserId = applicationClient.getTenantApplications(SessionContextHolder.getRequester().getTenant());
            }
            if (ObjectUtils.isNotEmpty(applicationsByUserId)) {
                Map<String, ApplicationDTO> collect = applicationsByUserId.stream()
                        .collect(Collectors.toMap(ApplicationDTO::getCode, v -> v));
                finalApplication = finalApplication.stream().filter(v -> collect.containsKey(v.getApplicationCode()))
                        .collect(Collectors.toList());
                finalApplication.forEach(app -> app.setIcon(collect.get(app.getApplicationCode()).getIcon()));
            } else {
                finalApplication = new ArrayList<>();
            }
        }
        return finalApplication;
    }

    private PropertyQrCodeVO getQrCodeVO(QrcodePO po, String serialNumber) {
        PropertyQrCodeVO copying = ObjectUtils.copying(po, PropertyQrCodeVO.class);
        copying.setSerialNumber(serialNumber);
        return copying;
    }

    private TenantDTO getTenantIfNeedCreate(List<EntityInfoCommand> entityInfos, Map<Long, List<QrcodePO>> qrcodeMap) {
        boolean needCreate = entityInfos.stream()
                .map(EntityInfoCommand::getEntityId)
                .anyMatch(entityId -> CollectionUtils.isEmpty(qrcodeMap.get(entityId)));
        if (!needCreate) {
            return null;
        }
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        TenantDTO tenantDTO = tenantClient.getByCode(tenant);
        Assert.isNotFound(tenantDTO, "租户不存在或已被删除！");
        return tenantDTO;
    }

    private List<PropertyQrCodeVO> getQrCodesByIds(List<Long> qrcodeIds) {
        List<Long> distinctIds = qrcodeIds.stream()
                .filter(ObjectUtils::isNotNull)
                .distinct()
                .collect(Collectors.toList());
        Assert.parameterIsNull(distinctIds, "二维码id集合不能为空");
        List<QrcodePO> qrcodePOS = this.list(Wrappers.<QrcodePO>lambdaQuery().in(QrcodePO::getId, distinctIds));
        Assert.parameterIsNull(qrcodePOS, "未找到可下载的二维码");
        Map<Long, QrcodePO> qrcodeMap = qrcodePOS.stream().collect(Collectors.toMap(QrcodePO::getId, qrcode -> qrcode));
        Assert.isParameterError(qrcodeMap.size() != distinctIds.size(), "部分二维码不存在或已被删除");
        return distinctIds.stream()
                .map(qrcodeMap::get)
                .map(qrcode -> ObjectUtils.copying(qrcode, PropertyQrCodeVO.class))
                .collect(Collectors.toList());
    }

    private Map<String, String> buildQrCodeContentMap(List<PropertyQrCodeVO> qrCodes) {
        List<String> codes = qrCodes.stream()
                .map(PropertyQrCodeVO::getCode)
                .filter(ObjectUtils::isNotEmpty)
                .collect(Collectors.toList());
        Map<String, String> cloudQrCodeContentMap = qrcodeStyleInstHandler.batchGetCloudQrCodeContent(codes);
        return codes.stream()
                .distinct()
                .collect(Collectors.toMap(code -> code, code -> cloudQrCodeContentMap.getOrDefault(code, qrcodeStyleInstHandler.getQrCodeUrl(code))));
    }

    private void writeQrCodeZip(List<PropertyQrCodeVO> qrCodes, Map<String, String> qrCodeContentMap,
                                HttpServletResponse response
                               ) throws IOException {
        String fileName = URLEncoder.encode("二维码.zip", StandardCharsets.UTF_8).replace("+", "%20");
        response.setContentType("application/zip");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        Map<String, BufferedImage> imageCache = new HashMap<>();
        Map<String, AtomicInteger> fileNameCounter = new LinkedHashMap<>();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream())) {
            for (PropertyQrCodeVO qrCode : qrCodes) {
                byte[] imageBytes = qrcodeStyleInstHandler.renderQrcodeImage(
                        qrCode.getConfigJson(), qrCodeContentMap.get(qrCode.getCode()), imageCache);
                zipOutputStream.putNextEntry(new ZipEntry(buildImageFileName(qrCode, fileNameCounter)));
                zipOutputStream.write(imageBytes);
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
        }
    }

    private String buildImageFileName(PropertyQrCodeVO qrCode, Map<String, AtomicInteger> fileNameCounter) {
        List<String> names = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(qrCode.getSerialNumber())) {
            names.add(qrCode.getSerialNumber());
        }
        if (ObjectUtils.isNotEmpty(qrCode.getName())) {
            names.add(qrCode.getName());
        }
        if (names.isEmpty()) {
            names.add(qrCode.getCode());
        }
        String baseName = String.join("-", names).replaceAll("[\\\\/:*?\"<>|]", "_");
        AtomicInteger counter = fileNameCounter.computeIfAbsent(baseName, name -> new AtomicInteger());
        int index = counter.incrementAndGet();
        if (index > 1) {
            baseName = baseName + "-" + index;
        }
        return baseName + ".png";
    }

    /**
     * @Author wql
     * @Description 生成二维码信息
     * @Date 19:26 2023/11/20
     **/
    private String createQRCode(EntityInfoCommand entity, String code, QrcodeBatchGenerateCommand command) {

        QrcodeStylePO stylePO = command.getStylePO();
        String configJson = stylePO.getConfigJson();
        if (QrcodeEnum.PROPERTY.name().equals(command.getEntityType())) {
            //资产二维码不需要中间logo
            return qrcodeStyleInstHandler.createQRCode(entity, code, configJson);
        }
        return qrcodeStyleInstHandler.qrcodeStyleToInst(entity, code, configJson);
    }

    private String createQRCode(QrcodeEntityInfo entityInfo, String code) {
        QrcodeStylePO stylePO = styleMapper.selectById(entityInfo.getStyleId());
        Assert.isNotFound(stylePO, "样式不存在或已被删除！");
        String configJson = stylePO.getConfigJson();
        return qrcodeStyleInstHandler.createQRCode(EntityInfoCommand.builder()
                                                           .entityId(entityInfo.getEntityId())
                                                           .entityName(entityInfo.getEntityName())
                                                           .build(), code, configJson);
    }

    // 填充二维码样式中的值
    private String fillQrcodeStyle(QrcodeCommand command) {
        QrcodeStylePO stylePO = styleMapper.selectById(command.getStyleId());
        Assert.isNotFound(stylePO, "样式不存在或已被删除！");
        String configJson = stylePO.getConfigJson();
        EntityInfoCommand entityInfoCommand = EntityInfoCommand.builder()
                .entityId(command.getEntityId())
                .entityName(command.getEntityName())
                .build();
        return qrcodeStyleInstHandler.qrcodeStyleToInst(entityInfoCommand, command.getCode(), configJson);
    }

}
