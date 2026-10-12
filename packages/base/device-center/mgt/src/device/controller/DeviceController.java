package com.guhecloud.cube.iot.mgt.device.controller;

import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.KeyValueWithTs;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DeviceDigestVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.DevicePropValueVo;
import com.guhecloud.cube.iot.mgt.core.data.vo.cache.ProductPropSchema;
import com.guhecloud.cube.iot.mgt.core.data.vo.device.DeviceRelTimeValVo;
import com.guhecloud.cube.iot.mgt.device.param.BatchUpdateDeviceParam;
import com.guhecloud.cube.iot.mgt.device.param.CommandHistoryPageParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceBatchDelParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceCreateParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceListParam;
import com.guhecloud.cube.iot.mgt.device.param.DevicePageParam;
import com.guhecloud.cube.iot.mgt.device.param.DeviceUpdateParam;
import com.guhecloud.cube.iot.mgt.device.service.DevDeviceService;
import com.guhecloud.cube.iot.mgt.device.vo.CommandHistoryVo;
import com.guhecloud.cube.iot.mgt.device.vo.CommandLogVo;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceThinPairVo;
import com.guhecloud.cube.iot.mgt.device.vo.DeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.guhecloud.cube.iot.mgt.Const.IOT_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
@RequestMapping(IOT_MGT_FRONTEND_SUFFIX + "/device/mgt")
public class DeviceController {

    @Resource
    private DevDeviceService devDeviceService;

    @GetMapping("/{deviceId}")
    public DeviceVo deviceDetail(@PathVariable Long deviceId) {
        return devDeviceService.deviceDetail(deviceId);
    }

    @GetMapping("/list")
    public List<DeviceVo> list(DeviceListParam param) {
        return devDeviceService.listInfoByIds(param);
    }

    /**
     * 分页查询设备命令下发历史记录
     */
    @PostMapping("/command-history/page")
    public Pagination<CommandHistoryVo> getCommandHistoryPage(@RequestBody CommandHistoryPageParam param) {
        return devDeviceService.getCommandHistoryPage(param);
    }

    /**
     * 查询指定命令的详情日志
     */
    @GetMapping("/command-history/{instanceId}/logs")
    public List<CommandLogVo> getCommandLogs(@PathVariable String instanceId) {
        return devDeviceService.getCommandLogs(instanceId);
    }

    @GetMapping("/page")
    public Pagination<DeviceVo> page(DevicePageParam devicePageParam) {
        return devDeviceService.listPage(devicePageParam);
    }

    @PostMapping("")
    public void saveDevice(@RequestBody DeviceCreateParam deviceCreateParam) {
        devDeviceService.saveDevice(deviceCreateParam);
    }

    @PostMapping("/batch/del")
    public void batchDel(@RequestBody DeviceBatchDelParam deviceBatchDelParam) {
        if (Objects.nonNull(deviceBatchDelParam)) {
            devDeviceService.batchDel(deviceBatchDelParam.getIds());
        }
    }

    @PutMapping("update-device")
    public void updateDevice(@RequestBody DeviceUpdateParam deviceUpdateParam) {
        devDeviceService.updateDevice(deviceUpdateParam);
    }

    @PutMapping("/batch-update")
    public String batchUpdate(@RequestBody BatchUpdateDeviceParam param) {
        return devDeviceService.batchUpdate(param);
    }

    @GetMapping("/{productId}/dev-instance")
    public List<DeviceThinPairVo> listDeviceByProductId(@PathVariable Long productId) {
        return devDeviceService.listDeviceByProductId(productId);
    }

    @GetMapping("/countByProdId/{productId}")
    public Long countByProdId(@PathVariable Long productId) {
        return devDeviceService.countByProdId(productId);
    }

    @GetMapping("/devRelVal/{deviceId}")
    public DeviceRelTimeValVo queryDevRelVal(@PathVariable String deviceId) {
        Assert.isNotBlank(deviceId, "设备ID不能为空");

        DeviceDigestVo deviceDigestVo = devDeviceService.queryDeviceDigest(Long.valueOf(deviceId));
        if (Objects.isNull(deviceDigestVo)) {
            return null;
        }

        //构建返回对象
        DeviceRelTimeValVo deviceRelTimeValVo = new DeviceRelTimeValVo();
        deviceRelTimeValVo.setDeviceId(deviceId);
        deviceRelTimeValVo.setDeviceName(deviceDigestVo.getDeviceName());
        deviceRelTimeValVo.setPropValList(new ArrayList<>());

        //没有属性
        List<ProductPropSchema> propSchemas = devDeviceService.queryProductPropSchema(deviceDigestVo.getProdId());
        if (propSchemas.isEmpty()) {
            return deviceRelTimeValVo;
        }

        //获取属性值
        for (ProductPropSchema propSchema : propSchemas) {
            Optional<DevicePropValueVo> devicePropValueVo = devDeviceService.queryDevicePropCache(Long.valueOf(deviceId), propSchema.getPropCode());

            if (devicePropValueVo.isPresent()) {
                DevicePropValueVo dpv = devicePropValueVo.get();

                KeyValueWithTs keyValueWithTs = new KeyValueWithTs();
                keyValueWithTs.setPreValue(dpv.getPreValue());
                keyValueWithTs.setValue(dpv.getValue());
                keyValueWithTs.setKey(propSchema.getPropCode());
                keyValueWithTs.setTs(dpv.getTs());
                deviceRelTimeValVo.getPropValList().add(keyValueWithTs);
            }
        }

        return deviceRelTimeValVo;
    }

    @GetMapping("/secret/{deviceId}")
    public String queryDeviceSecret(@PathVariable String deviceId) {
        Assert.isNotBlank(deviceId, "设备ID不能为空");
        return devDeviceService.queryDeviceSecret(deviceId);
    }

    @GetMapping("/visible/gateway")
    public List<DeviceVo> listVisibleGateway() {
        return devDeviceService.listVisibleGateway();
    }
}