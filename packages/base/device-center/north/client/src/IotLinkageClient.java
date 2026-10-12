package com.guhecloud.cube.iot.north.bridge.client;

import com.guhecloud.cube.iot.north.bridge.client.pj.dto.RuleCreateResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.RuleDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.RuleExecutionDetailDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.RuleTriggerLogDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.ChangeRuleStateParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.LinkageLogPageParam;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

import static com.guhecloud.cube.iot.north.bridge.client.Profile.CLIENT_NAME;

@FeignClient(name = CLIENT_NAME)
public interface IotLinkageClient {

    /**
     * 新增联动
     *
     * @param ruleCreateRequest ruleCreateRequest
     *
     * @author Fl
     **/
    @PostMapping("/north/linkage/rule")
    public RuleCreateResponseDTO ruleAdd(@RequestBody RuleCreateParam ruleCreateRequest);

    /**
     * 删除联动
     *
     * @param ruleNum ruleNum
     */
    @DeleteMapping("/north/linkage/rule/{ruleNum}")
    public void ruleDelete(@PathVariable("ruleNum") String ruleNum);

    /**
     * 批量删除联动
     *
     * @param ruleNums ruleNums
     */
    @DeleteMapping("/north/linkage/rule/batch")
    public void ruleDeleteBatch(@RequestParam("ruleNums") Set<String> ruleNums);

    /**
     * 联动详情
     *
     * @param ruleNum ruleNum
     *
     * @return RuleDetailVo
     */
    @GetMapping("/north/linkage/rule/{ruleNum}")
    public RuleDetailDTO ruleDetail(@PathVariable("ruleNum") String ruleNum);

    /**
     * 批量改变联动状态
     *
     * @param changeRuleStateParam 需要改变状态的规则编码
     *
     * @return list 成功的ruleNums
     */
    @PostMapping("/north/linkage/rule/state/change")
    public List<String> changeRuleStateBatch(@RequestBody ChangeRuleStateParam changeRuleStateParam);

    @GetMapping("/north/linkage/rule/{ruleNum}/event-log")
    public Pagination<RuleTriggerLogDTO> linkEventList(@PathVariable("ruleNum") String ruleNum, @SpringQueryMap LinkageLogPageParam param);

    @GetMapping("/north/linkage/rule/{ruleNum}/event-log/{instanceId}")
    public RuleExecutionDetailDTO linkEventDetails(@PathVariable("ruleNum") String instanceId, @PathVariable("instanceId") String ruleNum);
}
