package com.guhecloud.cube.srv.gen.safety.management.notify.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.safety.management.notify.po.AlarmNoticePO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AlarmNoticeMapper extends BaseMapper<AlarmNoticePO> {

    boolean insertOrUpdate(AlarmNoticePO alarmNoticePO);
}
