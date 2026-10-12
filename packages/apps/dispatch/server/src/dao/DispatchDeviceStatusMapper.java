package com.guhecloud.cube.biz.dispatch.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：调度设备-状态 Repository接口
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Repository
public interface DispatchDeviceStatusMapper extends BaseMapper<DispatchDeviceStatus> {

    void insertOrUpdateDeviceStatusBatch(List<DispatchDeviceStatus> dataList);

    void updateDeviceStatusBatch(@Param("status") Boolean online, @Param("ids") List<String> ids);
}