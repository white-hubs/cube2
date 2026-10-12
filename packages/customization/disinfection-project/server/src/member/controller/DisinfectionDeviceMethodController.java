package com.guhecloud.cube.srv.gen.disinfection.server.member.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ManualControlParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskCreateParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.ScheduledTaskUpdateParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberGroupDeviceService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.service.DisinfectionMemberScheduledTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ManualControlDurationSettingResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ScheduledTaskDetailResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.ScheduledTaskPageResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 设备方法控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DisinfectionDeviceMethodController {

    private final DisinfectionMemberGroupDeviceService disinfectionMemberGroupDeviceService;
    private final DisinfectionMemberScheduledTaskService disinfectionMemberScheduledTaskService;

    /**
     * 手动控制消杀设备（按设备 id、指令枚举及可选时长下发）
     *
     * @param param 设备 id、设备名称、指令枚举（驱蚊指令/雾灌指令/设备关闭/定时任务）、时长（驱蚊或雾灌时必填，单位秒）
     *
     * @return 是否成功
     */
    @PostMapping("/bus/member/device/manual-control")
    public Boolean manualControl(@RequestBody @Valid ManualControlParam param) {
        return disinfectionMemberGroupDeviceService.manualControl(param);
    }

    /**
     * 查询当前用户对设备手动控制指令的最新时长设置
     *
     * @param deviceId 设备 id
     *
     * @return 指令时长设置
     */
    @GetMapping("/bus/member/device/manual-control-duration")
    public List<ManualControlDurationSettingResult> getManualControlDurationSettings(@RequestParam Long deviceId) {
        return disinfectionMemberGroupDeviceService.getManualControlDurationSettings(deviceId);
    }

    /**
     * 创建定时任务
     *
     * @param param 任务名称、定时任务模式（枚举）、关联的设备id列表、定时任务计划列表（指令枚举、执行开始时间与结束时间）
     *
     * @return 定时任务主键 id
     */
    @PostMapping("/bus/member/device/scheduled-task/create")
    public Long createScheduledTask(@RequestBody @Valid ScheduledTaskCreateParam param) {
        return disinfectionMemberScheduledTaskService.createScheduledTask(param);
    }

    /**
     * 修改定时任务
     */
    @PutMapping("/bus/member/device/scheduled-task/update")
    public Boolean updateScheduledTask(@RequestBody @Valid ScheduledTaskUpdateParam param) {
        return disinfectionMemberScheduledTaskService.updateScheduledTask(param);
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/bus/member/device/scheduled-task/{id}")
    public Boolean deleteScheduledTask(@PathVariable Long id) {
        return disinfectionMemberScheduledTaskService.deleteScheduledTask(id);
    }

    /**
     * 获取定时任务详情
     */
    @GetMapping("/bus/member/device/scheduled-task/{id}")
    public ScheduledTaskDetailResult getScheduledTaskDetail(@PathVariable Long id) {
        return disinfectionMemberScheduledTaskService.getScheduledTaskDetail(id);
    }

    /**
     * 定时任务分页列表
     */
    @GetMapping("/bus/member/device/scheduled-task/page")
    public Pagination<ScheduledTaskPageResult> scheduledTaskPage(@Valid @ModelAttribute ScheduledTaskQueryParam param) {
        return disinfectionMemberScheduledTaskService.scheduledTaskPage(param);
    }
}
