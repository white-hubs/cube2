package com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces;

import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.TrafficDetectionAreaCommandService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ChangeTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.DelTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.IncreaseTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ModifyTrafficDetectionAreaCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.commandgateway.command.ModifyTrafficDetectionAreaDeviceCommand;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.TrafficDetectionAreaExportService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.TrafficDetectionAreaPersonFlowService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.TrafficDetectionAreaQueryService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.application.querygateway.query.TrafficDetectionAreaQueryParam;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.TrafficDetectionAreaDeviceAppService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.device.query.DeviceFilterPageQuery;
import com.guhecloud.cube.biz.traffic.detection.server.detection.infrastructure.client.file.TrafficDetectionAreaFileAppService;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.converters.TrafficDetectionAreaConverter;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaDetailVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaMonitorVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionAreaVisualVO;
import com.guhecloud.cube.biz.traffic.detection.server.detection.interfaces.vo.TrafficDetectionDevicePageInfoVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TrafficDetectionAreaController {

    private final TrafficDetectionAreaQueryService trafficDetectionAreaQueryService;

    private final TrafficDetectionAreaCommandService trafficDetectionAreaCommandService;

    private final TrafficDetectionAreaFileAppService trafficDetectionAreaFileAppService;

    private final TrafficDetectionAreaDeviceAppService trafficDetectionAreaDeviceAppService;

    private final TrafficDetectionAreaPersonFlowService trafficDetectionAreaPersonFlowService;

    private final TrafficDetectionAreaExportService trafficDetectionAreaExportService;

    /**
     * 新增人流区域信息
     *
     * @param command 新增信息
     */
    @PostMapping("/traffic-detection-areas")
    public void increaseTrafficDetectionArea(@Valid @RequestBody IncreaseTrafficDetectionAreaCommand command) {
        this.trafficDetectionAreaCommandService.increaseTrafficDetectionArea(command);
    }

    /**
     * 修改人流区域信息
     *
     * @param command 修改信息
     */
    @PutMapping("/traffic-detection-areas")
    public void changeTrafficDetectionArea(@Valid @RequestBody ChangeTrafficDetectionAreaCommand command) {
        this.trafficDetectionAreaCommandService.changeTrafficDetectionArea(command);
    }

    /**
     * 单个删除
     *
     * @param id 人流区域主键
     */
    @DeleteMapping("/traffic-detection-areas/{id}")
    public void removeTrafficDetectionArea(@PathVariable("id") Long id) {
        DelTrafficDetectionAreaCommand command = new DelTrafficDetectionAreaCommand();
        command.setIds(Collections.singleton(id));
        this.trafficDetectionAreaCommandService.delTrafficDetectionArea(command);
    }

    /**
     * 批量删除
     *
     * @param command 删除信息
     */
    @PostMapping("/traffic-detection-areas/batch-remove")
    public void batchRemoveTrafficDetectionArea(@Valid @RequestBody DelTrafficDetectionAreaCommand command) {
        this.trafficDetectionAreaCommandService.delTrafficDetectionArea(command);
    }

    /**
     * 获取人流区域信息列表
     *
     * @return
     */
    @GetMapping("/traffic-detection-areas")
    public List<TrafficDetectionAreaVO> getTrafficDetectionAreas(TrafficDetectionAreaQueryParam param) {
        var trafficDetectionAreas = this.trafficDetectionAreaQueryService.getTrafficDetectionAreas(param);
        return TrafficDetectionAreaConverter.toTrafficDetectionAreaVOList(trafficDetectionAreas);
    }

    /**
     * 人流区域详情
     *
     * @param id 主键
     *
     * @return 流量区域详情
     */
    @GetMapping("/traffic-detection-areas/{id}")
    public TrafficDetectionAreaDetailVO getTrafficDetectionAreaById(@PathVariable("id") Long id) {
        var trafficDetectionArea = this.trafficDetectionAreaQueryService.getTrafficDetectionAreaById(id);
        if (Objects.isNull(trafficDetectionArea)) {
            return null;
        }
        var trafficDetectionAreaDetailVO = TrafficDetectionAreaConverter.toTrafficDetectionAreaDetailVO(trafficDetectionArea);
        trafficDetectionAreaDetailVO.setFiles(trafficDetectionAreaFileAppService.getFilesByBusinessKey(trafficDetectionArea.getId()));
        return trafficDetectionAreaDetailVO;
    }

    /**
     * 获取人流区域信息列表-(分页)
     *
     * @return 人流区域列表
     */
    @GetMapping("/traffic-detection-areas/page")
    public Pagination<TrafficDetectionAreaVO> getTrafficDetectionAreasPage(TrafficDetectionAreaQueryParam param) {
        var trafficDetectionAreaPage = this.trafficDetectionAreaQueryService.getTrafficDetectionAreaPage(param);
        if (CollectionUtils.isEmpty(trafficDetectionAreaPage.getRecords())) {
            return Pagination.empty();
        }
        var areaIdToPersonFlow = this.trafficDetectionAreaPersonFlowService.getAreaIdToPersonFlowMap(trafficDetectionAreaPage.getRecords());
        var trafficDetectionAreaVOS = TrafficDetectionAreaConverter.toTrafficDetectionAreaVOList(trafficDetectionAreaPage.getRecords(), areaIdToPersonFlow);
        return PagingUtils.convert(trafficDetectionAreaPage, trafficDetectionAreaVOS);
    }

    /**
     * 人流区域导出（区域名称、区域描述、人流个数、时间）
     */
    @GetMapping("/traffic-detection-areas/export")
    public void exportTrafficDetectionAreas(TrafficDetectionAreaQueryParam param, HttpServletResponse response) {
        this.trafficDetectionAreaExportService.exportTrafficDetectionAreas(param, response);
    }

    /**
     * 获取视角配置信息
     *
     * @param id 人流区域主键
     *
     * @return 视角配置
     */
    @GetMapping("/traffic-detection-areas/{id}/view-config")
    public TrafficDetectionAreaVisualVO getTrafficDetectionAreaViewConfig(@PathVariable("id") Long id) {
        var trafficDetectionAreaVisual = trafficDetectionAreaQueryService.getTrafficDetectionAreaVisualById(id);
        if (Objects.isNull(trafficDetectionAreaVisual)) {
            return null;
        }
        return TrafficDetectionAreaConverter.toTrafficDetectionAreaViewConfigVO(trafficDetectionAreaVisual);
    }

    /**
     * 人流区域设备配置保存
     *
     * @param id      主键
     * @param command 设备参数
     */
    @PostMapping("/traffic-detection-areas/{id}/devices")
    public void modifyTrafficDetectionAreaDevices(@PathVariable("id") Long id,
                                                  @Valid @RequestBody ModifyTrafficDetectionAreaCommand command
                                                 ) {
        trafficDetectionAreaCommandService.modifyTrafficDetectionAreaDevices(id, command);
    }

    /**
     * 人流区域设备配置编辑
     *
     * @param command 设备参数
     */
    @PutMapping("/traffic-detection-areas/devices/modify")
    public void modifyTrafficDetectionAreaDevice(@Valid @RequestBody ModifyTrafficDetectionAreaDeviceCommand command) {
        trafficDetectionAreaCommandService.modifyTrafficDetectionAreaDevice(command);
    }

    /**
     * 获取人流区域设备配置列表
     *
     * @param query 分页查询参数
     *
     * @return 人流区域设备配置分页列表
     */
    @GetMapping("/traffic-detection-areas/{id}/devices/page")
    public Pagination<TrafficDetectionDevicePageInfoVO> queryDevicePageInfo(@PathVariable("id") Long id,
                                                                            @Valid DeviceFilterPageQuery query
                                                                           ) {
        var devices = trafficDetectionAreaQueryService.TrafficDetectionAreaDevicesById(id);
        if (CollectionUtils.isEmpty(devices)) {
            return Pagination.empty();
        }
        var deviceIds = devices.stream().map(TrafficDetectionAreaDevice::getDeviceId).collect(Collectors.toList());
        query.setMatchedDeviceIds(deviceIds);
        var devicePageInfoVOPagination = trafficDetectionAreaDeviceAppService.queryDevicePageInfo(query);
        return PagingUtils.convert(devicePageInfoVOPagination, TrafficDetectionAreaConverter.toTrafficDetectionDevicePageInfoVOList(devicePageInfoVOPagination.getRecords(), devices));
    }

    /**
     * 获取人流区域监控信息列表
     *
     * @return 人流区域监控信息列表
     */
    @GetMapping("/traffic-detection-areas/monitor-devices")
    public List<TrafficDetectionAreaMonitorVO> trafficDetectionAreaMonitorList() {
        var param = new TrafficDetectionAreaQueryParam();
        var trafficDetectionAreas = this.trafficDetectionAreaQueryService.getTrafficDetectionAreas(param);
        if (CollectionUtils.isEmpty(trafficDetectionAreas)) {
            return Collections.emptyList();
        }
        return TrafficDetectionAreaConverter.toTrafficDetectionAreaMonitorVOS(trafficDetectionAreas, this.trafficDetectionAreaDeviceAppService.queryTrafficDetectionDevices());
    }
}
