package com.guhecloud.cube.biz.dispatch.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchDeviceGroupRefer;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：调度组与设备关联 Repository接口
 *
 * @author Appet
 * @date 2022-08-09 10:08:36
 */
@Repository
public interface DispatchDeviceGroupReferMapper extends BaseMapper<DispatchDeviceGroupRefer> {

    void insertOrUpdateDeviceGroupReferBatch(List<DispatchDeviceGroupRefer> datas);

}