package com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeEntityTypeCreatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeEntityTypeDeletedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.cloudqr.event.QrcodeEntityTypeUpdatedEvent;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityStyleService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.QrcodeEntityTypeService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.commandgateway.command.QrcodeEntityTypeCommand;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.dao.QrcodeEntityTypeMapper;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.enumeration.QrcodeEnum;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.EntityStyleVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeEntityTypeVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.interfaces.vo.QrcodeStyleVO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityStylePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.po.QrcodeEntityTypePO;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.QrcodeStyleQueryService;
import com.guhecloud.cube.srv.gen.identity.server.qrcode.querygateway.query.QrcodeQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 实体类型
 * @date 2023/10/23 10:44
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class QrcodeEntityTypeServiceImpl extends ServiceImpl<QrcodeEntityTypeMapper, QrcodeEntityTypePO> implements QrcodeEntityTypeService {
    private final QrcodeEntityStyleService qrcodeEntityStyleService;
    private final QrcodeStyleQueryService qrcodeStyleQueryService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveType(QrcodeEntityTypeCommand typeCommand) {
        List<QrcodeEntityTypePO> list = this.lambdaQuery()
                .and(q -> q.eq(QrcodeEntityTypePO::getTypeName, typeCommand.getTypeName()).or()
                        .eq(QrcodeEntityTypePO::getEntityType, typeCommand.getTypeName()))
                .eq(typeCommand.getId() != null, QrcodeEntityTypePO::getId, typeCommand.getId()).list();
        Assert.isConflict(ObjectUtils.isNotEmpty(list), "实体类型已存在!");

        // 判断是新增还是更新
        boolean isNew = typeCommand.getId() == null;

        // 保存或更新
        QrcodeEntityTypePO po = ObjectUtils.copying(typeCommand, QrcodeEntityTypePO.class);
        this.saveOrUpdate(po);

        // 发布事件，由监听器异步处理云同步
        if (isNew) {
            applicationEventPublisher.publishEvent(
                    new QrcodeEntityTypeCreatedEvent(this, po.getEntityType(), po.getTypeName())
                                                  );
        } else {
            applicationEventPublisher.publishEvent(
                    new QrcodeEntityTypeUpdatedEvent(this, po.getId(), po.getEntityType(), po.getTypeName())
                                                  );
        }
    }

    @Override
    public List<QrcodeEntityTypeVO> typeList(String keyword) {
        List<QrcodeEntityTypeVO> vos = this.baseMapper.typeList(keyword);
        if (ObjectUtils.isNotEmpty(vos)) {
            vos.forEach(vo -> {
                vo.setAttributeVOS(QrcodeEnum.getObjAttributeList(vo.getEntityType()));
            });
        }
        return vos;
    }

    @Override
    public Pagination<EntityStyleVO> entityTypeStyle(QrcodeQueryParam param) {
        Assert.parameterIsNull(param.getEntityType(), "实体类型不能为空!");
        Pagination<QrcodeStyleVO> page = qrcodeStyleQueryService.getPage(param);
        List<EntityStyleVO> styleVOs = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(page.getRecords())) {
            styleVOs = ObjectUtils.copying(page.getRecords(), EntityStyleVO.class);
            List<QrcodeEntityStylePO> list = qrcodeEntityStyleService.lambdaQuery()
                    .eq(QrcodeEntityStylePO::getEntityType, param.getEntityType()).list();
            Set<Long> styles = new HashSet<>();
            if (ObjectUtils.isNotEmpty(list)) {
                styles = list.stream().map(QrcodeEntityStylePO::getStyleId).collect(Collectors.toSet());
            }
            Set<Long> finalStyles = styles;
            styleVOs.forEach(record -> {
                record.setOptFor(finalStyles.contains(record.getId()));
            });
        }
        return PagingUtils.convert(page, styleVOs);
    }

    /**
     * 重写删除方法，同步删除云二维码类型
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeById(Long id) {
        QrcodeEntityTypePO po = this.lambdaQuery().eq(QrcodeEntityTypePO::getId, id).one();
        Assert.isConflict(ObjectUtils.isEmpty(po), "实体类型不存在!");

        boolean result = super.removeById(id);
        if (result) {
            // 发布删除事件，由监听器异步处理云同步
            applicationEventPublisher.publishEvent(
                    new QrcodeEntityTypeDeletedEvent(this, po.getEntityType())
                                                  );
        }
        return result;
    }
}
