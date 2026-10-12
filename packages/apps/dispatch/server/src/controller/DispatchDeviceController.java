package com.guhecloud.cube.biz.dispatch.server.controller;

import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceQueryParam;
import com.guhecloud.cube.biz.dispatch.server.param.DispatchDeviceUpdateParam;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceService;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceDetailVO;
import com.guhecloud.cube.biz.dispatch.server.vo.DispatchDeviceVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 调度设备
 * @date 2022-07-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceController {
    private final DispatchDeviceService dispatchDeviceService;

    /**
     * 功能描述： 调度设备添加
     */
    @PostMapping(value = "/dispatch-device")
    public String add(@Valid @RequestBody DispatchDeviceParam param) {
        return dispatchDeviceService.add(param);
    }

    /**
     * 功能描述： 调度设备修改
     */
    @PutMapping(value = "/dispatch-device/{id}")
    public void update(@PathVariable String id, @Valid @RequestBody DispatchDeviceUpdateParam param) {
        dispatchDeviceService.update(id, param);
    }

    /**
     * 功能描述： 调度设备删除
     */
    @DeleteMapping(value = "/dispatch-device/{id}")
    public void delete(@PathVariable String id) {
        dispatchDeviceService.delete(id);
    }

    /**
     * 功能描述： 调度设备批量删除
     */
    @DeleteMapping(value = "/dispatch-device/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<String> ids) {
        dispatchDeviceService.batchDelete(ids);
    }

    /**
     * 功能描述： 调度设备分页
     */
    @GetMapping(value = "/dispatch-device/page")
    public Pagination<DispatchDeviceVO> getPage(DispatchDeviceQueryParam param) {
        return dispatchDeviceService.getPage(param);
    }

    /**
     * 功能描述： 调度设备详情
     */
    @GetMapping(value = "/dispatch-device/{id}")
    public DispatchDeviceVO getById(@PathVariable String id) {
        return dispatchDeviceService.getById(id);
    }

    /**
     * 功能描述：圈选 查询范围内的设备或用户
     * 暂停使用：计算距离方式错误
     */
    @GetMapping(value = "/dispatch-platform/circle/device")
    public List<DispatchDeviceDetailVO> getDeviceInCircle(@RequestParam double lng, @RequestParam double lat, @RequestParam double radius,
                                                          Boolean onlineStatus
                                                         ) {
        return dispatchDeviceService.getDeviceInCircle(lng, lat, radius, onlineStatus);
    }

    /**
     * 查询设备详情 带gps信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/dispatch-device/detail/page")
    public Pagination<DispatchDeviceDetailVO> getDetailPage(DispatchDeviceQueryParam param) {
        return dispatchDeviceService.detailPage(param);
    }

    /**
     * 查询设备详情 带gps信息
     *
     * @param param
     *
     * @return
     */
    @GetMapping(value = "/dispatch-device/detail/list")
    public List<DispatchDeviceDetailVO> getDetailList(DispatchDeviceQueryParam param) {
        return dispatchDeviceService.detailList(param);
    }

    /**
     * 查询组下的设备
     *
     * @param groupId
     *
     * @return
     */
    @GetMapping(value = "/dispatch-device/group/{groupId}")
    public List<DispatchDeviceDetailVO> deviceInGroup(@PathVariable String groupId) {
        return dispatchDeviceService.deviceInGroup(groupId);
    }

    /**
     * 同步第三方调度台的设备数据
     *
     * @param code
     *
     * @return
     */
    @PostMapping(value = "/dispatch-device/third/{code}/device")
    public void getDeviceInThirdPlatform(@PathVariable String code) {
        dispatchDeviceService.obtainThirdPlatformDevice(code);
    }

}