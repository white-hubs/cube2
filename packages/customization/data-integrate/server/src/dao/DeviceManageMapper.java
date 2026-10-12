package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.DeviceManage;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：设备数据采集 Repository接口
 *
 * @author Appet
 * @date 2023-09-13 15:08:36
 */
@Repository("di-device-mapper")
public interface DeviceManageMapper extends BaseMapper<DeviceManage> {

    void insertOrUpdateBatch(List<DeviceManage> dataList);
}