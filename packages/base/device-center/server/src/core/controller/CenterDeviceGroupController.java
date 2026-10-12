package com.guhecloud.cube.base.device.center.core.controller;

import com.guhecloud.cube.base.device.center.core.model.BizGroup;
import com.guhecloud.cube.base.device.center.core.service.CenterDeviceGroupService;
import com.guhecloud.cube.base.device.center.metadata.event.DeviceGroupChange;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateGroupCommand;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagBizGroupCommand;
import com.guhecloud.cube.base.device.center.metadata.std.vo.DeviceGroupVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CenterDeviceGroupController {

    private final CenterDeviceGroupService centerDeviceGroupService;

    /**
     * 设备分组新增
     *
     * @param groupCommand 新增参数
     *
     * @return 分组主键
     */
    @PostMapping("/device-groups")
    public Long createGroup(@Valid @RequestBody CreateGroupCommand groupCommand) {
        return centerDeviceGroupService.createGroup(groupCommand);
    }

    /**
     * 标签下设备分组新增
     *
     * @param command 新增参数
     *
     * @return 分组主键
     */
    @PostMapping("/tags/device-groups")
    public Long createGroupByTag(@Valid @RequestBody CreateTagBizGroupCommand command) {
        return centerDeviceGroupService.createGroupByTag(command);
    }

    /**
     * 修改设备分组
     *
     * @param id      分组主键
     * @param command 修改参数
     *
     * @return 分组主键
     */
    @PutMapping("/device-groups/{id}")
    public Long update(@PathVariable("id") Long id, @Valid @RequestBody CreateGroupCommand command) {
        var groupId = centerDeviceGroupService.update(id, command);
        RocketMQContextHolder.send(DeviceGroupChange.TOPIC, new DeviceGroupChange(groupId));
        return groupId;
    }

    /**
     * 设备分组详情
     *
     * @param id 分组主键
     *
     * @return DeviceGroupVO
     */
    @GetMapping("/device-groups/{id}")
    public DeviceGroupVO getGroup(@PathVariable("id") Long id) {
        BizGroup group = this.centerDeviceGroupService.getGroup(id);
        Assert.isNotFound(group, "分组不存在或已被删除");
        return ObjectUtils.copying(group, DeviceGroupVO.class);
    }

    /**
     * 设备分组删除
     *
     * @param id 分组主键
     */
    @DeleteMapping("/device-groups/{id}")
    public void removeById(@PathVariable("id") Long id) {
        this.centerDeviceGroupService.removeById(id);
    }

    /**
     * 设备分组批量删除
     *
     * @param ids 分组集合
     */
    @PostMapping("/device-groups/batch-delete")
    public void removeByIds(@RequestBody Set<Long> ids) {
        this.centerDeviceGroupService.removeByIds(ids);
    }

    /**
     * 设备分组下的设备批量删除
     *
     * @param id        分组id
     * @param deviceIds 设备集合
     */
    @PostMapping("/device-groups/{id}/devices/batch-delete")
    public void removeGroupRefDevice(@PathVariable("id") Long id, @RequestBody Set<Long> deviceIds) {
        this.centerDeviceGroupService.removeGroupRef(id, deviceIds);
    }
}
