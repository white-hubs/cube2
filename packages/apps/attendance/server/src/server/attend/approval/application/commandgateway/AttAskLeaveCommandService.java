package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttAskLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttAskLeaveQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain.AttApprovalDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.repository.AttDefaultHolidayBalanceRepository;
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
 * 请假操作服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttAskLeaveCommandService {

    private final AttAskLeaveQueryService attAskLeaveQueryService;
    private final FlowClient flowClient;
    private final AttDefaultHolidayBalanceRepository attDefaultHolidayBalanceRepository;
    private final AttAskLeaveFromMapper attAskLeaveFromMapper;
    private final UserClient userClient;

    /**
     * 提交请假申请
     */
    public String submitAskLeaveApply(SubmitInitFormCommand formCommand) {
        FormDataParam p = new FormDataParam(formCommand.getFormDefinitionId(), formCommand.getData());
        String fromString = flowClient.formParse(p);
        AttAskLeaveFromDTO fromDTO = JsonUtils.deserialize(fromString, AttAskLeaveFromDTO.class);
        if (fromDTO == null) {
            throw new ServiceException("表单解析失败！");
        }
        Long fromId = IdWorker.getId();
        fromDTO.setId(fromId);
        fromDTO.setStatus(FlowResult.ACTIVE.name());
        // 校验
        AttHolidayRulePO holidayRulePO = attAskLeaveQueryService.askLeaveCheck(fromDTO);
        UserDTO userDTO = userClient.getUser(fromDTO.getApplicantId());
        if (userDTO != null) {
            fromDTO.setApplicantName(userDTO.getName());
            UserDTO.Organization organization = userDTO.getOrganization();
            if (organization != null) {
                fromDTO.setDeptName(organization.getName());
            }
        }
        if (fromDTO.getHolidayBalance() >= AttConstants.zeroInt) { // 不限额为余额为-1，余额大于
            // 预扣
            fromDTO.setDeadlineIds(attDefaultHolidayBalanceRepository.occupyBalance(fromDTO));
        }
        // 记录表单
        AttAskLeaveFromPO leaveFromPO = new AttAskLeaveFromPO();
        BeanUtils.copyProperties(fromDTO, leaveFromPO);
        long leaveNumber = AttHolidayDomain.unitAndNumToLong(fromDTO.getLeaveNumber(), AttConstants.HOUR, null);
        leaveFromPO.setLeaveNumber(leaveNumber);
        leaveFromPO.setIntoAbsence(holidayRulePO.getIntoAbsence());
        leaveFromPO.setHolidayDayOfHour(holidayRulePO.getDayOfHour());
        // 不限额余额为"-1";
        long balanceNumber = fromDTO.getHolidayBalance().longValue();
        if (fromDTO.getHolidayBalance() > AttConstants.zeroInt) {
            balanceNumber = AttHolidayDomain.unitAndNumToLong(fromDTO.getHolidayBalance(), AttConstants.HOUR, null);
        }
        leaveFromPO.setHolidayBalance(balanceNumber);
        AttPoAttributeSet.initAddBasePoAttribute(leaveFromPO);
        attAskLeaveFromMapper.insert(leaveFromPO);
        // 提交
        JSONObject oldJson = JsonUtils.deserialize(formCommand.getData(), JSONObject.class);
        JSONObject newJson = JsonUtils.deserialize(fromDTO, JSONObject.class);
        formCommand.setData(JsonUtils.serialize(AttApprovalDomain.jsonMerge(newJson, oldJson)));
        String processInstanceId = flowClient.submitFirst(formCommand);
        // 将实例id添加到表单
        attAskLeaveFromMapper.update(null, Wrappers.<AttAskLeaveFromPO>lambdaUpdate()
                                             .eq(AttAskLeaveFromPO::getId, fromId)
                                             .set(AttAskLeaveFromPO::getProcessInstanceId, processInstanceId)
                                    );
        return processInstanceId;
    }

}
