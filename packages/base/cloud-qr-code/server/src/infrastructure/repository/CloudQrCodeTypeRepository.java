package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQrCodeTypePO;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.valueobject.QrCodeType;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.QrCodeTypeConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.mapper.CloudQrCodeTypeMapper;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class CloudQrCodeTypeRepository extends ServiceImpl<CloudQrCodeTypeMapper, CloudQrCodeTypePO> {

    /**
     * 保存或更新二维码类型
     *
     * @param qrCodeType 二维码类型实体
     *
     * @return 保存后的二维码类型实体
     */
    @Transactional(rollbackFor = Exception.class)
    public QrCodeType save(QrCodeType qrCodeType) {
        CloudQrCodeTypePO po = QrCodeTypeConverters.entityToPO(qrCodeType);
        po.setDeleted(false);
        this.saveOrUpdate(po);
        return QrCodeTypeConverters.poToEntity(po);
    }

    /**
     * 根据ID查询二维码类型
     *
     * @param id 二维码类型ID
     *
     * @return 二维码类型实体
     */
    public QrCodeType findById(Long id) {
        var po = this.baseMapper.selectById(id);
        if (po == null) {
            return null;
        }
        return QrCodeTypeConverters.poToEntity(po);
    }

    /**
     * 根据授权项目编号和类型编号查询
     */
    public QrCodeType findByGrantClientIdAndCode(String grantClientId, String code) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodeTypePO>lambdaQuery()
                                                      .eq(CloudQrCodeTypePO::getGrantClientId, grantClientId)
                                                      .eq(CloudQrCodeTypePO::getCode, code));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return QrCodeTypeConverters.poToEntity(list.getFirst());
    }

    /**
     * 根据授权项目编号和类型名称查询
     */
    public QrCodeType findByGrantClientIdAndName(String grantClientId, String name) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodeTypePO>lambdaQuery()
                                                      .eq(CloudQrCodeTypePO::getGrantClientId, grantClientId)
                                                      .eq(CloudQrCodeTypePO::getName, name));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return QrCodeTypeConverters.poToEntity(list.getFirst());
    }

    /**
     * 根据授权项目编号查询列表
     */
    public List<QrCodeType> findByGrantClientId(String grantClientId) {
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodeTypePO>lambdaQuery()
                                                      .eq(CloudQrCodeTypePO::getGrantClientId, grantClientId));
        return list.stream().map(QrCodeTypeConverters::poToEntity).toList();
    }

    /**
     * 分页查询（带grantClientCode过滤）
     */
    public Pagination<QrCodeType> pageQuery(Page<CloudQrCodeTypePO> page, String keyword, String grantClientId) {
        var pageResult = this.baseMapper.selectPage(page, Wrappers.<CloudQrCodeTypePO>lambdaQuery()
                .eq(StringUtils.isNotBlank(grantClientId), CloudQrCodeTypePO::getGrantClientId, grantClientId)
                .and(StringUtils.isNotBlank(keyword), x -> x
                        .like(CloudQrCodeTypePO::getCode, keyword)
                        .or()
                        .like(CloudQrCodeTypePO::getName, keyword))
                .orderByDesc(CloudQrCodeTypePO::getCreateTime));
        return PagingUtils.convert(pageResult, pageResult.getRecords().stream().map(QrCodeTypeConverters::poToEntity).toList());
    }

    /**
     * 根据ID删除二维码类型
     *
     * @param id 二维码类型ID
     *
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(Long id) {
        DatabaseContextHolder.softDelete(CloudQrCodeTypePO.class, Collections.singleton(id));
        return true;
    }

    /**
     * 根据二维码类型编号删除二维码类型
     *
     * @param code 二维码类型编号
     *
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCode(String code) {
        return deleteByCode(Collections.singleton(code));
    }

    /**
     * 根据二维码类型编号删除二维码类型
     *
     * @param codes 二维码类型编号集合
     *
     * @return 是否删除成功
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByCode(Collection<String> codes) {
        // 检查二维码是否存在
        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodeTypePO>lambdaQuery()
                                                      .in(CloudQrCodeTypePO::getCode, codes));
        if (CollectionUtils.isEmpty(list)) {
            return true;
        }
        DatabaseContextHolder.softDelete(CloudQrCodeTypePO.class, list.stream().map(CloudQrCodeTypePO::getId).collect(Collectors.toSet()));
        return true;
    }

    /**
     * 批量保存二维码类型（高性能）
     *
     * @param qrCodeTypes 二维码类型实体列表
     *
     * @return 保存后的二维码类型实体列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<QrCodeType> batchSave(List<QrCodeType> qrCodeTypes) {
        if (CollectionUtils.isEmpty(qrCodeTypes)) {
            return Collections.emptyList();
        }

        List<CloudQrCodeTypePO> poList = qrCodeTypes.stream()
                .map(QrCodeTypeConverters::entityToPO)
                .peek(po -> po.setDeleted(false))
                .toList();

        // 使用MyBatis-Plus批量插入，每批次500条
        this.saveBatch(poList, 500);

        return poList.stream().map(QrCodeTypeConverters::poToEntity).toList();
    }

    /**
     * 批量通过code查询二维码类型
     *
     * @param codes 二维码类型编号集合
     *
     * @return 二维码类型实体集合，key为code
     */
    public Map<String, QrCodeType> findByCodes(Collection<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyMap();
        }

        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodeTypePO>lambdaQuery()
                                                      .in(CloudQrCodeTypePO::getCode, codes));

        return list.stream()
                .map(QrCodeTypeConverters::poToEntity)
                .collect(Collectors.toMap(QrCodeType::getCode, qrCodeType -> qrCodeType, (existing, replacement) -> existing));
    }

    /**
     * 批量查询二维码类型（高性能）
     *
     * @param codes 二维码类型code集合
     *
     * @return 二维码类型实体列表
     */
    public List<QrCodeType> batchQuery(Set<String> codes) {
        if (CollectionUtils.isEmpty(codes)) {
            return Collections.emptyList();
        }

        var list = this.baseMapper.selectList(Wrappers.<CloudQrCodeTypePO>lambdaQuery()
                                                      .in(CloudQrCodeTypePO::getCode, codes));
        return list.stream()
                .map(QrCodeTypeConverters::poToEntity)
                .toList();
    }

    /**
     * 批量更新二维码类型（高性能）
     *
     * @param qrCodeTypes 二维码类型实体列表
     *
     * @return 更新后的二维码类型实体列表
     */
    @Transactional(rollbackFor = Exception.class)
    public List<QrCodeType> batchUpdate(List<QrCodeType> qrCodeTypes) {
        if (CollectionUtils.isEmpty(qrCodeTypes)) {
            return Collections.emptyList();
        }

        List<CloudQrCodeTypePO> poList = qrCodeTypes.stream()
                .map(QrCodeTypeConverters::entityToPO)
                .peek(po -> po.setDeleted(false))
                .toList();

        // 使用MyBatis-Plus批量更新，每批次500条
        this.updateBatchById(poList, 500);

        return poList.stream().map(QrCodeTypeConverters::poToEntity).toList();
    }
}
