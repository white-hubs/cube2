package com.guhecloud.cube.base.device.center.core.controller;

import com.guhecloud.cube.base.device.center.core.dto.CreateOrUpdateDeviceDTO;
import com.guhecloud.cube.base.device.center.core.dto.DevicePageDto;
import com.guhecloud.cube.base.device.center.core.param.DelDeviceParam;
import com.guhecloud.cube.base.device.center.core.param.DevicePageParam;
import com.guhecloud.cube.base.device.center.core.param.PwdCheck;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceService;
import com.guhecloud.cube.base.device.center.metadata.annotation.InvokeForm;
import com.guhecloud.cube.base.device.center.metadata.lang.From;
import com.guhecloud.cube.base.device.center.metadata.param.ChangeDeviceLntLatCmd;
import com.guhecloud.cube.base.device.center.metadata.param.ChangeDeviceSpatialCoordinatesCmd;
import com.guhecloud.cube.base.device.center.open.interfaces.web.command.ChangeDeviceInfoCmd;
import com.guhecloud.cube.base.device.center.open.interfaces.web.vo.DeviceDetailVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

import static com.guhecloud.cube.base.device.center.metadata.lang.Const.DEVICE_CENTER_MGT_FRONTEND_SUFFIX;

@Slf4j
@RestController
public class CenterDeviceInstanceController {
    @Resource
    private CenterDeviceService centerDeviceService;

    /**
     * 设备分页接口
     *
     * @param pageParam pageParam
     *
     * @return page
     */
    @GetMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device-instance/page")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public Pagination<DevicePageDto> page(DevicePageParam pageParam) {
        Integer pageNumber = pageParam.getPageNumber();
        Integer pageSize = pageParam.getPageSize();
        if (Objects.isNull(pageNumber) || pageNumber <= 1) {
            pageParam.setPageNumber(1);
        }

        if (Objects.isNull(pageSize) || pageSize <= 0) {
            pageParam.setPageSize(10);
        }

        return centerDeviceService.page(pageParam);
    }

    /**
     * 子应用设备分页接口<br/>
     * TODO: 金哥，这里的接口需要迁移出去
     *
     * @param pageParam pageParam
     *
     * @return page
     */
    @PostMapping("/application-devices/page")
    public Pagination<DevicePageDto> applicationDevicePage(@RequestBody DevicePageParam pageParam) {
        Integer pageNumber = pageParam.getPageNumber();
        Integer pageSize = pageParam.getPageSize();
        if (Objects.isNull(pageNumber) || pageNumber <= 1) {
            pageParam.setPageNumber(1);
        }

        if (Objects.isNull(pageSize) || pageSize <= 0) {
            pageParam.setPageSize(10);
        }

        return centerDeviceService.page(pageParam);
    }

    /**
     * 设备详情(设备中心管理页面专用)
     *
     * @param deviceId id
     *
     * @return detail
     */
    @GetMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device-instance/detail-mgt/{deviceId}")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public CreateOrUpdateDeviceDTO detailOfMgt(@PathVariable Long deviceId) {
        return centerDeviceService.detailOfMgt(deviceId);
    }

    /**
     * 更新设备信息
     *
     * @param changeDeviceInfoCmd 设备更新参数
     *
     * @return 设备详情
     */
    @PutMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device-instance/device")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public DeviceDetailVO changeDeviceInfo(@RequestBody @Valid ChangeDeviceInfoCmd changeDeviceInfoCmd) {
        return centerDeviceService.changeDeviceInfo(changeDeviceInfoCmd);
    }

    /**
     * 更新设备空间坐标信息
     *
     * @param changeDeviceSpatialCoordinatesCmd 设备空间更新参数
     *
     * @return 设备详情
     */
    @PutMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device-instance/device/spatial-coordinates")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public DeviceDetailVO changeDeviceSpatialCoordinates(
            @RequestBody @Valid ChangeDeviceSpatialCoordinatesCmd changeDeviceSpatialCoordinatesCmd
                                                        ) {
        return centerDeviceService.changeDeviceSpatialCoordinates(changeDeviceSpatialCoordinatesCmd);
    }

    /**
     * 更新设备经纬度与/或位置详细描述（扩展表 lnt_and_lat、location_des）
     *
     * @param cmd 设备 ID；可选 lntAndLat（一般为「经度,纬度」）、locationDes；二者至少传其一
     *
     * @return 是否成功
     */
    @PutMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device-instance/device/lnt-and-lat")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public Boolean changeDeviceLntLat(@RequestBody @Valid ChangeDeviceLntLatCmd cmd) {
        return centerDeviceService.changeDeviceLntLat(cmd);
    }

    /**
     * 删除设备
     *
     * @param delDeviceParam 设备id
     *
     * @return 删除结果
     */
    @PostMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device-instance/device/delete")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public Boolean deleteDevice(@RequestBody DelDeviceParam delDeviceParam) {
        if (Objects.isNull(delDeviceParam) || Objects.isNull(delDeviceParam.getIds())
            || delDeviceParam.getIds().isEmpty()) {
            throw new ResourcePreconditionFailedException("ids", "ids不能为空");
        }
        return centerDeviceService.batchDel(delDeviceParam.getIds());
    }

    /**
     * 更新设备密钥
     *
     * @param deviceId 设备ID
     *
     * @return 是否成功
     */
    @PutMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device-instance/secret-key/{deviceId}")
    public Boolean updateSecretKey(@PathVariable String deviceId, String secretKey) {
        return centerDeviceService.updateSecretKey(deviceId, secretKey);
    }

    /**
     * 查询设备详情
     * 此接口应不支持频繁调用
     * 返回基础档案信息+扩展信息
     * 应该提供一个统一的service
     */
    @GetMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device/{deviceId}")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public DeviceDetailVO getById(@PathVariable("deviceId") Long deviceId) {
        return centerDeviceService.getDeviceDetail(deviceId);
    }

    /**
     * 公共查询设备详情
     * 此接口应不支持频繁调用
     * 返回基础档案信息+扩展信息
     * 应该提供一个统一的service
     */
    @GetMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/public/device/{deviceId}")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public DeviceDetailVO publicGetById(@PathVariable("deviceId") Long deviceId) {
        return getById(deviceId);
    }

    /**
     * 查询设备详情
     * 此接口应不支持频繁调用
     * 返回基础档案信息+扩展信息
     * 应该提供一个统一的service
     */
    @GetMapping(DEVICE_CENTER_MGT_FRONTEND_SUFFIX + "/device/by-code/{deviceCode}")
    @InvokeForm(fromInvoke = From.SELF_MGT_FE)
    public DeviceDetailVO getByCode(@PathVariable("deviceCode") String deviceCode) {
        return centerDeviceService.getDeviceDetail(deviceCode);
    }

    @PostMapping("/device-instance/pwd-check")
    public Boolean pwdCheck(PwdCheck pwd) {
        return centerDeviceService.pwdCheck(pwd.getDeviceId(), pwd.getPwd());
    }
}
