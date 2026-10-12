package com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.DeviceBusinessUrlCommandService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.commandgateway.command.DeviceBusinessUrlModifyParam;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.dao.DeviceBusinessUrlMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceBusinessUrlPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceBusinessUrCommandServiceImpl extends ServiceImpl<DeviceBusinessUrlMapper, DeviceBusinessUrlPO> implements DeviceBusinessUrlCommandService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull DeviceBusinessUrlModifyParam param) {
        DeviceBusinessUrlPO todoBusinessUrl = ObjectUtils.copying(param, DeviceBusinessUrlPO.class);
        this.baseMapper.insert(todoBusinessUrl);
        return todoBusinessUrl.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull DeviceBusinessUrlModifyParam param) {
        DeviceBusinessUrlPO todoBusinessUrl = this.getById(id);
        if (Objects.nonNull(todoBusinessUrl)) {
            ObjectUtils.copying(param, todoBusinessUrl);
            this.baseMapper.updateById(todoBusinessUrl);
        }
    }
}
