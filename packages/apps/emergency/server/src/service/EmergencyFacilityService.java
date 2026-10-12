package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFacility;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFacilityParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFacilityQueryParam;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFacilityStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFacilityVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：应急设施管理服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
public interface EmergencyFacilityService extends IService<EmergencyFacility> {

    Long add(EmergencyFacilityParam param);

    void update(Long id, EmergencyFacilityParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    EmergencyFacilityVO getById(Long id);

    Pagination<EmergencyFacilityVO> getPage(EmergencyFacilityQueryParam param);

    EmergencyFacilityStatVO getEmergencyFacilityStatForUsed();
}