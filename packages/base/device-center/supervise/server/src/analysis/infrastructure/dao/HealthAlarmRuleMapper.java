package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.HealthAlarmRulePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.DeviceInstanceHealthAlarmRulePreviewVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/8/5 11:12
 * @description
 */
@Mapper
public interface HealthAlarmRuleMapper extends BaseMapper<HealthAlarmRulePO> {

    List<DeviceInstanceHealthAlarmRulePreviewVO> selectDeviceHealthAlarmRuleList(
            @Param("deviceInstanceIds") Set<Long> deviceInstanceIds
                                                                                );
}
