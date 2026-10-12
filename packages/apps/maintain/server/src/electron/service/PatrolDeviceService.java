package com.guhecloud.cube.srv.gen.maintain.server.electron.service;

import com.guhecloud.cube.srv.gen.maintain.server.electron.param.PatrolDeviceParam;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.DeviceStatusStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.PatrolDeviceVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface PatrolDeviceService {
    /**
     * 设备状态统计
     *
     * @return
     */
    List<DeviceStatusStatVO> getDeviceStatusStat();

    Pagination<PatrolDeviceVO> getPage(PatrolDeviceParam param);
}
