package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.base.device.center.metadata.dto.DeviceProductCountDTO;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DeviceMethodControlCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.DevicePropControlCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupDeviceCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupDeviceQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDevicePageResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分组设备相关接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionMemberGroupDeviceController {
    private final DisinfectionMemberGroupDeviceService disinfectionMemberGroupDeviceService;

    /**
     * 设备属性控制批量
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PutMapping("/bus/member/group-device/prop-batch")
    public Boolean devicePropControlBatch(@RequestBody DevicePropControlCommandParam param) {
        return disinfectionMemberGroupDeviceService.devicePropControlBatch(param);
    }

    /**
     * 设备方法控制批量
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @PutMapping("/bus/member/group-device/method-batch")
    public Boolean deviceMethodControlBatch(@RequestBody @Valid DeviceMethodControlCommandParam param) {
        return disinfectionMemberGroupDeviceService.deviceMethodControlBatch(param);
    }

    /**
     * 绑定单个设备(通过code绑定)
     * 可携带：latitude/longitude、districtLocation/locationDetail；绑定成功且传入经纬度或详细位置时，会同步写入设备中心 {@code lnt_and_lat}、{@code location_des}
     *
     * @param param 设备信息
     *
     * @return 是否成功
     */
    @PostMapping("/bus/member/group-device/bind-one")
    public Boolean bindDeviceOne(@RequestBody MemberGroupDeviceCommandParam param) {
        return disinfectionMemberGroupDeviceService.bindDeviceOne(param);
    }

    /**
     * 批量绑定设备
     * 可携带：latitude/longitude、districtLocation/locationDetail；绑定成功且传入经纬度或详细位置时，会同步写入设备中心 {@code lnt_and_lat}、{@code location_des}
     *
     * @param param 绑定信息
     *
     * @return 是否成功
     */
    @PostMapping("/bus/member/group-device/bind-batch")
    public Boolean bindDeviceBatch(@RequestBody MemberGroupDeviceCommandParam param) {
        return disinfectionMemberGroupDeviceService.bindDeviceBatch(param);
    }

    /**
     * 批量解绑设备
     *
     * @param param 解绑信息
     *
     * @return 是否成功
     */
    @PostMapping("/bus/member/group-device/unbind-batch")
    public Boolean unbindDeviceBatch(@RequestBody MemberGroupDeviceCommandParam param) {
        return disinfectionMemberGroupDeviceService.unbindDeviceBatch(param);
    }

    /**
     * 获取产品设备数量
     *
     * @param param 条件
     *
     * @return 产品设备数量集合
     */
    @GetMapping("/bus/member/group-device/product-list")
    public List<DeviceProductCountDTO> getGroupDeviceProductList(MemberGroupDeviceQueryParam param) {
        return disinfectionMemberGroupDeviceService.getGroupDeviceProductList(param);
    }

    /**
     * 设备列表
     *
     * @param param 条件
     *
     * @return 分页列表
     */
    @GetMapping("/bus/member/group-device/page")
    public Pagination<MemberDevicePageResult> memberGroupDevicePage(MemberGroupDeviceQueryParam param) {
        return disinfectionMemberGroupDeviceService.memberGroupDevicePage(param);
    }
}
