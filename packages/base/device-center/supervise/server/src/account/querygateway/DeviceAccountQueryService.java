package com.guhecloud.cube.srv.gen.device.supervise.server.account.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.device.supervise.server.account.po.DeviceAccountPO;

import java.util.List;

public interface DeviceAccountQueryService extends IService<DeviceAccountPO> {

    //    DeviceAccountVO getVOById(Long id);

    //    Pagination<DeviceAccountPageVO> getPage(DeviceAccountQuery pageQuery);

    //    List<DeviceAccountPageVO> getList(DeviceAccountQuery query);

    List<Long> getDeviceIds();

}
