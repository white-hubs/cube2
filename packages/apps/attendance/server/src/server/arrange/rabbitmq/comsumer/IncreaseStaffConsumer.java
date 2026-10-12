package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.rabbitmq.comsumer;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.command.SubmitWorkCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupClassesCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.command.SubmitAttendanceGroupStaffCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.message.IncreaseStaffMessage;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 新增员工
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RocketMQMessageListener(consumerGroup = IncreaseStaffMessage.TOPIC + "_" + "ATTENDANCEGROUP", topic = IncreaseStaffMessage.TOPIC,
                         selectorType = SelectorType.TAG
)
public class IncreaseStaffConsumer implements RocketMQListener<IncreaseStaffMessage> {

    private final WorkCommandService workCommandService;
    private final AttendanceGroupMapper attendanceGroupMapper;
    private final AttendanceGroupClassesMapper attendanceGroupClassesMapper;
    private final AttGroupMapper attGroupMapper;
    private final AttSchedulingStaffMapper attSchedulingStaffMapper;

    @Override
    public void onMessage(IncreaseStaffMessage message) {
        log.info("收到添加员工消息：{}", JSONObject.toJSONString(message));
        // 新增员工时，如果存在全员排班或值班考勤组，则需要给该员工排班
        try {
            // 2.0 如果有全部人考勤组需要将人员加入到考勤人员中 开始
            List<AttGroupPO> groupPOList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                             .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name()));
            if (groupPOList != null && !groupPOList.isEmpty()) {
                AttGroupPO groupPO = groupPOList.getFirst();
                List<AttSchedulingStaffPO> schedulingStaffPOList = attSchedulingStaffMapper.selectList(Wrappers
                                                                                                               .<AttSchedulingStaffPO>lambdaQuery()
                                                                                                               .eq(AttSchedulingStaffPO::getSchedulingMonth, DateUtils.format(message.getInductionDate(), "yyyy-MM"))
                                                                                                               .eq(AttSchedulingStaffPO::getStaffId, message.getEmployeeId())
                                                                                                               .eq(AttSchedulingStaffPO::getGroupId, groupPO.getId())
                                                                                                      );
                if (schedulingStaffPOList == null || schedulingStaffPOList.isEmpty()) {
                    AttSchedulingStaffPO attGroupStaffPO = AttSchedulingStaffPO.builder()
                            .groupId(groupPO.getId())
                            .staffId(message.getEmployeeId())
                            .staffName(message.getName())
                            .schedulingMonth(DateUtils.format(message.getInductionDate(), "yyyy-MM"))
                            .groupId(IdWorker.getId())
                            .build();
                    attSchedulingStaffMapper.insert(attGroupStaffPO);
                }
            }
            // 2.0 如果有全部人考勤组需要将人员加入到考勤人员中 结束
            AttendanceGroupPO arrangeGroupPO = attendanceGroupMapper.selectOne(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                                       .eq(AttendanceGroupPO::getType, AttendanceGroupTypeEnum.ARRANGE.name())
                                                                                       .eq(AttendanceGroupPO::getRanges, AttendanceGroupRangesEnum.All_STAFF.name()));
            AttendanceGroupPO dutyGroupPO = attendanceGroupMapper.selectOne(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                                    .eq(AttendanceGroupPO::getType, AttendanceGroupTypeEnum.DUTY.name())
                                                                                    .eq(AttendanceGroupPO::getRanges, AttendanceGroupRangesEnum.All_STAFF.name()));
            if (arrangeGroupPO != null) {
                this.generateWorkData(AttendanceGroupTypeEnum.ARRANGE.name(), arrangeGroupPO, message);
            }
            if (dutyGroupPO != null) {
                this.generateWorkData(AttendanceGroupTypeEnum.DUTY.name(), dutyGroupPO, message);
            }
        } catch (Exception e) {
            log.error("收到添加员工消息后，消费报错：{}", JsonUtils.serialize(message), e);
        }
    }

    private void generateWorkData(String type, AttendanceGroupPO groupPO, IncreaseStaffMessage message) {
        SubmitWorkCommand command = new SubmitWorkCommand();
        command.setGroupId(groupPO.getId());
        command.setType(type);
        command.setClassesType(groupPO.getClassesType());
        command.setAutoRest(groupPO.getAutoRest());
        command.setClassesPOS(ObjectUtils.copying(this.getClassesByGroupId(groupPO.getId()), SubmitAttendanceGroupClassesCommand.class));
        List<SubmitAttendanceGroupStaffCommand> staffPOS = new ArrayList<>();
        SubmitAttendanceGroupStaffCommand staffCommand = new SubmitAttendanceGroupStaffCommand();
        staffCommand.setStaffId(message.getEmployeeId());
        staffCommand.setStaffName(message.getName());
        staffCommand.setStaffMobile(message.getMobile());
        staffCommand.setStaffOrganizationId(message.getOrganizationId());
        staffPOS.add(staffCommand);
        command.setStaffPOS(staffPOS);
        workCommandService.generateWorkData(command);
    }

    private List<AttendanceGroupClassesVO> getClassesByGroupId(Long groupId) {
        List<AttendanceGroupClassesVO> classesVOS = new ArrayList<>();
        List<AttendanceGroupClassesPO> classesPOS = attendanceGroupClassesMapper.selectList(Wrappers.<AttendanceGroupClassesPO>lambdaQuery()
                                                                                                    .eq(AttendanceGroupClassesPO::getGroupId, groupId));
        classesPOS.stream().collect(Collectors.groupingBy(AttendanceGroupClassesPO::getWeek)).forEach((week, pos) -> {
            if (CollectionUtils.isNotEmpty(pos)) {
                AttendanceGroupClassesPO classesPO = pos.getFirst();
                AttendanceGroupClassesVO classesVO = new AttendanceGroupClassesVO();
                classesVO.setId(classesPO.getId());
                classesVO.setGroupId(classesPO.getGroupId());
                classesVO.setWeek(classesPO.getWeek());
                classesVO.setClasses(ObjectUtils.copying(pos, Classes.class));
                classesVOS.add(classesVO);
            }
        });
        Collections.sort(classesVOS);
        return classesVOS;
    }

}
