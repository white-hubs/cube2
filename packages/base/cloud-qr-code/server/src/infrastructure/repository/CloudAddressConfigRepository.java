package com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.entity.AddressConfig;
import com.guhecloud.cube.base.cloud.qr.code.server.domain.po.CloudAddressConfigPO;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.converters.AddressConfigConverters;
import com.guhecloud.cube.base.cloud.qr.code.server.infrastructure.repository.mapper.CloudAddressConfigMapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 云地址配置Repository
 * <p>
 * 注意：此表只有一条数据，保存时会自动更新已存在的记录
 */
@Repository
public class CloudAddressConfigRepository extends ServiceImpl<CloudAddressConfigMapper, CloudAddressConfigPO> {

    /**
     * 保存或更新云地址配置
     * <p>
     * 由于表中只有一条数据，此方法会查询现有记录并更新，如果不存在则创建新记录
     *
     * @param addressConfig 云地址配置实体
     *
     * @return 保存后的云地址配置实体
     */
    @Transactional(rollbackFor = Exception.class)
    public AddressConfig save(AddressConfig addressConfig) {
        // 查询现有记录
        var existingPO = findExistingRecord();

        // 如果存在记录，使用现有ID
        if (existingPO != null) {
            addressConfig.setId(existingPO.getId());
        }

        CloudAddressConfigPO po = AddressConfigConverters.entityToPO(addressConfig);
        this.saveOrUpdate(po);
        return AddressConfigConverters.poToEntity(po);
    }

    /**
     * 查询云地址配置
     * <p>
     * 返回表中唯一的配置记录
     *
     * @return 云地址配置实体，如果不存在则返回null
     */
    public AddressConfig find() {
        var po = findExistingRecord();
        if (po == null) {
            return null;
        }
        return AddressConfigConverters.poToEntity(po);
    }

    /**
     * 查询现有记录
     *
     * @return 现有的PO对象，如果不存在则返回null
     */
    private CloudAddressConfigPO findExistingRecord() {
        var list = this.baseMapper.selectList(Wrappers.<CloudAddressConfigPO>lambdaQuery().last("LIMIT 1"));
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        return list.getFirst();
    }
}
