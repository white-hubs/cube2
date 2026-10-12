package com.guhecloud.cube.biz.emergency.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFacility;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFacilityStatVO;
import org.springframework.stereotype.Repository;

/**
 * 描述：应急设施管理 Repository接口
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Repository
public interface EmergencyFacilityMapper extends BaseMapper<EmergencyFacility> {

    EmergencyFacilityStatVO getEmergencyFacilityStatForUsed();
}