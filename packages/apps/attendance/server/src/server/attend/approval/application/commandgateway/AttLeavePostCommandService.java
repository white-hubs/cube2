package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttLeavePostFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttLeavePostQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain.AttApprovalDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.leavepost.infrastructure.po.AttLeavePostRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.JsonUtils;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 离岗审批操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttLeavePostCommandService {

    private final FlowClient flowClient;
    private final AttLeavePostQueryService attLeavePostQueryService;
    private final AttLeavePostFromMapper attLeavePostFromMapper;
    private final UserClient userClient;

    /**
     * 提交离岗申请
     */
    public String submitLeavePostApply(SubmitInitFormCommand formCommand) {
        FormDataParam p = new FormDataParam(formCommand.getFormDefinitionId(), formCommand.getData());
        String fromString = flowClient.formParse(p);
        AttLeavePostFromDTO fromDTO = JsonUtils.deserialize(fromString, AttLeavePostFromDTO.class);
        if (fromDTO == null) {
            throw new ServiceException("表单解析失败！");
        }
        Long fromId = IdWorker.getId();
        fromDTO.setId(fromId);
        fromDTO.setStatus(FlowResult.ACTIVE.name());
        // 校验
        AttLeavePostRulePO postRule = attLeavePostQueryService.leavePostCheck(fromDTO);
        fromDTO.setPostRuleId(postRule.getId());
        fromDTO.setPostRuleName(postRule.getName());
        UserDTO userDTO = userClient.getUser(fromDTO.getApplicantId());
        if (userDTO != null) {
            fromDTO.setApplicantName(userDTO.getName());
            UserDTO.Organization organization = userDTO.getOrganization();
            if (organization != null) {
                fromDTO.setDeptName(organization.getName());
            }
        }
        // 记录表单
        AttLeavePostFromPO postFromPO = new AttLeavePostFromPO();
        BeanUtils.copyProperties(fromDTO, postFromPO);
        long postNumber = AttHolidayDomain.unitAndNumToLong(fromDTO.getPostNumber(), AttConstants.HOUR, null);
        postFromPO.setPostNumber(postNumber);
        postFromPO.setPostRuleId(postRule.getId());
        postFromPO.setPostRuleName(postRule.getName());

        AttPoAttributeSet.initAddBasePoAttribute(postFromPO);
        attLeavePostFromMapper.insert(postFromPO);
        // 提交
        JSONObject oldJson = JsonUtils.deserialize(formCommand.getData(), JSONObject.class);
        JSONObject newJson = JsonUtils.deserialize(fromDTO, JSONObject.class);
        formCommand.setData(JsonUtils.serialize(AttApprovalDomain.jsonMerge(newJson, oldJson)));
        String processInstanceId = flowClient.submitFirst(formCommand);
        // 将实例id添加到表单
        attLeavePostFromMapper.update(null, Wrappers.<AttLeavePostFromPO>lambdaUpdate()
                                              .eq(AttLeavePostFromPO::getId, fromId)
                                              .set(AttLeavePostFromPO::getProcessInstanceId, processInstanceId)
                                     );
        return processInstanceId;
    }

}
