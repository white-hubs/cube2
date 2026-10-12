package com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.repository.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po.TrafficDetectionAreaPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface TrafficDetectionAreaMapper extends BaseMapper<TrafficDetectionAreaPO> {

    List<TrafficDetectionAreaPO> selectTrafficDetectionAreasByDeviceIds(@Param("deviceIds") Collection<Long> deviceIds);

}
