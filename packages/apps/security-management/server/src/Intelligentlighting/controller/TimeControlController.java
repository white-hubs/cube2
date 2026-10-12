package com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.enumeration.DeviceTypeEnum;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlInfoParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.param.TimeControlInfoQueryParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.service.TimeControlInfoService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.DeviceTypeEnumVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.Intelligentlighting.vo.TimeControlInfoVo;
import com.guhecloud.platform.base.model.Pagination;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TimeControlController {
    private final TimeControlInfoService timeControlInfoService;

    /**
     * @Description 时控新增
     * @Author lrj
     */
    @PostMapping("/time-control")
    public void addTimeControl(@RequestBody TimeControlInfoParam param) {
        timeControlInfoService.addTimeControl(param);
    }

    /**
     * @Description 时控编辑
     * @Author lrj
     */
    @PutMapping("/time-control/{id}")
    public void updateTimeControl(@PathVariable Long id, @RequestBody TimeControlInfoParam param) {
        timeControlInfoService.updateTimeControl(id, param);
    }

    /**
     * @Description 时控删除
     * @Author lrj
     */
    @DeleteMapping("/time-control/{id}")
    public void deleteTimeControl(@PathVariable Long id) {
        timeControlInfoService.deleteTimeControl(id);
    }

    /**
     * @Description 时控详情
     * @Author lrj
     */
    @GetMapping("/time-control/{id}")
    public TimeControlInfoVo getTimeControlInfo(@PathVariable Long id) {
        return timeControlInfoService.getTimeControlInfo(id);
    }

    /**
     * @Description 时控分页
     * @Author lrj
     */
    @GetMapping("/time-controls")
    public Pagination<TimeControlInfoVo> getTimeControls(TimeControlInfoQueryParam param) {
        return timeControlInfoService.getTimeControls(param);
    }

    /**
     * @Description 类型枚举列表
     * @Author lrj
     */
    @GetMapping("/object-type")
    public List<DeviceTypeEnumVo> getObjectType() {
        return DeviceTypeEnum.getEnumTypeVo();
    }

}
