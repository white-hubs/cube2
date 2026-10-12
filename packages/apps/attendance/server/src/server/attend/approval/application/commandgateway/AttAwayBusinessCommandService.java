package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAwayBusinessFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttAwayBusinessQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain.AttApprovalDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAwayBusinessFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.awaybusiness.infrastructure.po.AttAwayBusinessRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 出差操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAwayBusinessCommandService extends ServiceImpl<AttAwayBusinessFromMapper, AttAwayBusinessFromPO> {

    private final UserClient userClient;
    private final FlowClient flowClient;
    private final AttAwayBusinessQueryService attAwayBusinessQueryService;
    private final AttAwayBusinessFromMapper attAwayBusinessFromMapper;

    /**
     * 提交出差申请
     */
    public String submitAwayBusinessApply(SubmitInitFormCommand formCommand) {
        FormDataParam p = new FormDataParam(formCommand.getFormDefinitionId(), formCommand.getData());
        String fromString = flowClient.formParse(p);
        AttAwayBusinessFromDTO fromDTO = JsonUtils.deserialize(fromString, AttAwayBusinessFromDTO.class);
        Assert.isNotAcceptable(ObjectUtils.isNull(fromDTO), "表单解析失败！");
        Long fromId = IdWorker.getId();
        fromDTO.setId(fromId);
        fromDTO.setStatus(FlowResult.ACTIVE.name());
        // 校验
        AttAwayBusinessRulePO awayRule = attAwayBusinessQueryService.awayBusinessCheck(fromDTO);
        Assert.isNotAcceptable(awayRule.getId() == null, "请配置出差规则！");
        fromDTO.setAwayRuleId(awayRule.getId());
        fromDTO.setAwayRuleName(awayRule.getName());
        UserDTO userDTO = userClient.getUser(fromDTO.getApplicantId());
        if (userDTO != null) {
            fromDTO.setApplicantName(userDTO.getName());
            UserDTO.Organization organization = userDTO.getOrganization();
            if (organization != null) {
                fromDTO.setDeptName(organization.getName());
            }
        }
        // 记录表单
        AttAwayBusinessFromPO awayFromPO = new AttAwayBusinessFromPO();
        BeanUtils.copyProperties(fromDTO, awayFromPO);
        long postNumber = AttHolidayDomain.unitAndNumToLong(fromDTO.getAwayNumber(), AttConstants.HOUR, null);
        awayFromPO.setAwayNumber(postNumber);
        awayFromPO.setAwayRuleId(awayRule.getId());
        awayFromPO.setAwayRuleName(awayRule.getName());

        AttPoAttributeSet.initAddBasePoAttribute(awayFromPO);
        attAwayBusinessFromMapper.insert(awayFromPO);
        // 提交
        JSONObject oldJson = JsonUtils.deserialize(formCommand.getData(), JSONObject.class);
        JSONObject newJson = JsonUtils.deserialize(fromDTO, JSONObject.class);
        formCommand.setData(JsonUtils.serialize(AttApprovalDomain.jsonMerge(newJson, oldJson)));
        String processInstanceId = flowClient.submitFirst(formCommand);
        // 将实例id添加到表单
        attAwayBusinessFromMapper.update(null, Wrappers.<AttAwayBusinessFromPO>lambdaUpdate()
                                                 .eq(AttAwayBusinessFromPO::getId, fromId)
                                                 .set(AttAwayBusinessFromPO::getProcessInstanceId, processInstanceId)
                                        );
        return processInstanceId;
    }

}
