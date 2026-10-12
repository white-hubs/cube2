package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.QrCode;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQrCodePO;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQrRoutePO;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.QrCodeConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.QrRouteConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.mapper.CloudQrCodeMapper;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class CloudQrCodeRepository extends ServiceImpl<CloudQrCodeMapper, CloudQrCodePO> {

    private final CloudQrCodeRouteRepository cloudQrCodeRouteRepository;

    public CloudQrCodeRepository(CloudQrCodeRouteRepository cloudQrCodeRouteRepository) {
        this.cloudQrCodeRouteRepository = cloudQrCodeRouteRepository;
    }

    /**
     * 保存或更新二维码
     *
     * @param qrCode 二维码实体
     *
     * @return 保存后的二维码实体
     */
    @Transactional(rollbackFor = Exception.class)
    public QrCode save(QrCode qrCode) {
        var po = QrCodeConverters.entityToPO(qrCode);
        po.setDeleted(false);
        this.saveOrUpdate(po);
        if (Objects.nonNull(qrCode.getQrRoute())) {
            cloudQrCodeRouteRepository.save(qrCode.getQrRoute(), po.getId(), qrCode.getShortCode());
        }
        return QrCodeConverters.poToEntity(po);
    }

    /**
     * 根据ID查询二维码
     *
     * @param id 二维码ID
     *
     * @return 二维码实体
     */
    public QrCode findById(Long id) {
        var po = this.baseMapper.selectById(id);
        if (po == null) {
            return null;
        }
        return QrCodeConverters.poToEntity(po);
    }

    /**
     * 根据短码查询二维码
     *
     * @param shortCode
     *
     * @return 二维码实体
     */
    public QrCode findByShortCode(String shortCode) {
        var qrCodes = this.baseMapper.selectList(Wrappers.<CloudQrCodePO>lambdaQuery()
                                                         .eq(CloudQrCodePO::getShortCode, shortCode));
        if (CollectionUtils.isEmpty(qrCodes)) {
            return null;
        }
        return QrCodeConverters.poToEntity(qrCodes.getFirst());
    }

    /**
     * 根据授权项目编号和编号查询
     */
    public QrCode findByGrantClientIdAndCode(String grantClientId, String code) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodePO>lambdaQuery()
                                                      .eq(CloudQrCodePO::getGrantClientId, grantClientId)
                                                      .eq(CloudQrCodePO::getCode, code));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return QrCodeConverters.poToEntity(list.getFirst());
    }

    /**
     * 根据授权项目编号和编号查询
     *
     * @return
     */
    public List<QrCode> findByGrantClientIdAndCodes(String grantClientId, Collection<String> codes) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodePO>lambdaQuery()
                                                      .eq(CloudQrCodePO::getGrantClientId, grantClientId)
                                                      .in(CloudQrCodePO::getCode, codes));
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(QrCodeConverters::poToEntity).toList();
    }

    /**
     * 根据授权项目编号和名称查询
     */
    public QrCode findByGrantClientIdAndName(String grantClientId, String name) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodePO>lambdaQuery()
                                                      .eq(CloudQrCodePO::getGrantClientId, grantClientId)
                                                      .eq(CloudQrCodePO::getName, name));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return QrCodeConverters.poToEntity(list.getFirst());
    }

    /**
     * 根据授权项目编号查询列表
     */
    public List<QrCode> findByGrantClientCode(String grantClientId) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodePO>lambdaQuery()
                                                      .eq(CloudQrCodePO::getGrantClientId, grantClientId));
        return list.stream().map(QrCodeConverters::poToEntity).toList();
    }

    /**
     * 分页查询（带grantClientCode过滤）
     */
    public Pagination<QrCode> pageQuery(Page<CloudQrCodePO> page, String keyword, String grantClientId) {
        var pageResult = this.baseMapper.selectPage(page, Wrappers.<CloudQrCodePO>lambdaQuery()
                .eq(StringUtils.isNotBlank(grantClientId), CloudQrCodePO::getGrantClientId, grantClientId)
                .and(StringUtils.isNotBlank(keyword), x -> x
                        .like(CloudQrCodePO::getCode, keyword)
                        .or()
                        .like(CloudQrCodePO::getName, keyword))
                .orderByDesc(CloudQrCodePO::getCreateTime));
        return PagingUtils.convert(pageResult, pageResult.getRecords().stream().map(QrCodeConverters::poToEntity).toList());
    }

    /**
     * 根据ID删除二维码
     *
     * @param id 二维码ID
     *
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        DatabaseContextHolder.softDelete(CloudQrCodePO.class, Collections.singleton(id));
        return true;
    }

    /**
     * 根据二维码编号删除二维码
     *
     * @param code 二维码编号
     *
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCode(String code) {
        return deleteByCode(Collections.singleton(code));
    }

    /**
     * 根据二维码编号删除二维码
     *
     * @param codes 二维码编号集合
     *
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCode(Collection<String> codes) {
        // 检查二维码是否存在
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodePO>lambdaQuery()
                                                      .in(CloudQrCodePO::getCode, codes)
                                             );
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        DatabaseContextHolder.softDelete(CloudQrCodePO.class, list.stream().map(CloudQrCodePO::getId).collect(Collectors.toSet()));
        return true;
    }

    /**
     * 批量保存二维码（高性能）
     *
     * @param qrCodes 二维码实体列表
     *
     * @return 保存后的二维码实体列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<QrCode> batchSave(List<QrCode> qrCodes) {
        if (CollectionUtils.isEmpty(qrCodes)) {
            return Collections.emptyList();
        }

        var poList = qrCodes.stream()
                .map(QrCodeConverters::entityToPO)
                .peek(po -> po.setDeleted(false))
                .toList();
        // 使用MyBatis-Plus批量插入，每批次500条
        this.saveBatch(poList, 500);
        var routeList = new ArrayList<CloudQrRoutePO>();
        // 批量保存路由信息
        for (int i = 0; i < qrCodes.size(); i++) {
            var qrCode = qrCodes.get(i);
            var po = poList.get(i);
            if (Objects.nonNull(qrCode.getQrRoute())) {
                routeList.add(QrRouteConverters.entityToPO(qrCode.getQrRoute(), po.getId(), qrCode.getShortCode()));
            }
        }
        if (CollectionUtils.isNotEmpty(routeList)) {
            cloudQrCodeRouteRepository.saveBatch(routeList, 500);
        }
        return poList.stream().map(QrCodeConverters::poToEntity).toList();
    }

    /**
     * 批量通过code查询二维码
     *
     * @param codes 二维码编号集合
     *
     * @return 二维码实体集合，key为code
     */
    public Map<String, QrCode> findByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyMap();
        }

        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodePO>lambdaQuery()
                                                      .in(CloudQrCodePO::getCode, codes));

        return list.stream()
                .map(QrCodeConverters::poToEntity)
                .collect(Collectors.toMap(QrCode::getCode, qrCode -> qrCode, (existing, replacement) -> existing));
    }

    /**
     * 批量更新二维码（高性能）
     *
     * @param qrCodes 二维码实体列表
     *
     * @return 更新后的二维码实体列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<QrCode> batchUpdate(List<QrCode> qrCodes) {
        if (CollectionUtils.isEmpty(qrCodes)) {
            return Collections.emptyList();
        }

        var poList = qrCodes.stream()
                .map(QrCodeConverters::entityToPO)
                .peek(po -> po.setDeleted(false))
                .toList();

        // 使用MyBatis-Plus批量更新，每批次500条
        this.updateBatchById(poList, 500);

        // 批量更新路由信息
        for (int i = 0; i < qrCodes.size(); i++) {
            var qrCode = qrCodes.get(i);
            var po = poList.get(i);
            if (Objects.nonNull(qrCode.getQrRoute())) {
                cloudQrCodeRouteRepository.save(qrCode.getQrRoute(), po.getId(), qrCode.getShortCode());
            }
        }

        return poList.stream().map(QrCodeConverters::poToEntity).toList();
    }
}
