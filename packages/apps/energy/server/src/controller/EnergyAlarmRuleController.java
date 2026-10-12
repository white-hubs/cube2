package com.guhecloud.cube.srv.gen.energymanagement.server.controller;

import com.guhecloud.cube.srv.gen.energymanagement.server.param.EnergyAlarmRuleParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyAlarmRuleQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.EnergyAlarmRuleService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyAlarmRuleVO;
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
 * @Description 报警规则设备绑定
 * @date 2022-07-07 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnergyAlarmRuleController {
    private final EnergyAlarmRuleService energyAlarmRuleService;

    /**
     * 功能描述： 报警规则设备绑定添加
     */
    @PostMapping(value = "/energy-alarm-rule")
    public Long add(@Valid @RequestBody EnergyAlarmRuleParam param) {
        return energyAlarmRuleService.add(param);
    }

    /**
     * 功能描述： 报警规则设备绑定修改
     */
    @PutMapping(value = "/energy-alarm-rule/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EnergyAlarmRuleParam param) {
        energyAlarmRuleService.update(id, param);
    }

    /**
     * 功能描述： 报警规则设备绑定删除
     */
    @DeleteMapping(value = "/energy-alarm-rule/{id}")
    public void delete(@PathVariable Long id) {
        energyAlarmRuleService.delete(id);
    }

    /**
     * 功能描述： 报警规则设备绑定批量删除
     */
    @DeleteMapping(value = "/energy-alarm-rule/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        energyAlarmRuleService.batchDelete(ids);
    }

    /**
     * 功能描述： 报警规则设备绑定分页
     */
    @GetMapping(value = "/energy-alarm-rule/page")
    public Pagination<EnergyAlarmRuleVO> getPage(EnergyAlarmRuleQueryParam param) {
        return energyAlarmRuleService.getPage(param);
    }

    /**
     * 功能描述： 报警规则设备绑定详情
     */
    @GetMapping(value = "/energy-alarm-rule/{id}")
    public EnergyAlarmRuleVO getById(@PathVariable Long id) {
        return energyAlarmRuleService.getById(id);
    }

}