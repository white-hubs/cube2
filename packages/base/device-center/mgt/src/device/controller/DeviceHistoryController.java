package com.guhecloud.cube.iot.mgt.device.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.guhecloud.cube.iot.mgt.comm.platform.PlatformEvnCom;
import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DeviceEventHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryQueryVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.timeseries.DevicePropHistoryResultVo;
import com.guhecloud.cube.iot.mgt.core.def.TimeSeriesInterface;
import com.guhecloud.cube.iot.mgt.device.model.DevDevice;
import com.guhecloud.cube.iot.mgt.device.param.DeviceEventHistoryParam;
import com.guhecloud.cube.iot.mgt.device.param.DevicePropHistoryParam;
import com.guhecloud.cube.iot.mgt.device.service.DevDeviceService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/device/mgt")
public class DeviceHistoryController {
    @Resource
    private DevDeviceService devDeviceService;
    private TimeSeriesInterface timeSeriesInterface;
    @Resource
    private PlatformEvnCom platformEvnCom;

    @Autowired(required = false)
    public void setTimeSeries(TimeSeriesInterface timeSeriesInterface) {
        this.timeSeriesInterface = timeSeriesInterface;
    }

    @GetMapping("/history/prop")
    @Deprecated
    public List<DevicePropHistoryResultVo> historyProp(DevicePropHistoryParam devicePropHistoryParam) {
        return null;
    }

    @GetMapping("/history/page/prop")
    public IPage<DevicePropHistoryResultVo> historyPropPage(DevicePropHistoryParam devicePropHistoryParam, Integer pageNumber,
                                                            Integer pageSize
                                                           ) {
        Assert.isTrue(platformEvnCom.isIotPlatform(), "当前环境不是物联网平台,非法调用");
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);

        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }

        if (pageSize == null) {
            pageSize = 100;
        }

        return timeSeriesInterface.querySingleDevicePropPage(toPropHistoryQueryVo(tenant, devicePropHistoryParam), pageNumber, pageSize);
    }

    @GetMapping("/history/page/event")
    public IPage<DeviceEventHistoryResultVo> eventHistoryPage(DeviceEventHistoryParam deviceEventHistoryParam, Integer pageNumber,
                                                              Integer pageSize
                                                             ) {
        //TODO: implements me
        Assert.isTrue(platformEvnCom.isIotPlatform(), "当前环境不是物联网平台,非法调用");
        Integer tenant = SessionContextHolder.getRequesterProperty(Requester::getTenant);

        if (pageNumber == null || pageNumber < 1) {
            pageNumber = 1;
        }

        if (pageSize == null) {
            pageSize = 100;
        }

        return timeSeriesInterface.querySingleDeviceEventPage(toEventHistoryQueryVo(tenant, deviceEventHistoryParam), pageNumber, pageSize);
    }

    private String queryProductIdByDeviceId(Long deviceId) {
        DevDevice devDevice = devDeviceService.getById(deviceId);
        if (ObjectUtils.isEmpty(devDevice)) {
            throw new IllegalArgumentException("设备不存在");
        }
        return String.valueOf(devDevice.getProductId());
    }

    /**
     * 转换为查询参数
     *
     * @param tenant                 租户
     * @param devicePropHistoryParam 参数
     *
     * @return 查询参数
     */
    private DevicePropHistoryQueryVo toPropHistoryQueryVo(Integer tenant, DevicePropHistoryParam devicePropHistoryParam) {
        DevicePropHistoryQueryVo devicePropHistoryQueryVo = new DevicePropHistoryQueryVo();
        devicePropHistoryQueryVo.setTenantId(String.valueOf(tenant));
        //为支持品类变更
        devicePropHistoryQueryVo.setProductId(queryProductIdByDeviceId(devicePropHistoryParam.getDeviceId()));
        devicePropHistoryQueryVo.setDeviceId(devicePropHistoryParam.getDeviceId());
        devicePropHistoryQueryVo.setPropCodes(devicePropHistoryParam.getPropCodes());
        devicePropHistoryQueryVo.setStartTime(devicePropHistoryParam.getStartTime());
        devicePropHistoryQueryVo.setEndTime(devicePropHistoryParam.getEndTime());
        devicePropHistoryQueryVo.setOrder(devicePropHistoryParam.getOrder());
        devicePropHistoryQueryVo.setMaxRecords(devicePropHistoryParam.getMaxRecords());
        return devicePropHistoryQueryVo;
    }

    private DeviceEventHistoryQueryVo toEventHistoryQueryVo(Integer tenant, DeviceEventHistoryParam deviceEventHistoryParam) {
        DeviceEventHistoryQueryVo deviceEventHistoryQueryVo = new DeviceEventHistoryQueryVo();
        deviceEventHistoryQueryVo.setTenantId(String.valueOf(tenant));
        deviceEventHistoryQueryVo.setDeviceId(deviceEventHistoryParam.getDeviceId());
        deviceEventHistoryQueryVo.setEventCodes(deviceEventHistoryParam.getEventCodes());
        deviceEventHistoryQueryVo.setStartTime(deviceEventHistoryParam.getStartTime());
        deviceEventHistoryQueryVo.setEndTime(deviceEventHistoryParam.getEndTime());
        deviceEventHistoryQueryVo.setOrder(deviceEventHistoryParam.getOrder());
        deviceEventHistoryQueryVo.setMaxRecords(deviceEventHistoryParam.getMaxRecords());
        deviceEventHistoryQueryVo.setBizType(deviceEventHistoryParam.getBizType());

        if (StringUtils.isBlank(deviceEventHistoryQueryVo.getBizType())) {
            deviceEventHistoryQueryVo.setBizType("touch");
        }
        return deviceEventHistoryQueryVo;
    }
}
