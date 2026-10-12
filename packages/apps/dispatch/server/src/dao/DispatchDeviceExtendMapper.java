package com.guhecloud.cube.biz.dispatch.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceExtend;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：调度设备-扩展 Repository接口
 *
 * @author Appet
 * @date 2022-07-26 10:08:36
 */
@Repository
public interface DispatchDeviceExtendMapper extends BaseMapper<DispatchDeviceExtend> {

    void insertOrUpdateDeviceExtendBatch(List<DispatchDeviceExtend> dataList);
}