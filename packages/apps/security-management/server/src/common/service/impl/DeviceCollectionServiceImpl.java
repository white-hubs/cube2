package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.DeviceCollectionMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.DeviceCollection;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceCollectionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.DeviceCollectionService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceCollectionServiceImpl extends ServiceImpl<DeviceCollectionMapper, DeviceCollection> implements DeviceCollectionService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDeviceCollection(DeviceCollectionParam param) {
        Long id = SecurityContextHolder.getPrincipal().getId();

        DeviceCollection one = this.getOne(Wrappers.<DeviceCollection>lambdaQuery()
                                                   .eq(DeviceCollection::getDeviceId, param.getDeviceId())
                                                   .eq(DeviceCollection::getApplicationCode, param.getApplicationCode())
                                                   .eq(DeviceCollection::getUserId, id));
        if (Objects.nonNull(one)) {
            DatabaseContextHolder.softDelete(this, one.getId());
        } else {
            DeviceCollection copying = ObjectUtils.copying(param, DeviceCollection.class);
            copying.setUserId(id);
            this.save(copying);
        }

    }
}
