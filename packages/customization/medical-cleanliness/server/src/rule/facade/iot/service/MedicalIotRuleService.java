package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.service;

import com.guhecloud.cube.iot.north.bridge.client.IotLinkageClient;
import com.guhecloud.cube.iot.north.bridge.client.pj.dto.RuleCreateResponseDTO;
import com.guhecloud.cube.iot.north.bridge.client.pj.param.RuleCreateParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.facade.iot.factory.RuleDefinitionFactory;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalIotRuleService {

    private final IotLinkageClient iotLinkageClient;
    private final MedicalRuleService medicalRuleService;

    /**
     * 规则下发保存到IOT
     *
     * @param ruleVO
     * @param delayTime
     *
     * @return
     */
    public RuleCreateResponseDTO addIotRule(RuleVO ruleVO, Integer delayTime) {
        this.deleteIotRule(ruleVO.getIotRuleId());
        ruleVO.setIotRuleId(IdUtils.next());
        RuleParam ruleParam = ConvertUtils.convert(RuleParam.class, ruleVO);
        this.medicalRuleService.updateByIotRuleId(ruleParam);
        RuleCreateParam ruleCreateParam = RuleDefinitionFactory.buildRuleCreateParam(ruleVO, delayTime);
        return this.iotLinkageClient.ruleAdd(ruleCreateParam);
    }

    /**
     * 修改规则下发保存到IOT
     *
     * @param ruleVO
     * @param delayTime
     *
     * @return
     */
    public RuleCreateResponseDTO updateIotRule(RuleVO ruleVO, Integer delayTime) {
        this.deleteIotRule(ruleVO.getIotRuleId());
        return this.addIotRule(ruleVO, delayTime);
    }

    /**
     * 删除规则IOT
     *
     * @param id
     */
    public void deleteIotRule(Long id) {
        if (ObjectUtils.isNotEmpty(id)) {
            this.iotLinkageClient.ruleDelete(id.toString());
        }
    }
}
