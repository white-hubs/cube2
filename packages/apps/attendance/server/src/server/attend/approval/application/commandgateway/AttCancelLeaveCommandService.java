package com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.commandgateway;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.dto.AttCancelLeaveFromDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.application.querygateway.AttCancelLeaveQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.domain.AttApprovalDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveLedgerMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttCancelLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttCancelLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.AttHolidayDomain;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.dto.AttHolidayBalanceMutationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.domain.repository.AttHolidayBalanceRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayBalanceDeadlineMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayBalanceDeadlinePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ApprovalBizEnum;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import instance.command.SubmitInitFormCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销假服务
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttCancelLeaveCommandService {

    private final AttHolidayBalanceDeadlineMapper attHolidayBalanceDeadlineMapper;
    private final AttCancelLeaveQueryService attCancelLeaveQueryService;
    private final AttCancelLeaveFromMapper attCancelLeaveFromMapper;
    private final AttAskLeaveFromMapper attAskLeaveFromMapper;
    private final AttAskLeaveLedgerMapper attAskLeaveLedgerMapper;
    private final AttHolidayBalanceRepository attHolidayBalanceRepository;
    private final FlowClient flowClient;
    private final UserClient userClient;
    private final AttApproveLogMapper attApproveLogMapper;

    /**
     * 发起销假审批
     *
     * @param formCommand 表单对象
     */
    public String submitCancelLeave(SubmitInitFormCommand formCommand) {
        FormDataParam p = new FormDataParam(formCommand.getFormDefinitionId(), formCommand.getData());
        String fromString = flowClient.formParse(p);
        AttCancelLeaveFromDTO fromDTO = JsonUtils.deserialize(fromString, AttCancelLeaveFromDTO.class);
        // 查询请假单信息
        AttAskLeaveFromPO askLeaveFromPO = attAskLeaveFromMapper.selectById(fromDTO.getAskLeaveFromId());
        // 校验
        attCancelLeaveQueryService.cancelLeaveCheck(fromDTO, askLeaveFromPO);
        UserDTO userDTO = userClient.getUser(fromDTO.getApplicantId());
        if (userDTO != null) {
            fromDTO.setApplicantName(userDTO.getName());
            UserDTO.Organization organization = userDTO.getOrganization();
            if (organization != null) {
                fromDTO.setDeptName(organization.getName());
            }
        }
        Long fromId = IdWorker.getId();
        fromDTO.setId(fromId);

        // 记录表单
        AttCancelLeaveFromPO insertFromPO = new AttCancelLeaveFromPO();
        BeanUtils.copyProperties(fromDTO, insertFromPO);
        long cancelNumber = AttHolidayDomain.unitAndNumToLong(fromDTO.getCancelLeaveNumber(), AttConstants.HOUR, null);
        insertFromPO.setCancelNumber(cancelNumber);
        if (askLeaveFromPO != null) {
            insertFromPO.setHolidayId(askLeaveFromPO.getHolidayId());
        }
        attCancelLeaveFromMapper.insert(insertFromPO);

        // 发起流程
        JSONObject oldJson = JsonUtils.deserialize(formCommand.getData(), JSONObject.class);
        JSONObject newJson = JsonUtils.deserialize(fromDTO, JSONObject.class);
        formCommand.setData(JsonUtils.serialize(AttApprovalDomain.jsonMerge(newJson, oldJson)));
        String processInstanceId = flowClient.submitFirst(formCommand);

        if (StringUtils.isNotBlank(processInstanceId)) {
            attCancelLeaveFromMapper.update(null, Wrappers.<AttCancelLeaveFromPO>lambdaUpdate()
                                                    .eq(AttCancelLeaveFromPO::getId, fromId)
                                                    .set(AttCancelLeaveFromPO::getProcessInstanceId, processInstanceId)
                                           );
        } else {
            log.error("发起流程失败！未返回流程实例id。");
            attCancelLeaveFromMapper.deleteById(fromId);
            throw new ServiceException("流程发起失败！");
        }
        return processInstanceId;
    }

    /**
     * 销假
     *
     * @param param 销假单
     */
    public void cancelLeave(AttCancelLeaveFromDTO param) {
        if (param == null || param.getAskLeaveFromId() == null || param.getId() == null) {
            log.error("销假消费失败！参数有误：{}", JsonUtils.serialize(param));
            return;
        }
        // 总销假假时间
        long cancelNumber = AttHolidayDomain.unitAndNumToLong(param.getCancelLeaveNumber(), AttConstants.HOUR, null);
        if (cancelNumber <= AttConstants.zeroLong) {
            log.error("销假时长为零：{}", JsonUtils.serialize(param));
            return;
        }
        /* 更新假期余额 */
        attHolidayBalanceRepository.incrementAvailableBalance(AttHolidayBalanceMutationDTO.builder()
                                                             .staffId(param.getApplicantId())
                                                             .staffName(param.getApplicantName())
                                                             .holidayRuleId(param.getHolidayId())
                                                             .availableDelta(cancelNumber)
                                                             .changeNumber(cancelNumber)
                                                             .operator(param.getApplicantName())
                                                             .remark("销假退还")
                                                             .processInstanceId(param.getProcessInstanceId())
                                                             .businessType(ApprovalBizEnum.CANCEL_LEAVE.name())
                                                             .build());

        /* 启用有效期的余额*/
        AttAskLeaveFromPO leaveFromPO = attAskLeaveFromMapper.selectById(param.getAskLeaveFromId());
        if (leaveFromPO != null && StringUtils.isNotBlank(leaveFromPO.getDeadlineIds())) {
            List<Long> idList = Arrays.stream(leaveFromPO.getDeadlineIds().split(",")).map(Long::valueOf).collect(Collectors.toList());
            attHolidayBalanceDeadlineMapper.update(null, Wrappers.<AttHolidayBalanceDeadlinePO>lambdaUpdate()
                                                           .in(AttHolidayBalanceDeadlinePO::getId, idList)
                                                           .set(AttHolidayBalanceDeadlinePO::getNormal, true)
                                                           .set(AttHolidayBalanceDeadlinePO::getUpdateTime, LocalDateTime.now())
                                                  );
        }

        /* 变更请假单信息*/
        attAskLeaveFromMapper.update(null, Wrappers.<AttAskLeaveFromPO>lambdaUpdate()
                                             .eq(AttAskLeaveFromPO::getId, param.getAskLeaveFromId())
                                             .set(AttAskLeaveFromPO::getCancelLeaveFromId, param.getId())
                                             .set(AttAskLeaveFromPO::getUpdateTime, LocalDateTime.now())
                                    );

        /* 变更审批记录信息 */
        if (leaveFromPO != null && StringUtils.isNotBlank(leaveFromPO.getProcessInstanceId())) {
            // 销全部假(销假开始结束时间和请假开始结束时间相同)，清除请假记录
            if (param.getCancelStartTime().equals(leaveFromPO.getLeaveStartTime()) &&
                param.getCancelEndTime().equals(leaveFromPO.getLeaveEndTime())) {
                attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                                   .eq(AttApproveLogPO::getProcessInstanceId, leaveFromPO.getProcessInstanceId())
                                                   .set(AttApproveLogPO::getDeleted, DateUtils.toTimestamp(LocalDateTime.now()))
                                                   .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                          );
            } else if (param.getCancelStartTime().equals(leaveFromPO.getLeaveStartTime())) {
                // 销开始的部分假(开始时间相同)，更新请假开始时间为销假结束时间
                attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                                   .eq(AttApproveLogPO::getProcessInstanceId, leaveFromPO.getProcessInstanceId())
                                                   .set(AttApproveLogPO::getStartTime, param.getCancelEndTime())
                                                   .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                          );
            } else if (param.getCancelEndTime().equals(leaveFromPO.getLeaveEndTime())) {
                // 销结束的部分假(结束时间相同)，更新请假结束时间为销假开始开始
                attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                                   .eq(AttApproveLogPO::getProcessInstanceId, leaveFromPO.getProcessInstanceId())
                                                   .set(AttApproveLogPO::getEndTime, param.getCancelStartTime())
                                                   .set(AttApproveLogPO::getUpdateTime, LocalDateTime.now())
                                          );
            } else {
                log.error("没有匹配的销假时间段：{}", param);
            }

        }

        /* 变更请假台账信息*/

        // 获取请假台账
        List<AttAskLeaveLedgerPO> askLedgerList = attAskLeaveLedgerMapper.selectList(Wrappers.<AttAskLeaveLedgerPO>lambdaQuery()
                                                                                             .eq(AttAskLeaveLedgerPO::getLeaveFromId, param.getAskLeaveFromId())
                                                                                             .orderByAsc(AttAskLeaveLedgerPO::getLeaveDate)
                                                                                    );
        if (askLedgerList == null || askLedgerList.isEmpty()) {
            log.error("销假单" + param.getId() + "没有请假台账记录{}", JsonUtils.serialize(param));
            return;
        }
        // 遍历更新请假台账信息
        long tempAllNum = cancelNumber;
        List<Long> delList = new ArrayList<>();
        for (AttAskLeaveLedgerPO leaveLedgerPO : askLedgerList) {
            if (tempAllNum <= AttConstants.zeroInt) {
                break;
            }
            if (tempAllNum >= leaveLedgerPO.getLeaveNumber()) {
                delList.add(leaveLedgerPO.getId());
            } else {
                leaveLedgerPO.setLeaveNumber(leaveLedgerPO.getLeaveNumber() - tempAllNum);
                leaveLedgerPO.setUpdateTime(LocalDateTime.now());
                attAskLeaveLedgerMapper.update(null, Wrappers.<AttAskLeaveLedgerPO>lambdaUpdate()
                                                       .eq(AttAskLeaveLedgerPO::getId, leaveLedgerPO.getId())
                                                       .set(AttAskLeaveLedgerPO::getLeaveNumber, leaveLedgerPO.getLeaveNumber() - tempAllNum)
                                                       .set(AttAskLeaveLedgerPO::getUpdateTime, LocalDateTime.now())
                                              );
            }
            tempAllNum -= leaveLedgerPO.getLeaveNumber();
        }
        if (!delList.isEmpty()) {
            attAskLeaveLedgerMapper.deleteByIds(delList);
        }

    }
}
