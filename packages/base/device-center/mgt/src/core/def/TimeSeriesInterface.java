package com.guhecloud.cube.iot.mgt.core.def;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.RuleTriggerLog;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSamplingHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSchemaTipsModelVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSchemaTipsQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.LinkageEventHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.MultiDevicePropHistoryQueryVo;

import java.util.List;

public interface TimeSeriesInterface {
    //region read

    /**
     * 单设备属性历史分页查询
     */
    IPage<DevicePropHistoryResultVo> querySingleDevicePropPage(DevicePropHistoryQueryVo devicePropHistoryQueryVo, Integer pageNumber, Integer pageSize);

    /**
     * 多设备属性历史一次性批量查询（避免逐设备多次查询）
     */
    List<DevicePropHistoryResultVo> queryMultiDevicePropHistory(MultiDevicePropHistoryQueryVo multiDevicePropHistoryQueryVo);

    /**
     * 单设备采样属性历史分页查询
     */
    IPage<DeviceSamplingHistoryResultVo> queryDeviceSamplingPropPage(DeviceSamplingHistoryQueryVo deviceSamplingHistoryQueryVo, Integer pageNumber, Integer pageSize);

    /**
     * 单设备事件历史分页查询
     */
    IPage<DeviceEventHistoryResultVo> querySingleDeviceEventPage(DeviceEventHistoryQueryVo toEventHistoryQueryVo, Integer pageNumber, Integer pageSize);

    /**
     * 单设备联动历史分页查询
     */
    IPage<RuleTriggerLog> querySingleLinkagePage(LinkageEventHistoryQueryVo linkageEventHistoryQueryVo);

    /**
     * 设备属性提示信息分页查询
     */
    IPage<DeviceSchemaTipsModelVo> queryDeviceSchemaInfoPage(DeviceSchemaTipsQueryVo deviceSchemaTipsQueryVo);

    //endregion
}
