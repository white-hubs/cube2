package com.guhecloud.cube.iot.mgt.north.server;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.iot.mgt.north.model.LinkConfig;
import com.guhecloud.cube.iot.mgt.north.param.RuleCreateRequest;
import com.guhecloud.cube.iot.mgt.north.vo.RuleCreateResponse;
import com.guhecloud.cube.iot.mgt.north.vo.RuleDetailVo;

public interface LinkConfigService extends IService<LinkConfig> {


    RuleCreateResponse projectRuleAdd(RuleCreateRequest ruleCreateRequest);

    void ruleDelete(String ruleNum);

    /**
     * 规则详情
     *
     * @param ruleNum ruleNum
     * @return RuleDetailVo
     */
    RuleDetailVo ruleDetail(String ruleNum);

    /**
     * 规则禁用
     *
     * @param ruleNum 规则
     */
    void changeRuleState(String ruleNum, boolean enable);
}
