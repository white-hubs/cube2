package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthValuePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthLevelVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author pengjian
 * @date 2022/8/1 15:38
 * @description
 */
@Mapper
public interface DeviceHealthValueMapper extends BaseMapper<DeviceHealthValuePO> {

    List<DeviceHealthValuePO> selectLast(@Param("deviceInstanceIds") Set<Long> deviceInstanceIds);

    Page<DeviceHealthValuePO> selectLastPage(Page page);

    HealthLevelVO selectHealthLevelNumber(@Param("deviceIds") Set<Long> deviceIds);
}
