package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.base.alarm.client.dto.SatAlarmLevelDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterAlarmService;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.AlarmRulesParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.AlarmRulesQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.AlarmRulesService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AlarmRulesVO;
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
 * @author lrj
 * @Description 报警规则配置
 * @date 2021-12-30 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AlarmRulesController {
    private final AlarmRulesService alarmRulesService;

    private final AdapterAlarmService adapterAlarmService;

    /**
     * 功能描述： 报警规则配置添加
     */
    @PostMapping(value = "/alarm-rules")
    public Long add(@Valid @RequestBody AlarmRulesParam param) {
        return alarmRulesService.add(param);
    }

    /**
     * 功能描述： 报警规则配置修改
     */
    @PutMapping(value = "/alarm-rules/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody AlarmRulesParam param) {
        alarmRulesService.update(id, param);
    }

    /**
     * 功能描述： 报警规则配置修改
     */
    @PutMapping(value = "/alarm-rules/updateStatus/{id}/{status}")
    public void updateStatus(@PathVariable Long id, @PathVariable("status") Boolean status) {
        alarmRulesService.updateStatus(id, status);
    }

    /**
     * 功能描述： 报警规则配置删除
     */
    @DeleteMapping(value = "/alarm-rules/{id}")
    public void delete(@PathVariable Long id) {
        alarmRulesService.delete(id);
    }

    /**
     * 功能描述： 报警规则配置批量删除
     */
    @DeleteMapping(value = "/alarm-rules/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        alarmRulesService.batchDelete(ids);
    }

    /**
     * 功能描述： 报警规则配置分页
     */
    @GetMapping(value = "/alarm-rules/page")
    public Pagination<AlarmRulesVO> getPage(AlarmRulesQueryParam param) {
        return alarmRulesService.getPage(param);
    }

    /**
     * 功能描述： 报警规则配置详情
     */
    @GetMapping(value = "/alarm-rules/{id}")
    public AlarmRulesVO getById(@PathVariable Long id) {
        return alarmRulesService.getById(id);
    }

    /**
     * @Description 能耗报警等级
     * @Author lrj
     */
    @GetMapping("/alarm-levels")
    public List<SatAlarmLevelDTO> listAlarmLevels() {
        return adapterAlarmService.listAlarmLevels();
    }
}