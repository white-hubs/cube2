package com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway;

import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.EmployeeHolidayCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.CompensateHolidayBalanceCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.DeductHolidayBalanceCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.OccupyHolidayBalanceCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.HolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.LeaveApprovedCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.LeaveRefuseCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.SubmitLeaveFormCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.command.WithholdCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.application.commandgateway.validator.SubmitLeaveFormValidator;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.aggregate.LeaveForm;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.domainservice.LeaveFormDomainService;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.event.ApprovedLeaveFormEvent;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.event.SubmitLeaveFormEvent;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.factory.LeaveFormFactory;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.repository.LeaveFormRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.leave.domain.valueobject.LeaveStatus;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/11/18 19:07
 * @description 请假单
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = Exception.class)
public class LeaveFormCommandService {
    private final List<SubmitLeaveFormValidator> submitLeaveFormValidators;
    private final LeaveFormRepository leaveFormRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final HolidayQueryService holidayQueryService;
    private final EmployeeHolidayCommandService employeeHolidayCommandService;

    /**
     * 预扣资源
     *
     * @param withholdCommand 预扣资源
     */
    public void withhold(WithholdCommand withholdCommand) {
        Optional<LeaveForm> optional = leaveFormRepository.getById(withholdCommand.getLeaveFormId());
        LeaveForm leaveForm = optional.orElseThrow(() -> new ServiceException("此请假单不存在"));

        Float hour = LeaveFormDomainService.computeLeaveTime(
                leaveForm.getDetail().getUserId(),
                leaveForm.getDetail().getLeaveTime(),
                leaveForm.getLeaveRule()
                                                            );

        // 调用假期扣除余额
        employeeHolidayCommandService.occupy(
                new OccupyHolidayBalanceCommand(
                        withholdCommand.getLeaveFormId().toString(),
                        hour,
                        leaveForm.getDetail().getUserId(),
                        leaveForm.getDetail().getLeaveType())
                                            );
    }

    /**
     * 提交请假单
     *
     * @param submitLeaveFormCommand 提交请假单
     */
    public Long submit(SubmitLeaveFormCommand submitLeaveFormCommand) {
        // 计算真实请假时长
        Holiday holiday = holidayQueryService.getByType(submitLeaveFormCommand.getLeaveType());

        // 构建请假单聚合 如果有个性化或者特殊的功能应该在加一层，不应该在这一层修改，此层只封装核心功能
        LeaveForm leaveForm = LeaveFormFactory.create(
                submitLeaveFormCommand.getSubmitTime(),
                submitLeaveFormCommand.getSubmitUserId(),
                submitLeaveFormCommand.getLeaveTime(),
                submitLeaveFormCommand.getLeaveType(),
                holiday,
                submitLeaveFormCommand.getLocation()
                                                     );
        // 前置验证
        submitLeaveFormValidators.forEach(submitLeaveFormValidator -> submitLeaveFormValidator.verify(leaveForm));

        // 存储请假单聚合
        leaveFormRepository.save(leaveForm);

        // 提交请假单事件
        applicationEventPublisher.publishEvent(new SubmitLeaveFormEvent(this, leaveForm.getId()));
        return leaveForm.getId();
    }

    /**
     * 请假审批通过
     */
    public void approved(LeaveApprovedCommand leaveApprovedCommand) {
        Optional<LeaveForm> optional = leaveFormRepository.getById(leaveApprovedCommand.getLeaveFormId());
        LeaveForm leaveForm = optional.orElseThrow(() -> new ServiceException("此请假单不存在"));
        if (leaveForm.getStatus() == LeaveStatus.PASS || leaveForm.getStatus() == LeaveStatus.REFUSE) {
            throw new ServiceException("已结束的请假单不能操作");
        }
        leaveForm.changeStatus(LeaveStatus.PASS);

        leaveFormRepository.save(leaveForm);

        // 扣减
        employeeHolidayCommandService.deduct(
                new DeductHolidayBalanceCommand(
                        leaveForm.getDetail().getUserId(),
                        leaveForm.getDetail().getLeaveHour(),
                        leaveApprovedCommand.getLeaveFormId().toString(),
                        leaveForm.getDetail().getLeaveType())
                                            );

        applicationEventPublisher.publishEvent(new ApprovedLeaveFormEvent(this, leaveForm.getId()));
    }

    /**
     * 请假审批拒绝
     *
     * @param leaveRefuseCommand 请假拒绝
     */
    public void refuse(LeaveRefuseCommand leaveRefuseCommand) {
        // 释放预扣资源
        Optional<LeaveForm> optional = leaveFormRepository.getById(leaveRefuseCommand.getLeaveFormId());
        LeaveForm leaveForm = optional.get();

        if (leaveForm.getStatus() == LeaveStatus.PASS || leaveForm.getStatus() == LeaveStatus.REFUSE) {
            throw new ServiceException("已结束的请假单不能操作");
        }
        leaveForm.changeStatus(LeaveStatus.REFUSE);

        Holiday holiday = holidayQueryService.getByType(leaveForm.getDetail().getLeaveType());

        leaveFormRepository.save(leaveForm);
        //如果是不限额假期不做
        if (!holiday.isQuotaLimited()) {
            return;
        }

        employeeHolidayCommandService.compensate(
                new CompensateHolidayBalanceCommand(
                        leaveForm.getDetail().getUserId(),
                        leaveRefuseCommand.getLeaveFormId().toString(),
                        leaveForm.getDetail().getLeaveHour(),
                        leaveForm.getDetail().getLeaveType()
                )
                                                );

    }

    public Long trySubmit(SubmitLeaveFormCommand submitLeaveFormCommand) {
        Long leaveFormId = submit(
                submitLeaveFormCommand
                                 );

        //如果是不限额假期不用预扣
        Optional<LeaveForm> optional = leaveFormRepository.getById(leaveFormId);
        LeaveForm leaveForm = optional.get();
        Holiday holiday = holidayQueryService.getByType(leaveForm.getDetail().getLeaveType());
        if (!holiday.isQuotaLimited()) {
            return leaveFormId;
        }

        // 预扣
        withhold(new WithholdCommand(leaveFormId));

        return leaveFormId;
    }

    public void undo(Long leaveFormId) {
        Optional<LeaveForm> optional = leaveFormRepository.getById(leaveFormId);
        LeaveForm leaveForm = optional.orElseThrow(() -> new ServiceException("此请假单不存在"));
        leaveForm.changeStatus(LeaveStatus.UNDO);
        leaveFormRepository.save(leaveForm);

        Holiday holiday = holidayQueryService.getByType(leaveForm.getDetail().getLeaveType());
        //如果是不限额假期不做
        if (!holiday.isQuotaLimited()) {
            return;
        }
        employeeHolidayCommandService.compensate(
                new CompensateHolidayBalanceCommand(
                        leaveForm.getDetail().getUserId(),
                        leaveFormId.toString(),
                        leaveForm.getDetail().getLeaveHour(),
                        leaveForm.getDetail().getLeaveType()
                )
                                                );
    }
}
