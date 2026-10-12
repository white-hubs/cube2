package com.guhecloud.cube.iot.mgt.north.controller;

import com.guhecloud.cube.iot.mgt.comm.util.Assert;
import com.guhecloud.cube.iot.mgt.core.data.msg.notify.RuleTriggerLog;
import com.guhecloud.cube.iot.mgt.core.data.vo.linkage.RuleExecutionDetail;
import com.guhecloud.cube.iot.mgt.north.param.ChangeRuleStateParam;
import com.guhecloud.cube.iot.mgt.north.param.LinkageLogPageParam;
import com.guhecloud.cube.iot.mgt.north.param.RuleCreateRequest;
import com.guhecloud.cube.iot.mgt.north.server.LinkConfigService;
import com.guhecloud.cube.iot.mgt.north.server.impl.LinkageLogService;
import com.guhecloud.cube.iot.mgt.north.vo.RuleCreateResponse;
import com.guhecloud.cube.iot.mgt.north.vo.RuleDetailVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Requester;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/north/linkage")
public class NorthLinkageController {

    @Resource
    private LinkConfigService linkConfigService;
    @Resource
    private LinkageLogService linkageLogService;

    /**
     * 新增联动
     *
     * @param ruleCreateRequest ruleCreateRequest
     *
     * @author Fl
     **/
    @PostMapping("/rule")
    public RuleCreateResponse ruleAdd(@RequestBody RuleCreateRequest ruleCreateRequest) {
        return linkConfigService.projectRuleAdd(ruleCreateRequest);
    }

    /**
     * 删除联动
     *
     * @param ruleNum ruleNum
     */
    @DeleteMapping("/rule/{ruleNum}")
    public void ruleDelete(@PathVariable("ruleNum") String ruleNum) {
        linkConfigService.ruleDelete(ruleNum);
    }

    /**
     * 批量删除联动
     *
     * @param ruleNums ruleNums
     */
    @DeleteMapping("/rule/batch")
    public void ruleDeleteBatch(@RequestParam("ruleNums") Set<String> ruleNums) {
        Assert.notEmpty(ruleNums, "ruleNums is empty");

        for (String ruleNum : ruleNums) {
            linkConfigService.ruleDelete(ruleNum);
        }
    }

    /**
     * 联动详情
     *
     * @param ruleNum ruleNum
     *
     * @return RuleDetailVo
     */
    @GetMapping("/rule/{ruleNum}")
    public RuleDetailVo ruleDetail(@PathVariable String ruleNum) {
        if (StringUtils.isBlank(ruleNum)) {
            throw new ServiceException("规则编号必填");
        }

        //按需实现
        return linkConfigService.ruleDetail(ruleNum);
    }

    /**
     * 批量改变联动状态
     *
     * @param changeRuleStateParam 需要改变状态的规则编码
     *
     * @return list 成功的ruleNums
     */
    @PostMapping("/rule/state/change")
    public List<String> changeRuleStateBatch(@RequestBody ChangeRuleStateParam changeRuleStateParam) {
        if (Objects.isNull(changeRuleStateParam.getRuleNums()) || changeRuleStateParam.getRuleNums().isEmpty()) {
            return Collections.emptyList();
        }

        List<String> succeed = new ArrayList<>();
        for (String ruleNum : changeRuleStateParam.getRuleNums()) {
            try {
                linkConfigService.changeRuleState(ruleNum, changeRuleStateParam.isEnabled());
                succeed.add(ruleNum);
            } catch (Exception e) {
                log.error("disable rule error", e);
            }
        }

        return succeed;
    }

    @GetMapping("rule/{ruleNum}/event-log")
    public Pagination<RuleTriggerLog> linkEventList(@PathVariable("ruleNum") String ruleNum, @SpringQueryMap LinkageLogPageParam param) {
        Assert.notNull(param, "param is null");

        param.setRuleNum(ruleNum);
        param.setTenantId(SessionContextHolder.getRequesterProperty(Requester::getTenant));
        return linkageLogService.linkEventHistoryLogList(param);
    }

    @GetMapping("/rule/{ruleNum}/event-log/{instanceId}")
    public RuleExecutionDetail linkEventDetails(@PathVariable String instanceId, @PathVariable String ruleNum) {

        return linkageLogService.linkEventDetails(instanceId, ruleNum);
    }
}