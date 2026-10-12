package com.guhecloud.cube.base.device.center.core.controller;

import com.guhecloud.cube.base.device.center.core.service.CenterDeviceTagService;
import com.guhecloud.cube.base.device.center.metadata.param.DeviceBindTagsCmd;
import com.guhecloud.cube.base.device.center.metadata.param.TagBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.param.TagUnBindDeviceCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.cube.base.device.center.metadata.std.param.QueryDeviceByAreaParam;
import com.guhecloud.cube.base.device.center.metadata.std.vo.CustomerTagVO;
import com.guhecloud.cube.base.device.center.metadata.vo.DeviceTagVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
public class CenterDeviceTagController {

    @Resource
    private CenterDeviceTagService centerDeviceTagService;

    /**
     * 解绑Tag<br/>
     */
    @PutMapping("/tag/unbind")
    public void unbindTag(@RequestBody TagUnBindDeviceCmd tagUnBindDeviceCmd) {

        centerDeviceTagService.unbindTag(tagUnBindDeviceCmd);
    }

    /**
     * 新建Tag
     *
     * @param createTagCmd 新建Tag参数
     */
    @PostMapping("/tag")
    public void createTag(@RequestBody CreateTagCmd createTagCmd) {

        centerDeviceTagService.createTagIfNeed(createTagCmd);
    }

    /**
     * 检查标签是否已经存在
     *
     * @param tagCode 标签code
     *
     * @return 标签数量
     */
    @GetMapping("/tag/check/{tagCode}")
    public Integer checkTag(@PathVariable String tagCode) {
        return centerDeviceTagService.checkTag(tagCode);
    }

    /**
     * 绑定Tag<br/>
     *
     * @param tagBindDeviceCmd 绑定Tag参数
     */
    @PostMapping("/tag/bind")
    public void bindTag(@RequestBody TagBindDeviceCmd tagBindDeviceCmd) {

        centerDeviceTagService.bindTag(tagBindDeviceCmd);
    }

    /**
     * 设备绑定自定义标签
     *
     * @param bindTagCmd 操作命令
     */
    @PostMapping("/device/tag/bind")
    public void deviceBindTag(@RequestBody DeviceBindTagsCmd bindTagCmd) {
        centerDeviceTagService.deviceBindTag(bindTagCmd);
    }

    @GetMapping("/device/{tagCode}/queryDeviceIds")
    public List<Long> queryDeviceIdsByTagCode(@PathVariable String tagCode) {
        return centerDeviceTagService.queryDeviceIdsByTagCode(tagCode);
    }

    /**
     * 获取预置标签列表
     *
     * @return
     */
    @GetMapping("/tag/builtins")
    public Set<DeviceTagVO> getBuiltinDeviceTags() {
        return centerDeviceTagService.getBuiltinDeviceTags();
    }

    /**
     * 查询标签下设备
     *
     * @param tagCode
     * @param param   区域参数
     *
     * @return
     */
    @PostMapping("/device/{tagCode}/queryDeviceIds/by-area")
    public List<Long> queryDeviceIdsByTagCodeAndArea(@PathVariable("tagCode") String tagCode, @RequestBody QueryDeviceByAreaParam param) {
        return centerDeviceTagService.queryDeviceIdsByTagCodeAndArea(tagCode, param);
    }

    /**
     * 查询设备所有标签
     *
     * @param deviceIds 设备主键集合
     *
     * @return
     */
    @PostMapping("/device/all-tags")
    public Map<Long, List<CustomerTagVO>> deviceAllTag(@RequestBody Set<Long> deviceIds) {
        return centerDeviceTagService.deviceAllTag(deviceIds);
    }
}
