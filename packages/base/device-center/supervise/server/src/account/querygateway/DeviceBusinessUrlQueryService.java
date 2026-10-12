package com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceBusinessUrlPO;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface DeviceBusinessUrlQueryService extends IService<DeviceBusinessUrlPO> {

    DeviceBusinessUrlPO getById(Serializable id);

    List<DeviceBusinessUrlPO> listByBusinessTypes(Collection<String> businessTypes, String terminal);
}
