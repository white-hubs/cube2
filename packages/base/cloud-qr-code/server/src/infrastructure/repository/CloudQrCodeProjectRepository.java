package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.CloudQrCodeProject;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudQRCodeProjectPO;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.CloudQrCodeProjectConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.dao.CloudQrCodeProjectMapper;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 云二维码项目仓储
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CloudQrCodeProjectRepository {

    private final CloudQrCodeProjectMapper mapper;

    public void save(CloudQrCodeProject entity) {
        CloudQRCodeProjectPO po = CloudQrCodeProjectConverters.entityToPO(entity);
        mapper.insert(po);
        entity.setId(po.getId());
    }

    public void update(CloudQrCodeProject entity) {
        CloudQRCodeProjectPO po = CloudQrCodeProjectConverters.entityToPO(entity);
        mapper.updateById(po);
    }

    public void deleteById(Long id) {
        mapper.deleteById(id);
    }

    public CloudQrCodeProject findById(Long id) {
        CloudQRCodeProjectPO po = mapper.selectById(id);
        return CloudQrCodeProjectConverters.poToEntity(po);
    }

    public CloudQrCodeProject findByCode(String code) {
        CloudQRCodeProjectPO po = mapper.selectOne(
                new LambdaQueryWrapper<CloudQRCodeProjectPO>()
                        .eq(CloudQRCodeProjectPO::getCode, code)
                                                  );
        return CloudQrCodeProjectConverters.poToEntity(po);
    }

    public Pagination<CloudQrCodeProject> pageQuery(Page<CloudQRCodeProjectPO> page, String keyword) {
        var poPage = mapper.selectPage(
                page,
                Wrappers.<CloudQRCodeProjectPO>lambdaQuery()
                        .like(StringUtils.isNotBlank(keyword), CloudQRCodeProjectPO::getName, keyword)
                        .orderByDesc(CloudQRCodeProjectPO::getCreateTime));
        return PagingUtils.convert(poPage, poPage.getRecords().stream().map(CloudQrCodeProjectConverters::poToEntity).toList());
    }

    public List<CloudQrCodeProject> listQuery(String keyword) {
        var list = mapper.selectList(
                Wrappers.<CloudQRCodeProjectPO>lambdaQuery()
                        .like(StringUtils.isNotBlank(keyword), CloudQRCodeProjectPO::getName, keyword)
                        .orderByDesc(CloudQRCodeProjectPO::getCreateTime));
        return list.stream().map(CloudQrCodeProjectConverters::poToEntity).toList();
    }
}
