package com.guhecloud.cube.srv.gen.securitymanagement.server.common.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceDeletedParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceExportAppParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.ModelUnderDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.SecurityCommonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceProdCategoryVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.ModelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceApplicationCodeStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceApplicationStatusStatisticsVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceOutVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityDeviceVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityCommonController {

    private final SecurityCommonService securityCommonService;

    /**
     * @Description 获取应用ids
     * @Author lrj
     */
    @GetMapping("/device-ids/{applicationCode}")
    public List<Long> getDeviceIds(@PathVariable String applicationCode) {

        List<SecurityDevice> list = securityCommonService.list(Wrappers.<SecurityDevice>lambdaQuery()
                                                                       .eq(SecurityDevice::getApplicationCode, applicationCode));
        List<Long> collect = list.stream().map(SecurityDevice::getDeviceId).distinct().collect(Collectors.toList());

        return collect;
    }

    /**
     * @Description 设备列表分页
     * @Author lrj
     */
    @GetMapping("/getSecurityDevicePage/{applicationCode}")
    public Pagination<SecurityDeviceVo> getSecurityDevicePage(@PathVariable String applicationCode, DeviceParam param) {
        return securityCommonService.getSecurityDevicePage(applicationCode, param);
    }

    /**
     * @Description post请求设备列表分页
     * @Author lrj
     */
    @PostMapping("/screen-out-existing-data")
    public Pagination<SecurityDeviceVo> doPostSecurityDevicePage(@RequestBody DeviceParam param) {
        return securityCommonService.doPostSecurityDevicePage(param);
    }

    /**
     * @Description 删除设备
     * @Author lrj
     */
    @DeleteMapping("/deleteSecurityDevice/{id}")
    public void deleteSecurityDevice(@PathVariable Long id) {
        securityCommonService.deleteSecurityDevice(id);
    }

    /**
     * @Description 批量删除设备
     * @Author lrj
     */
    @DeleteMapping("/deleteSecurityDeviceBatch")
    public void deleteSecurityDeviceBatch(@RequestParam("ids") List<Long> ids) {
        securityCommonService.deleteSecurityDeviceBatch(ids);
    }

    /**
     * @Description 批量删除设备
     * @Author lrj
     */
    @PostMapping("/deleteSecurityDeviceBatch")
    public void postDeleteSecurityDeviceBatch(@RequestBody List<Long> ids) {
        securityCommonService.deleteSecurityDeviceBatch(ids);
    }

    /**
     * 删除条件设备
     *
     * @param param
     */
    @DeleteMapping("/delete/condition/security-device")
    public void conditionDeletedSecurityDevice(@RequestBody DeviceDeletedParam param) {
        securityCommonService.conditionDeletedSecurityDevice(param);
    }

    /**
     * @Description 设备关联导入
     * @Author lrj
     */
    @Deprecated
    @PostMapping("/add-security-device")
    public void addSecurityDevice(@RequestBody DeviceExportAppParam param) {
        securityCommonService.addSecurityDevice(param);
    }

    /**
     * @Description 通过应用查询类型
     * @Author lrj
     */
    @GetMapping("/getGenreVo/{applicationCode}")
    public List<DeviceProdCategoryVo> getGenreVo(@PathVariable String applicationCode) {
        return securityCommonService.getGenreVo(applicationCode);
    }

    /**
     * @Description 通过应用类型查询型号（废除）
     * @Author lrj
     */
    @GetMapping("/getGenreVo/{applicationCode}/{genre}")
    public List<ModelVo> getModelVo(@PathVariable("applicationCode") String applicationCode, @PathVariable("genre") String genre
                                   ) {
        return securityCommonService.getModelVo(applicationCode, genre);
    }

    /**
     * @Description 通过应用型号查设备
     * @Author lrj
     */
    @PostMapping("/getDeviceByModel")
    public Pagination<SecurityDeviceVo> getDeviceByModel(@RequestBody ModelUnderDeviceParam param) {
        return securityCommonService.getDeviceByModel(param);
    }

    /**
     * @Description pc通过应用型号查设备
     * @Author lrj
     */
    @PostMapping("/getDeviceListByModel")
    public List<SecurityDeviceVo> getDeviceListByModel(@RequestBody ModelUnderDeviceParam param) {
        return securityCommonService.getDeviceListByModel(param);
    }

    /**
     * @Description 设备列表分页(内部调用)
     * @Author lrj
     */
    @PostMapping("/getApplicationSecurityDevicePage")
    public Pagination<SecurityDeviceVo> getSecurityDevicePage(@RequestBody InsideSecurityDeviceParam param) {
        DeviceParam copying = ObjectUtils.copying(param, DeviceParam.class);
        copying.setIds(param.getDeviceIds());
        return securityCommonService.getSecurityDevicePage(param.getApplicationCode(), copying);
    }

    /**
     * @Description 设备列表
     * @Author lrj
     */
    @GetMapping("/application-device/{applicationCode}")
    public List<SecurityDeviceOutVo> getSecurityDeviceList(@PathVariable String applicationCode, DeviceParam param) {
        return securityCommonService.getSecurityDeviceList(applicationCode, param);
    }

    /**
     * 获取应用设备集合
     *
     * @param applicationCode
     *
     * @return
     */
    @GetMapping("/application-device/ids/{applicationCode}")
    public List<Long> getSecurityDeviceIds(@PathVariable("applicationCode") String applicationCode) {
        return securityCommonService.getSecurityDeviceIds(applicationCode);
    }

    /**
     * 按应用统计设备数量。
     *
     * @return 应用设备数量统计，按设备数量倒序排列
     */
    @GetMapping("/security-device/application-statistics")
    public List<SecurityDeviceApplicationCodeStatisticsVO> getApplicationDeviceStatistics() {
        return securityCommonService.getApplicationDeviceStatistics();
    }

    /**
     * 按应用统计在线、离线设备数量。
     *
     * @return 应用在线、离线设备数量统计，按设备总数倒序排列
     */
    @GetMapping("/security-device/application-status-statistics")
    public List<SecurityDeviceApplicationStatusStatisticsVO> getApplicationDeviceStatusStatistics() {
        return securityCommonService.getApplicationDeviceStatusStatistics();
    }
}
