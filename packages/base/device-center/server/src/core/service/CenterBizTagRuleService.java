package com.guhecloud.cube.base.device.center.core.service;

import com.guhecloud.cube.base.device.center.core.mapper.CenterBizTagRuleMapper;
import com.guhecloud.cube.base.device.center.core.model.BizTagRule;
import com.guhecloud.cube.base.device.center.core.param.CreateOrUpdateTagRuleParam;
import com.guhecloud.cube.base.device.center.metadata.std.param.CreateTagCmd;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CenterBizTagRuleService {

    @Resource
    private CenterBizTagRuleMapper centerBizTagRuleMapper;

    @Resource
    private CenterDeviceTagService centerDeviceTagService;

    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdateTagRule(CreateOrUpdateTagRuleParam param) {
        CreateTagCmd createTagCmd = CreateTagCmd.createTopTag(null, param.getCode(), param.getName());
        centerDeviceTagService.createTagIfNeed(createTagCmd);
        Optional<BizTagRule> tagRuleOptional = getTagRule(param.getCode());
        BizTagRule tagRule;
        if (tagRuleOptional.isPresent()) {
            tagRule = tagRuleOptional.get();
            tagRule.setRule(param.getRule());
            centerBizTagRuleMapper.updateById(tagRule);
        } else {
            tagRule = new BizTagRule();
            tagRule.setTagCode(param.getCode());
            tagRule.setRule(param.getRule());
            centerBizTagRuleMapper.insert(tagRule);
        }
        return tagRule.getId();
    }

    public Optional<BizTagRule> getTagRule(String code) {
        BizTagRule tagRule = centerBizTagRuleMapper.getCenterBizTagRule(code);
        return Optional.ofNullable(tagRule);
    }

    public List<BizTagRule> getAll() {
        return centerBizTagRuleMapper.selectList(null);
    }

    public void removeTagRule(String code) {
        Optional<BizTagRule> tagRuleOptional = getTagRule(code);
        tagRuleOptional
                .ifPresent(tagRule -> DatabaseContextHolder.softDelete(BizTagRule.class, tagRule));
    }
}
