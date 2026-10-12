package com.guhecloud.cube.srv.gen.securitymanagement.client;

import com.guhecloud.cube.srv.gen.securitymanagement.param.DeviceExportAppParam;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.param.ModelUnderDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.DeviceMonitoringVo;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("securityManagement")
public interface SecurityDeviceClient {
    /**
     * @Description 设备分页列表内部使用
     * @Author lrj
     */
    @PostMapping("/getInsideDeviceMonitoringPage")
    Pagination<DeviceMonitoringVo> getInsideDeviceMonitoringPage(@RequestBody InsideSecurityDeviceParam param);

    /**
     * @Description 设备列表内部使用
     * @Author lrj
     */
    @PostMapping("/getDeviceMonitoringList")
    List<DeviceMonitoringVo> getDeviceMonitoringList(@RequestBody InsideSecurityDeviceParam param);

    /**
     * @Description 设备列表分页(内部调用)
     * @Author lrj
     */
    @PostMapping("/getApplicationSecurityDevicePage")
    Pagination<SecurityDeviceVo> getSecurityDevicePage(@RequestBody InsideSecurityDeviceParam param);

    /**
     * @Description 设备关联导入
     * @Author lrj
     */
    @PostMapping("/add-security-device")
    void addSecurityDevice(@RequestBody DeviceExportAppParam param);

    /**
     * @Description POST批量删除设备
     * @Author lrj
     */
    @PostMapping("/deleteSecurityDeviceBatch")
    void deleteSecurityDeviceBatch(@RequestBody List<Long> ids);

    /**
     * 按应用编码查询关联设备分页列表。
     *
     * @param applicationCode 应用编码
     * @param pageNumber 页码
     * @param pageSize 每页条数
     * @param areaId 区域 ID
     * @param ids 设备中心设备 ID 筛选条件
     *
     * @return 关联设备分页列表
     */
    @GetMapping("/getSecurityDevicePage/{applicationCode}")
    Pagination<SecurityDeviceVo> getSecurityDevicePage(@PathVariable("applicationCode") String applicationCode,
                                                        @RequestParam("pageNumber") Integer pageNumber,
                                                        @RequestParam("pageSize") Integer pageSize,
                                                        @RequestParam(value = "areaId", required = false) String areaId,
                                                        @RequestParam(value = "ids", required = false) List<Long> ids);

    /**
     * @Description 通过应用型号查设备
     * @Author lrj
     */
    @PostMapping("/getDeviceByModel")
    Pagination<SecurityDeviceVo> getDeviceByModel(@RequestBody ModelUnderDeviceParam param);

    /**
     * @Description pc通过应用型号查设备
     * @Author lrj
     */
    @PostMapping("/getDeviceListByModel")
    List<SecurityDeviceVo> getDeviceListByModel(@RequestBody ModelUnderDeviceParam param);

    /**
     * @Description 设备属性列表
     * @Author lrj
     */
    @GetMapping("/inner/gear-position-ids")
    List<Long> getInnerGearPositionIds();

    /**
     * 应用设备主键集合
     *
     * @param applicationCode
     *
     * @return
     */
    @GetMapping("/application-device/ids/{applicationCode}")
    List<Long> getSecurityDeviceIds(@PathVariable("applicationCode") String applicationCode);
}
