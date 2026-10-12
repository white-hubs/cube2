package com.guhecloud.cube.srv.gen.maintain.server.electron.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.electron.entity.PatrolDevice;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.DeviceStatusStatVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PatrolDeviceMapper extends BaseMapper<PatrolDevice> {

    @Select("select status as code,status_name as name,count(id) as num from patrol_device d group by status")
    List<DeviceStatusStatVO> getDeviceStatusStat();
}
