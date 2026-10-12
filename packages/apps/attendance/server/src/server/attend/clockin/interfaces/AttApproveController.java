package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.dto.ProcessInstanceDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttApproveApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ApprovalRecordPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ApprovePageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ApproveVerificationParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.mapper.AttHolidayRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.infrastructure.po.AttHolidayRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-07  19:32
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttApproveController {
    private final AttApproveApplication approveApplication;
    private final AttApproveLogMapper attApproveLogMapper;
    private final AttAskLeaveFromMapper attAskLeaveFromMapper;
    private final AttHolidayRuleMapper attHolidayRuleMapper;

    /**
     * 审批前缀校验
     */
    @PostMapping("/att/approve/verification")
    public Boolean verification(@RequestBody ApproveVerificationParam param) {
        return approveApplication.verification(param);
    }

    /**
     * 给历史请假记录设置假期名称
     *
     * @return 错误信息
     */
    @GetMapping("/att/approve/holidayTypeName")
    public String holidayTypeName() {
        List<AttApproveLogPO> approveLogPOList = attApproveLogMapper.selectList(Wrappers.<AttApproveLogPO>lambdaQuery()
                                                                                        .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.LEAVE.name())
                                                                                        .isNull(AttApproveLogPO::getSceneCode)
                                                                               );
        if (approveLogPOList == null || approveLogPOList.isEmpty()) {
            return "暂无请符合条件的请假记录！";
        }
        Set<String> instanceIdList = new HashSet<>();
        for (AttApproveLogPO approveLogPO : approveLogPOList) {
            instanceIdList.add(approveLogPO.getProcessInstanceId());
        }
        List<AttAskLeaveFromPO> leaveFromPOList = attAskLeaveFromMapper.selectList(Wrappers.<AttAskLeaveFromPO>lambdaQuery()
                                                                                           .in(AttAskLeaveFromPO::getProcessInstanceId, instanceIdList)
                                                                                  );
        if (leaveFromPOList == null || leaveFromPOList.isEmpty()) {
            return "未获取到请假单；";
        }
        Map<Long, AttHolidayRulePO> holidayRulePOMap = new HashMap<>();
        List<AttHolidayRulePO> holidayRulePOList = attHolidayRuleMapper.selectList(Wrappers.emptyWrapper());
        if (holidayRulePOList != null && !holidayRulePOList.isEmpty()) {
            for (AttHolidayRulePO rulePO : holidayRulePOList) {
                holidayRulePOMap.put(rulePO.getId(), rulePO);
            }
        }
        Map<String, AttAskLeaveFromPO> leaveFromPOMap = new HashMap<>();
        for (AttAskLeaveFromPO attAskLeaveFromPO : leaveFromPOList) {
            leaveFromPOMap.put(attAskLeaveFromPO.getProcessInstanceId(), attAskLeaveFromPO);
        }
        StringBuilder stringBuffer = new StringBuilder();
        for (AttApproveLogPO approveLogPO : approveLogPOList) {
            AttAskLeaveFromPO leaveFromPO = leaveFromPOMap.get(approveLogPO.getProcessInstanceId());
            if (leaveFromPO == null || leaveFromPO.getHolidayId() == null) {
                stringBuffer.append("请假单：").append(approveLogPO.getProcessInstanceId()).append("。");
                approveLogPO.setSceneName("请假");
                approveLogPO.setSceneCode(ClassesStatusTriggerEnum.LEAVE.name());
                attApproveLogMapper.updateById(approveLogPO);
                continue;
            }
            AttHolidayRulePO holidayRulePO = holidayRulePOMap.get(leaveFromPO.getHolidayId());
            if (holidayRulePO == null) {
                stringBuffer.append("假期类型：").append(approveLogPO.getProcessInstanceId()).append("&&").append(leaveFromPO.getHolidayId())
                        .append("。");
                approveLogPO.setSceneName("请假");
                approveLogPO.setSceneCode(ClassesStatusTriggerEnum.LEAVE.name());
                attApproveLogMapper.updateById(approveLogPO);
                continue;
            }
            attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                               .eq(AttApproveLogPO::getId, approveLogPO.getId())
                                               .set(AttApproveLogPO::getSceneCode, ClassesStatusTriggerEnum.LEAVE.name())
                                               .set(AttApproveLogPO::getSceneName, holidayRulePO.getName())
                                      );
        }
        // 更新离岗
        attApproveLogMapper.update(null, Wrappers.<AttApproveLogPO>lambdaUpdate()
                                           .eq(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.LEAVE_THE_POST.getDesc())
                                           .isNull(AttApproveLogPO::getSceneName)
                                           .set(AttApproveLogPO::getSceneCode, ClassesStatusTriggerEnum.LEAVE_THE_POST.name())
                                           .set(AttApproveLogPO::getSceneName, "离岗")
                                  );
        return stringBuffer.toString();
    }

    /**
     * 审批列表
     */
    @GetMapping("/att/getApproveByUserPage")
    public Pagination<ProcessInstanceDTO> getApproveByUserPage(ApprovePageParam param) {
        return approveApplication.getApproveByUserPage(param);
    }

    /**
     * 考勤审批记录分页
     */
    @GetMapping("/att/approve/record/page")
    public Pagination<ProcessInstanceDTO> approvalRecordPage(@Valid ApprovalRecordPageParam param) {
        return approveApplication.approvalRecordPage(param);
    }

    /**
     * 考勤审批记录导出
     */
    @GetMapping("/att/approve/record/export")
    public void approvalRecordExport(@Valid ApprovalRecordPageParam param, HttpServletResponse response) {
        approveApplication.approvalRecordExport(param, response);
    }
}
