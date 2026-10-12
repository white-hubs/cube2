package com.guhecloud.cube.base.device.center.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.device.center.core.model.ExtDeviceAttrCmd;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceInstanceExecuteCommandVO;
import com.guhecloud.cube.base.device.center.metadata.lang.Pair;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceInstanceExecuteCommandQuery;
import com.guhecloud.cube.base.device.center.metadata.vo.ChangeLogByBatchIdVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface ExtDeviceAttrCmdMapper extends BaseMapper<ExtDeviceAttrCmd> {
    Page<DeviceInstanceExecuteCommandVO> page(Page page, @Param("query") DeviceInstanceExecuteCommandQuery query);

    List<ChangeLogByBatchIdVO> listByBatchId(@Param("batchId") Long batchId);

    List<Pair<String, String>> devicesPwdNeed(@Param("deviceIds") Set<Long> deviceIds);
}