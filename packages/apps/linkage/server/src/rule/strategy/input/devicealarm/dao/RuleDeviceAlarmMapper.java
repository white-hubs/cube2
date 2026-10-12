package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.entity.RuleDeviceAlarm;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.param.RuleDeviceAlarmQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicealarm.vo.RuleDeviceAlarmPageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RuleDeviceAlarmMapper extends BaseMapper<RuleDeviceAlarm> {

    Page<RuleDeviceAlarmPageVO> page(@Param("param") RuleDeviceAlarmQueryParam param, Page page);
}
