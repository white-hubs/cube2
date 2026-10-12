package com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.dao.DeviceBusinessUrlMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceBusinessUrlPO;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway.DeviceBusinessUrlQueryService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceBusinessUrlQueryServiceImpl extends ServiceImpl<DeviceBusinessUrlMapper, DeviceBusinessUrlPO> implements DeviceBusinessUrlQueryService {

    @Override
    public DeviceBusinessUrlPO getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<DeviceBusinessUrlPO> listByBusinessTypes(@NonNull Collection<String> businessTypes, @NonNull String terminal) {
        return CollectionUtils.isEmpty(businessTypes) ? Collections.emptyList() : this.lambdaQuery()
                .in(DeviceBusinessUrlPO::getBusinessType, businessTypes)
                .eq(DeviceBusinessUrlPO::getTerminal, terminal)
                .list();
    }
}
