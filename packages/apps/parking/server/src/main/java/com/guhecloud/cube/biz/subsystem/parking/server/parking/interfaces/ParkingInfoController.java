package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces;

import com.guhecloud.cube.base.device.center.metadata.vo.DevicePageInfoVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.ParkingInfoCommandService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ChangeParkingInfoCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.DelParkingInfoCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.IncreaseParkingInfoCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ModifyParkingInfoDeviceCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.commandgateway.command.ModifyParkingInfoDockConfigCommand;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingInfoQueryService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingSourceEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.valueobj.ParkingInfoDevice;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.area.ParkAreaAppService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.device.ParkDeviceAppService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.device.query.DeviceFilterPageQuery;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.client.file.ParkFileAppService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.converters.ParkingInfoConverter;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoDetailVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoDockConfigVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingInfoVisualVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingLotVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingMonitorVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
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
public class ParkingInfoController {

    private final ParkingInfoQueryService parkingInfoQueryService;

    private final ParkingInfoCommandService parkingInfoCommandService;

    private final ParkFileAppService parkFileAppService;

    private final ParkAreaAppService parkAreaAppService;

    private final ParkDeviceAppService parkDeviceAppService;

    /**
     * 新增车场信息
     *
     * @param command 新增信息
     */
    @PostMapping("/parking-infos")
    public void increaseParkingInfo(@Valid @RequestBody IncreaseParkingInfoCommand command) {
        this.parkingInfoCommandService.increaseParkingInfo(command);
    }

    /**
     * 修改车场信息
     *
     * @param command 修改信息
     */
    @PutMapping("/parking-infos")
    public void changeParkingInfo(@Valid @RequestBody ChangeParkingInfoCommand command) {
        this.parkingInfoCommandService.changeParkingInfo(command);
    }

    /**
     * 单个删除
     *
     * @param id 车场主键
     */
    @DeleteMapping("/parking-infos/{id}")
    public void removeParkingInfo(@PathVariable("id") Long id) {
        DelParkingInfoCommand command = new DelParkingInfoCommand();
        command.setIds(Collections.singleton(id));
        this.parkingInfoCommandService.delParkingInfo(command);
    }

    /**
     * 批量删除
     *
     * @param command 删除信息
     */
    @PostMapping("/parking-infos/batch-remove")
    public void batchRemoveParkingInfo(@Valid @RequestBody DelParkingInfoCommand command) {
        this.parkingInfoCommandService.delParkingInfo(command);
    }

    /**
     * 获取车场信息列表
     *
     * @return
     */
    @GetMapping("/parking-infos")
    public List<ParkingInfoVO> getParkingInfos(ParkingInfoQueryParam param) {
        var parkingInfos = this.parkingInfoQueryService.getParkingInfo(param);
        return ParkingInfoConverter.toParkingVOList(parkingInfos);
    }

    /**
     * 车场详情
     *
     * @param id 主键
     *
     * @return 车场详情
     */
    @GetMapping("/parking-infos/{id}")
    public ParkingInfoDetailVO getParkingInfoById(@PathVariable("id") Long id) {
        var parkingInfo = this.parkingInfoQueryService.getParkingInfoById(id);
        if (Objects.isNull(parkingInfo)) {
            return null;
        }
        var parkingDetailVO = ParkingInfoConverter.toParkingDetailVO(parkingInfo);
        parkingDetailVO.setFiles(parkFileAppService.getFilesByBusinessKey(parkingInfo.getId()));
        if (StringUtils.isNotBlank(parkingDetailVO.getAreaId())) {
            parkingDetailVO.setAreaName(parkAreaAppService.getParkingAreaPathNameByPath(parkingDetailVO.getAreaId()));
        }
        return parkingDetailVO;
    }

