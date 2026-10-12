package com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.EntityInfoCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeStyleMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.enumeration.QrcodeEnum;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.OldQrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeStylePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.handler.QrcodeStyleInstHandler;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrcodeQueryParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author bq
 * @date 2022/7/22
 * @description
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeQueryService extends ServiceImpl<QrcodeMapper, QrcodePO> {

    private final QrcodeStyleMapper styleMapper;
    private final QrcodeStyleInstHandler qrcodeStyleInstHandler;
    private final QrcodeMapper qrcodeMapper;

    public Pagination<QrcodeVO> getPage(QrcodeQueryParam param) {
        Page<QrcodePO> page = this.lambdaQuery()
                .select(QrcodePO::getId, QrcodePO::getEntityId, QrcodePO::getEntityName, QrcodePO::getEntityType, QrcodePO::getName, QrcodePO::getCode, QrcodePO::getStyleId, QrcodePO::getCreateTime)
                .eq(StringUtils.isNotEmpty(param.getEntityType()), QrcodePO::getEntityType, param.getEntityType())
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(QrcodePO::getEntityName, param.getKeyword())
                        .or()
                        .like(QrcodePO::getName, param.getKeyword())
                    ).orderByDesc(QrcodePO::getCreateTime)
                .page(PagingUtils.convert(param));
        if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
            return new Pagination<>();
        }
        return PagingUtils.convert(page, QrcodeVO.class);
    }

    public String preview(QrcodeCommand param) {
        String json = "";
        String code = param.getCode();
        Long styleId = param.getStyleId();
        // 如果存在ID则为查询数据库中的数据预览
        // 如果没有ID则将传进来的数据预览
        if (null != param.getId()) {
            QrcodePO qrcodePO = this.lambdaQuery().eq(QrcodePO::getId, param.getId()).one();
            if (qrcodePO != null) {
                code = qrcodePO.getCode();
                styleId = qrcodePO.getStyleId();
            }
        }
        QrcodeStylePO po = styleMapper.selectById(styleId);
        json = (po == null ? "" : po.getConfigJson());
        Assert.isParameterError(StringUtils.isEmpty(json) || StringUtils.isEmpty(code), "数据不全，预览失败！");
        EntityInfoCommand entityInfoCommand = EntityInfoCommand.builder().entityId(param.getEntityId())
                .entityName(param.getEntityName())
                .build();
        String styleToInst = qrcodeStyleInstHandler.qrcodeStyleToInst(entityInfoCommand, code, json);
        return qrcodeStyleInstHandler.fillQrcodeBase64ByUrl(styleToInst, qrcodeStyleInstHandler.getQrCodeUrl(code));
    }

    /**
     * 历史二维码数据清洗迁移
     *
     * @param type 对象类型
     *
     * @return 处理结果
     */
    public String oldQrcodeTransfer(String type, Integer start) {
        int result = 0;
        if (QrcodeEnum.PROPERTY.name().equals(type)) {
            log.info("开始处理资产二维码迁移...");
            result = propertyOldQrcodeTransfer(start);
        } else if (QrcodeEnum.SITE_POINT.name().equals(type)) {
            log.info("开始处理现场点二维码迁移...");
            result = sitePointOldQrcodeTransfer(start);
        }
        log.info("{}二维码处理完成,共计处理:{}条数据!", type, result);
        return type + "二维码处理完成,共计处理:" + result + "条数据!";
    }

    /**
     * 现场点历史二维码迁移
     */
    private int sitePointOldQrcodeTransfer(Integer start) {
        final Integer tenant = SessionContextHolder.getRequester().getTenant();
        int total = qrcodeMapper.countOldQrcodeNum("point");
        int result = 0;
        for (int i = start; i < total; i += 1000) {
            log.info("total:{}现场点开始处理:{},---{}", total, i, i + 1000);
            List<OldQrcodePO> oldCodes = qrcodeMapper.getOldQrcodeList("point", i);
            if (ObjectUtils.isNotEmpty(oldCodes)) {
                result += saveSitePointCode(tenant, oldCodes);
            }
        }
        return result;
    }

    private int saveSitePointCode(Integer tenant, List<OldQrcodePO> oldCodes) {
        CopyOnWriteArrayList<QrcodePO> qrcodePOS = Lists.newCopyOnWriteArrayList();
        oldCodes.parallelStream().forEach(code -> {
            SessionContextHolder.requestTenant(tenant);
            QrcodeCommand command = QrcodeCommand.builder()
                    .code(code.getQrcodeHiddenCode())
                    .name(code.getQrcodeName())
                    .entityIdStr(code.getBindObjectId())
                    .entityId(Long.parseLong(code.getBindObjectId()))
                    .entityName(code.getBindObjectName())
                    .entityType(QrcodeEnum.SITE_POINT.name())
                    .styleIdStr("1726505877848473667")
                    .styleId(1726505877848473667L)
                    .build();
            qrcodePOS.add(QrcodePO.builder().code(code.getQrcodeHiddenCode())
                                  .name(code.getQrcodeName())
                                  .entityType(QrcodeEnum.SITE_POINT.name())
                                  .entityId(Long.parseLong(code.getBindObjectId()))
                                  .entityName(code.getBindObjectName())
                                  .styleId(1726505877848473667L)
                                  .configJson(preview(command))
                                  .build());
        });
        if (ObjectUtils.isNotEmpty(qrcodePOS)) {
            this.saveBatch(qrcodePOS);
            log.info("现场点二维码数据已处理:{}条", qrcodePOS.size());
        }
        return qrcodePOS.size();
    }

    /**
     * 资产历史二维码迁移
     */
    public int propertyOldQrcodeTransfer(Integer start) {
        final Integer tenant = SessionContextHolder.getRequester().getTenant();
        int total = qrcodeMapper.countOldQrcodeNum("asset");
        int result = 0;
        for (int i = start; i < total; i += 1000) {
            log.info("total:{}资产开始处理:{},---{}", total, i, i + 1000);
            List<OldQrcodePO> oldCodes = qrcodeMapper.getOldQrcodeList("asset", i);
            if (ObjectUtils.isNotEmpty(oldCodes)) {
                result += savePropertyCode(tenant, oldCodes);
            }
        }
        return result;
    }

    public int savePropertyCode(Integer tenant, List<OldQrcodePO> oldCodes) {
        List<QrcodePO> qrcodePOS = new ArrayList<>();
        oldCodes.parallelStream().forEach(code -> {
            SessionContextHolder.requestTenant(tenant);
            QrcodeCommand command = QrcodeCommand.builder()
                    .code(code.getQrcodeCode())
                    .name(code.getQrcodeName())
                    .entityIdStr(code.getBindObjectId())
                    .entityId(Long.parseLong(code.getBindObjectId()))
                    .entityName(code.getBindObjectName())
                    .entityType(QrcodeEnum.PROPERTY.name())
                    .styleIdStr("1727209257265602562")
                    .styleId(1727209257265602562L)
                    .build();
            qrcodePOS.add(QrcodePO.builder().code(code.getQrcodeCode())
                                  .name(code.getQrcodeName())
                                  .entityType(QrcodeEnum.PROPERTY.name())
                                  .entityId(Long.parseLong(code.getBindObjectId()))
                                  .entityName(code.getBindObjectName())
                                  .styleId(1727209257265602562L)
                                  .configJson(preview(command))
                                  .build());
        });
        if (ObjectUtils.isNotEmpty(qrcodePOS)) {
            this.saveBatch(qrcodePOS);
            log.info("资产二维码数据已处理:{}条", qrcodePOS.size());
        }
        return qrcodePOS.size();
    }

    public Integer getTenantByQrCode(String code) {
        Integer tenant;
        try {
            tenant = qrcodeMapper.selectTenantByQrCode(code);
            if (ObjectUtils.isEmpty(tenant)) {
                throw new ServiceException("", "二维码不存在");
            }
        } catch (Exception exception) {
            throw new ServiceException("", "二维码不存在");
        }
        return tenant;
    }
}
