package com.guhecloud.cube.srv.gen.adapter.access.control.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.entity.DoorDevice;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.entity.EventRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("adapter-door-device-mapper")
public interface DoorDeviceMapper extends BaseMapper<DoorDevice> {

}