    /**
     * 获取车场信息列表-(分页)
     *
     * @return 车场列表
     */
    @GetMapping("/parking-infos-page")
    public Pagination<ParkingInfoVO> getParkingInfosPage(ParkingInfoQueryParam param) {
        var parkingInfoPage = this.parkingInfoQueryService.getParkingInfoPage(param);
        if (CollectionUtils.isEmpty(parkingInfoPage.getRecords())) {
            return Pagination.empty();
        }
        var parkingInfoVOS = ParkingInfoConverter.toParkingVOList(parkingInfoPage.getRecords());
        //区域信息扩充
        var areaPaths = parkingInfoVOS.stream().map(ParkingInfoVO::getAreaId)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toSet());
        var areaPathMap = parkAreaAppService.getParkingAreaPathNamesByPaths(areaPaths);
        parkingInfoVOS.forEach(parkingInfoVO -> {
            if (StringUtils.isNotBlank(parkingInfoVO.getAreaId()) && areaPathMap.containsKey(parkingInfoVO.getAreaId())) {
                parkingInfoVO.setAreaName(areaPathMap.get(parkingInfoVO.getAreaId()));
            }
        });
        return PagingUtils.convert(parkingInfoPage, parkingInfoVOS);
    }

    /**
     * 获取车场视角配置信息
     *
     * @param id 车场主键
     *
     * @return 视角配置
     */
    @GetMapping("/parking-infos/{id}/view-config")
    public ParkingInfoVisualVO getParkingViewConfig(@PathVariable("id") Long id) {
        var parkingInfoVisual = parkingInfoQueryService.getParkingInfoVisualById(id);
        if (Objects.isNull(parkingInfoVisual)) {
            return null;
        }
        return ParkingInfoConverter.toParkingViewConfigVO(parkingInfoVisual);
    }

    /**
     * 车场设备配置保存
     *
     * @param id      主键
     * @param command 设备参数
     */
    @PostMapping("/parking-infos/{id}/devices")
    public void modifyParkingInfoDevices(@PathVariable("id") Long id, @Valid @RequestBody ModifyParkingInfoDeviceCommand command) {
        parkingInfoCommandService.modifyParkingInfoDevices(id, command);
    }

    /**
     * 获取车场设备配置列表
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/parking-infos/{id}/devices/page")
    public Pagination<DevicePageInfoVO> queryDevicePageInfo(@PathVariable("id") Long id, @Valid DeviceFilterPageQuery query) {
        var parkingInfoDevices = parkingInfoQueryService.getParkingInfoDevicesById(id);
        if (CollectionUtils.isEmpty(parkingInfoDevices)) {
            return Pagination.empty();
        }
        var deviceIds = parkingInfoDevices.stream().map(ParkingInfoDevice::getDeviceId).collect(Collectors.toList());
        query.setMatchedDeviceIds(deviceIds);
        return parkDeviceAppService.queryDevicePageInfo(query);
    }

    /**
     * 车场三方对接配置保存
     *
     * @param id。主键
     * @param command 对接参数
     */
    @PostMapping("/parking-infos/{id}/dock-config")
    public void modifyParkingInfoDockConfig(@PathVariable("id") Long id, @Valid @RequestBody ModifyParkingInfoDockConfigCommand command) {
        parkingInfoCommandService.modifyParkingInfoDockConfig(id, command);
    }

    /**
     * 获取车场三方对接配置
     *
     * @param id 主键
     *
     * @return 三方对接配置
     */
    @GetMapping("/parking-infos/{id}/dock-config")
    public ParkingInfoDockConfigVO getParkingInfoDockConfig(@PathVariable("id") Long id) {
        var parkingInfoDockConfig = parkingInfoQueryService.getParkingInfoDockConfigById(id);
        if (Objects.isNull(parkingInfoDockConfig)) {
            return null;
        }
        return ParkingInfoConverter.toParkingDockConfigVO(parkingInfoDockConfig);
    }

    /**
     * 获取有三方对接配置的车场列表-(分页)
     *
     * @return 车场列表
     */
    @GetMapping("/parking-infos/page/with-has-dock-config")
    public Pagination<ParkingInfoVO> getHasDockConfigParkingPage(ParkingInfoQueryParam param) {
        var parkingInfoPage = this.parkingInfoQueryService.getHasDockConfigParkingPage(param);
        if (CollectionUtils.isEmpty(parkingInfoPage.getRecords())) {
            return Pagination.empty();
        }
        var parkingInfoVOS = ParkingInfoConverter.toParkingVOList(parkingInfoPage.getRecords());
        return PagingUtils.convert(parkingInfoPage, parkingInfoVOS);
    }

    /**
     * 获取车位信息
     *
     * @return 车场车位汇总信息
     */
    @GetMapping("/parking-info")
    public ParkingLotVO getParkingInfo(ParkingInfoQueryParam param) {
        var parkingInfos = this.parkingInfoQueryService.getParkingInfo(param);
        if (CollectionUtils.isEmpty(parkingInfos)) {
            return new ParkingLotVO(0, 0, 0);
        }
        var totalNum = 0;
        var totalInNum = 0;
        var totalSpaceNum = 0;
        for (var parkingInfo : parkingInfos) {
            if (Objects.nonNull(parkingInfo)) {
                totalNum += parkingInfo.getTotalNum();
                totalInNum += parkingInfo.getTotalInNum();
                totalSpaceNum += parkingInfo.getTotalSpaceNum();
            }

        }
        return new ParkingLotVO(totalNum, totalInNum, totalSpaceNum);
    }

    /**
     * 获取车场监控信息列表
     *
     * @return 车场监控信息列表
     */
    @GetMapping("/parking/monitor-devices")
    public List<ParkingMonitorVO> parkingMonitorList() {
        var param = new ParkingInfoQueryParam();
        param.setSource(ParkingSourceEnum.SELF_BUILT);
        var parkingInfos = this.parkingInfoQueryService.getParkingInfo(param);
        if (CollectionUtils.isEmpty(parkingInfos)) {
            return Collections.emptyList();
        }
        return ParkingInfoConverter.toParkingMonitorVOS(parkingInfos, this.parkDeviceAppService.queryParkingDevices());
    }
}
