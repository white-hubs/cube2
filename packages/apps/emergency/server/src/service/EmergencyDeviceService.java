package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyDevice;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDevice2QueryParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyDeviceParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyDeviceVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急设备管理服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
public interface EmergencyDeviceService extends IService<EmergencyDevice> {

    Long add(EmergencyDeviceParam param);

    void update(Long id, EmergencyDeviceParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencyDeviceVO getById(Long id);

    Pagination<EmergencyDeviceVO> getPage(EmergencyDevice2QueryParam param);

    EmergencyDeviceStatVO getEmergencyDeviceStatForUsed();
}