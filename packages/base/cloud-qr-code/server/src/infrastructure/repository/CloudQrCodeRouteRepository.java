package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.QrRoute;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQrRoutePO;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.QrRouteConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.mapper.CloudQrRouteMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CloudQrCodeRouteRepository extends ServiceImpl<CloudQrRouteMapper, CloudQrRoutePO> {

    /**
     * 保存或更新二维码路由
     *
     * @param qrRoute 二维码路由实体
     *
     * @return 保存后的二维码路由实体
     */
    @Transactional(rollbackFor = Exception.class)
    public QrRoute save(QrRoute qrRoute, Long qrCodeId, String shortCode) {
        var po = this.baseMapper.selectOne(Wrappers.<CloudQrRoutePO>lambdaQuery()
                                                   .eq(CloudQrRoutePO::getQrCodeId, qrCodeId)
                                                   .eq(CloudQrRoutePO::getShortCode, shortCode));
        if (Objects.nonNull(po)) {
            //修改
            po.setTargetUrl(qrRoute.getTargetUrl());
            po.setTargetParams(qrRoute.getTargetParams());
            this.baseMapper.updateById(po);
        } else {
            //新增
            po = QrRouteConverters.entityToPO(qrRoute, qrCodeId, shortCode);
            this.baseMapper.insert(po);
        }
        return qrRoute;
    }

    /**
     * 根据ID查询二维码路由
     *
     * @param id 二维码路由ID
     *
     * @return 二维码路由实体
     */
    public QrRoute findById(Long id) {
        var po = this.baseMapper.selectById(id);
        if (po == null) {
            return null;
        }
        return QrRouteConverters.poToEntity(po);
    }

    /**
     * 根据编号查询二维码路由
     */
    public QrRoute findByShortCode(String qrShortCode) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrRoutePO>lambdaQuery()
                                                      .eq(CloudQrRoutePO::getShortCode, qrShortCode));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return QrRouteConverters.poToEntity(list.getFirst());
    }

    /**
     * 根据二维码ID查询二维码路由
     */
    public QrRoute findByQrCodeId(Long qrCodeId) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrRoutePO>lambdaQuery()
                                                      .eq(CloudQrRoutePO::getQrCodeId, qrCodeId));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return QrRouteConverters.poToEntity(list.getFirst());
    }

    /**
     * 根据ID删除二维码
     *
     * @param qrCodeId 二维码ID
     *
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByQrCodeId(Long qrCodeId) {
        // 检查二维码路由是否存在
        var list = this.baseMapper.selectList(Wrappers.<CloudQrRoutePO>lambdaQuery()
                                                      .eq(CloudQrRoutePO::getQrCodeId, qrCodeId));
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        DatabaseContextHolder.softDelete(CloudQrRoutePO.class, list.stream().map(CloudQrRoutePO::getId).collect(Collectors.toSet()));
        return true;
    }
}
