package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.attendance.scheduling.domain.po.DingTalkSchedulePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DingTalkScheduleMapper extends BaseMapper<DingTalkSchedulePO> {
}
