package com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.param.RuleDeviceConditionQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.service.RuleDeviceConditionService;
import com.guhecloud.cube.biz.linkage.server.rule.strategy.input.devicecondition.vo.RuleDeviceConditionPageVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleDeviceConditionController {

    private final RuleDeviceConditionService ruleDeviceConditionService;

    private final UserClient userClient;

    /**
     * 条件规则分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/rule-device-conditions")
    public Pagination<RuleDeviceConditionPageVO> page(RuleDeviceConditionQueryParam param) {
        Page<RuleDeviceConditionPageVO> page = this.ruleDeviceConditionService.page(param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        page.getRecords().stream().forEach(ruleDeviceConditionPageVO -> {
            UserDTO userDTO;
            if (StringUtils.isNotBlank(ruleDeviceConditionPageVO.getCreatorId()) &&
                ActorTypeEnum.USER.name().equals(ruleDeviceConditionPageVO.getCreatorType())) {
                userDTO = userClient.getUser(Long.valueOf(ruleDeviceConditionPageVO.getCreatorId()));
            } else {
                userDTO = this.userClient.getUserByUserName(ruleDeviceConditionPageVO.getCreateBy());
            }
            if (Objects.nonNull(userDTO)) {
                ruleDeviceConditionPageVO.setCreateByName(userDTO.getName());
            }
        });
        return PagingUtils.convert(page);
    }
}
