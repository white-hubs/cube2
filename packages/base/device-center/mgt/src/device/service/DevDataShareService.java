package com.guhecloud.cube.iot.mgt.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.device.model.DevDataShare;
import com.guhecloud.cube.iot.mgt.device.param.DevDataShareBatchDelParam;
import com.guhecloud.cube.iot.mgt.device.param.DevDataShareCreateParam;
import com.guhecloud.cube.iot.mgt.device.param.DevDataShareUpdateParam;
import com.guhecloud.cube.iot.mgt.device.vo.DevDataShareSummaryVo;
import com.guhecloud.cube.iot.mgt.device.vo.DevDataShareVo;

import java.util.List;

public interface DevDataShareService extends IService<DevDataShare> {

    void create(DevDataShareCreateParam param);

    void update(DevDataShareUpdateParam param);

    void delete(Long id);

    void batchDelete(DevDataShareBatchDelParam param);

    /**
     * 批量创建数据共享配置
     *
     * @param items 批量创建的共享配置列表
     */
    void batchCreate(List<DevDataShareCreateParam> items);

    List<DevDataShareVo> listAsSource(Long deviceId);

    List<DevDataShareVo> listAsTarget(Long deviceId);

    DevDataShareSummaryVo summary(Long deviceId);

    /**
     * 根据设备ID删除关联的共享配置（同时删除设备作为源和目标的所有配置）
     */
    void removeByDeviceId(Long deviceId);
}

