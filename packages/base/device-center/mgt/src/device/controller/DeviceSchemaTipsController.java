package com.guhecloud.cube.iot.mgt.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSchemaTipsModelVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceSchemaTipsQueryVo;
import com.guhecloud.cube.iot.mgt.core.def.TimeSeriesInterface;
import com.guhecloud.platform.base.context.SessionContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/device/mgt/schema-tips")
public class DeviceSchemaTipsController {
    private TimeSeriesInterface timeSeriesInterface;

    @Autowired(required = false)
    public void setTimeSeries(TimeSeriesInterface timeSeriesInterface) {
        this.timeSeriesInterface = timeSeriesInterface;
    }

    /**
     * 查询设备缓存的元数据信息
     *
     * @param deviceSchemaTipsQueryVo 查询条件
     *
     * @return 分页数据
     */
    @PostMapping("/page")
    public IPage<DeviceSchemaTipsModelVo> historyProp(@RequestBody DeviceSchemaTipsQueryVo deviceSchemaTipsQueryVo) {
        int tenantId = SessionContextHolder.getRequester().getTenant();
        deviceSchemaTipsQueryVo.setTenantId(String.valueOf(tenantId));

        if (deviceSchemaTipsQueryVo.getPageNumber() < 1) {
            deviceSchemaTipsQueryVo.setPageNumber(1);
        }

        return timeSeriesInterface.queryDeviceSchemaInfoPage(deviceSchemaTipsQueryVo);
    }

}
