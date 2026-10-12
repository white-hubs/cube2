package com.guhecloud.cube.biz.emergency.server.rule.interfaces;

import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.EmergencyRuleCommandService;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.ChangeEmergencyRuleEnabledCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.DeleteEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.commandgateway.command.IncreaseEmergencyRuleCommand;
import com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.EmergencyRuleQueryService;
import com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.query.EmergencyRuleApplicationQuery;
import com.guhecloud.cube.biz.emergency.server.rule.application.querygateway.query.EmergencyRuleCollectionQuery;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleCollectionVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleDetailVO;
import com.guhecloud.cube.biz.emergency.server.rule.interfaces.vo.EmergencyRuleSafetyReportVO;
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

import jakarta.validation.Valid;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 预案规则接口管理
 *
 * @author lanchun
 * @date 2022/9/5 10:46
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyRuleController {

    private final EmergencyRuleCommandService emergencyRuleCommandService;

    private final EmergencyRuleQueryService emergencyRuleQueryService;

    /**
     * 预案规则分页列表
     *
     * @param emergencyRuleCollectionQuery
     *
     * @return
     */
    @GetMapping("/emergency-rule/list-page")
    public Pagination<EmergencyRuleCollectionVO> emergencyRuleCollection(EmergencyRuleCollectionQuery emergencyRuleCollectionQuery) {
        return this.emergencyRuleQueryService.listPage(emergencyRuleCollectionQuery);
    }

    /**
     * 预案规则详情
     *
     * @param emergencyRuleId
     *
     * @return
     */
    @GetMapping("/emergency-rule/{emergencyRuleId}")
    public EmergencyRuleDetailVO emergencyRuleDetail(@PathVariable("emergencyRuleId") Long emergencyRuleId) {
        return this.emergencyRuleQueryService.getDetail(emergencyRuleId);
    }

    /**
     * 预案规则新增
     *
     * @param increaseEmergencyRuleCommand
     */
    @PostMapping("/emergency-rule")
    public void increase(@Valid @RequestBody IncreaseEmergencyRuleCommand increaseEmergencyRuleCommand) {
        this.emergencyRuleCommandService.increaseEmergencyRule(increaseEmergencyRuleCommand);
    }

    /**
     * 预案规则修改
     *
     * @param changeEmergencyRuleCommand
     */
    @PutMapping("/emergency-rule")
    public void change(@Valid @RequestBody ChangeEmergencyRuleCommand changeEmergencyRuleCommand) {
        this.emergencyRuleCommandService.changeEmergencyRule(changeEmergencyRuleCommand);
    }

    /**
     * 预案规则修改启用状态
     *
     * @param changeEmergencyRuleEnabledCommand
     */
    @PutMapping("/emergency-rule/enabled")
    public void changeEnabled(@Valid @RequestBody ChangeEmergencyRuleEnabledCommand changeEmergencyRuleEnabledCommand) {
        this.emergencyRuleCommandService.changeEmergencyRuleEnabled(changeEmergencyRuleEnabledCommand);
    }

    /**
     * 预案规则删除
     *
     * @param id
     */
    @DeleteMapping("/emergency-rule/delete/{id}")
    public void delete(@PathVariable("id") Long id) {
        DeleteEmergencyRuleCommand deleteEmergencyRuleCommand = new DeleteEmergencyRuleCommand(Collections.singleton(id));
        this.emergencyRuleCommandService.deleteEmergencyRule(deleteEmergencyRuleCommand);
    }

    /**
     * 预案规则批量删除
     *
     * @param ids
     */
    @DeleteMapping("/emergency-rule/delete/batch/{ids}")
    public void deleteBatch(@PathVariable("ids") Set<Long> ids) {
        DeleteEmergencyRuleCommand deleteEmergencyRuleCommand = new DeleteEmergencyRuleCommand(ids);
        this.emergencyRuleCommandService.deleteEmergencyRule(deleteEmergencyRuleCommand);
    }

    /**
     * 根据指定报警获取预案规则
     *
     * @param emergencyRuleApplicationQuery
     *
     * @return
     */
    @PostMapping("/emergency-rule/safety-report")
    public List<EmergencyRuleSafetyReportVO> getEmergencyRuleByAlarmAndApplication(
            @RequestBody EmergencyRuleApplicationQuery emergencyRuleApplicationQuery
                                                                                  ) {
        return this.emergencyRuleQueryService.emergencyRuleByAlarmAndApplication(emergencyRuleApplicationQuery);

    }
}
