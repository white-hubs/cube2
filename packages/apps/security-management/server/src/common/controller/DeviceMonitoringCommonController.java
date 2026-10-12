package com.guhecloud.cube.srv.gen.securitymanagement.server.common.controller;

import com.guhecloud.cube.biz.mdata.client.vo.AreaListVO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.DeviceTSDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.dto.SecurityDeviceInfoDTO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceCollectionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.DeviceMonitoringParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.HistoryDataParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.DeviceCollectionService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.DeviceMonitoringCommonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceCollectionVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceMonitoringVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.DeviceProdCategoryVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.ModelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.SecurityStatusDevicesNumVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description 设备监测
 * @Author lrj
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceMonitoringCommonController {
    private final DeviceMonitoringCommonService deviceMonitoringCommonService;

    private final DeviceCollectionService deviceCollectionService;

    /**
     * @Description 通过应用获取区域树
     * @Author lrj
     */
    @GetMapping("/getAreaTree/{applicationCode}")
    public List<AreaListVO> getAreaTree(@PathVariable String applicationCode) {
        return deviceMonitoringCommonService.getAreaTree(applicationCode);
    }

    /**
     * @Description 设备状态数据数
     * @Author lrj
     */
    @GetMapping("/getStatusList/{applicationCode}")
    public SecurityStatusDevicesNumVo getStatusList(@PathVariable String applicationCode) {
        return deviceMonitoringCommonService.getStatusList(applicationCode);
    }

    /**
     * @Description 产品类型
     * @Author lrj
     */
    @GetMapping("/getApplicationGenreVo/{applicationCode}")
    public List<DeviceProdCategoryVo> getApplicationGenreVo(@PathVariable String applicationCode) {
        return deviceMonitoringCommonService.getApplicationGenreVo(applicationCode);
    }

    /**
     * 产品型号
     *
     * @param applicationCode
     *
     * @return
     */
    @GetMapping("/getApplicationModels/{applicationCode}")
    public List<ModelVo> getApplicationModels(@PathVariable String applicationCode) {
        return deviceMonitoringCommonService.getApplicationModels(applicationCode);
    }

    /**
     * @Description 收藏下拉
     * @Author lrj
     */
    @GetMapping("/getDeviceCollection/{applicationCode}")
    public List<DeviceCollectionVo> getDeviceCollection(@PathVariable String applicationCode) {
        return deviceMonitoringCommonService.getDeviceCollection(applicationCode);
    }

    /**
     * @Description 收藏/取消
     * @Author lrj
     */
    @PostMapping("/addDeviceCollection")
    public void addDeviceCollection(@RequestBody DeviceCollectionParam param) {
        deviceCollectionService.addDeviceCollection(param);
    }

    /**
     * @Description app设备数
     * @Author lrj
     */
    @GetMapping("/getDevicesNum/{applicationCode}")
    public Long getDevicesNum(@PathVariable String applicationCode) {
        return deviceMonitoringCommonService.getDevicesNum(applicationCode);
    }

    /**
     * @Description app收藏设备数
     * @Author lrj
     */
    @GetMapping("/getCollectionDevicesNum/{applicationCode}")
    public Long getCollectionDevicesNum(@PathVariable String applicationCode) {
        return deviceMonitoringCommonService.getCollectionDevicesNum(applicationCode);
    }

    /**
     * @Description 监测设备分页列表
     * @Author lrj
     */
    @GetMapping("/getDeviceMonitoringPage/{applicationCode}")
    public Pagination<SecurityDeviceInfoDTO> getDeviceMonitoringPage(@PathVariable String applicationCode, DeviceMonitoringParam param) {
        return deviceMonitoringCommonService.getDeviceMonitoringPage(applicationCode, param);
    }

    @GetMapping("/getDeviceMonitoringByTenantPage/{applicationCode}")
    public Pagination<SecurityDeviceInfoDTO> getDeviceMonitoringByTenantPage(@PathVariable String applicationCode,
                                                                             DeviceMonitoringParam param
                                                                            ) {
        SessionContextHolder.setRequester(new Requester(SessionContextHolder.getRequester().getUri(), SessionContextHolder.getRequester()
                .getHost(), SessionContextHolder.getRequester().getDomain(), SessionContextHolder.getRequester()
                                                                .getAuthorization(), param.getTenant(), SessionContextHolder.getRequester()
                                                                .getTerminal(), SessionContextHolder.getRequester()
                                                                .getVersion(), SessionContextHolder.getRequester()
                                                                .getSignature(), SessionContextHolder.getRequester().getPermission()));
        Pagination<SecurityDeviceInfoDTO> pagination = deviceMonitoringCommonService.getDeviceMonitoringPage(applicationCode, param);
        SessionContextHolder.clearSessionContext();
        return pagination;
    }

    /**
     * @Description app收藏
     * @Author lrj
     */
    @GetMapping("/getCollectionDeviceMonitoringPage/{applicationCode}")
    public Pagination<SecurityDeviceInfoDTO> getCollectionDeviceMonitoringPage(@PathVariable String applicationCode,
                                                                               DeviceMonitoringParam param
                                                                              ) {
        return deviceMonitoringCommonService.getCollectionDeviceMonitoringPage(applicationCode, param);
    }

    /**
     * @Description 设备信息
     * @Author lrj
     */
    @GetMapping("/getDeviceInfo/{deviceId}")
    public Map<String, Object> getDeviceInfo(@PathVariable Long deviceId) {
        return deviceMonitoringCommonService.getDeviceInfo(deviceId);
    }

    /**
     * @Description 历史数据
     * @Author lrj
     */
    @PostMapping("/getDeviceHistoryData")
    public DeviceTSDTO getDeviceHistoryData(@RequestBody HistoryDataParam param) {
        Assert.isParameterError(CollectionUtils.isEmpty(param.getAttributeCode()), "未选择属性");
        return deviceMonitoringCommonService.getDeviceHistoryData(param);
    }

    /**
     * @Description 设备分页列表内部使用
     * @Author lrj
     */
    @PostMapping("/getInsideDeviceMonitoringPage")
    public Pagination<SecurityDeviceInfoDTO> getInsideDeviceMonitoringPage(@RequestBody InsideSecurityDeviceParam param) {

        return deviceMonitoringCommonService.getInsideDeviceMonitoringPage(param);
    }

    /**
     * @Description 设备列表内部使用
     * @Author lrj
     */
    @PostMapping("/getDeviceMonitoringList")
    public List<DeviceMonitoringVo> getDeviceMonitoringList(@RequestBody InsideSecurityDeviceParam param) {
        param.setPageSize(100000);
        param.setPageNumber(1);
        Pagination<SecurityDeviceInfoDTO> deviceMonitoringList = deviceMonitoringCommonService.getDeviceMonitoringList(param);
        List<SecurityDeviceInfoDTO> records = deviceMonitoringList.getRecords();
        List<DeviceMonitoringVo> monitoringVos = ObjectUtils.copying(records, DeviceMonitoringVo.class);
        return monitoringVos;
    }
}
