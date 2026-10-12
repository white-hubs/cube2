package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.CompensateHolidayBalanceCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.DeductHolidayBalanceCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.ExpireCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.GrantHolidayCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.OccupyHolidayBalanceCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.exception.OccupyHolidayBalanceException;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.EmployeeHolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.HolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceListMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceListPO;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author pengjian
 * @date 2022/11/21 15:46
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Transactional(rollbackFor = Exception.class)
public class EmployeeHolidayCommandService {

    private final EmployeeHolidayRepository employeeHolidayRepository;
    private final HolidayRepository holidayRepository;
    private final StaffClient staffClient;
    private final HolidayBalanceListMapper holidayBalanceListMapper;

    /**
     * 假期余额占用
     *
     * @param occupyHolidayBalanceCommand 占用余额命令
     *
     * @return 此次占用编号
     *
     * @throws OccupyHolidayBalanceException 占用失败异常
     */
    public void occupy(OccupyHolidayBalanceCommand occupyHolidayBalanceCommand) throws OccupyHolidayBalanceException {
        EmployeeHoliday employeeHoliday = employeeHolidayRepository.getByEmployeeId(occupyHolidayBalanceCommand.getEmployeeId());

        employeeHoliday.occupy(
                occupyHolidayBalanceCommand.getNumber(),
                occupyHolidayBalanceCommand.getHolidayType(),
                occupyHolidayBalanceCommand.getBusinessKey(),
                ""
                              );

        // 存储
        employeeHolidayRepository.save(employeeHoliday);
    }

    /**
     * 实际扣减假期余额
     *
     * @param deductHolidayBalanceCommand 扣减命令
     */
    public void deduct(DeductHolidayBalanceCommand deductHolidayBalanceCommand) {
        EmployeeHoliday employeeHoliday = employeeHolidayRepository.getByEmployeeId(deductHolidayBalanceCommand.getEmployeeId());

        //  如果是不限额假期不做 todo 后面需要做冗余
        Holiday holiday = holidayRepository.getByType(deductHolidayBalanceCommand.getHolidayType());
        if (!holiday.isQuotaLimited()) {
            employeeHoliday.reduceRecord(
                    deductHolidayBalanceCommand.getNumber(),
                    deductHolidayBalanceCommand.getHolidayType(),
                    deductHolidayBalanceCommand.getBusinessKey(),
                    ""
                                        );
        } else {
            // 将占用库存变为实际库存
            employeeHoliday.reduce(
                    deductHolidayBalanceCommand.getNumber(),
                    deductHolidayBalanceCommand.getHolidayType(),
                    deductHolidayBalanceCommand.getBusinessKey(),
                    ""
                                  );
        }
        employeeHolidayRepository.save(employeeHoliday);
    }

    /**
     * 回滚补偿假余额, 未来支持回滚指定余额
     *
     * @param compensateHolidayBalanceCommand 补偿命令
     */
    public void compensate(CompensateHolidayBalanceCommand compensateHolidayBalanceCommand) {
        EmployeeHoliday employeeHoliday = employeeHolidayRepository.getByEmployeeId(compensateHolidayBalanceCommand.getEmployeeId());
        employeeHoliday.compensate(compensateHolidayBalanceCommand.getNumber(), compensateHolidayBalanceCommand.getHolidayType());
        employeeHolidayRepository.save(employeeHoliday);
    }

    /**
     * 发放假期
     * 支持发放小时，发放天
     *
     * @param grantHolidayCommand 发放命令
     */
    public void grantHoliday(GrantHolidayCommand grantHolidayCommand) {
        // 假期变动
        EmployeeHoliday employeeHoliday = employeeHolidayRepository.getByEmployeeId(grantHolidayCommand.getEmployeeId());
        employeeHoliday.addRemainingBalance(grantHolidayCommand.getNumber(), grantHolidayCommand.getHolidayType(), "");
        employeeHolidayRepository.save(employeeHoliday);
    }

    /**
     * 假期失效
     *
     * @param expireCommand 失效命令
     */
    public void expire(ExpireCommand expireCommand) {
        Holiday holiday = holidayRepository.getByType(expireCommand.getHolidayType());
        // 假期失效：扣除对应假期类型的剩余假期余额
        /*
         * 1. 每次发放的记录：每次发放的剩余多少
         * 2. 定时任务扫描发放记录根据失效规则和发放时间判断是否失效假期
         * 3. 减去剩余余额，发放记录剩余余额置为0，记录失效
         */
        EmployeeHoliday employeeHoliday = employeeHolidayRepository.getByEmployeeId(expireCommand.getEmployeeId());
        employeeHoliday.addExpireBalance(expireCommand.getNumber(), holiday.getHolidayType());
        employeeHolidayRepository.save(employeeHoliday);

    }

    /**
     * 修改假期余额员工信息
     *
     * @param staffId
     */
    public void updateHolidayBalanceUserInfo(Long staffId) {
        StaffDetailDTO staffDetailDTO = staffClient.getStaffVOById(staffId);
        if (staffDetailDTO != null) {
            List<HolidayBalanceListPO> balanceListPOList = holidayBalanceListMapper.selectList(Wrappers.<HolidayBalanceListPO>lambdaQuery()
                                                                                                       .eq(HolidayBalanceListPO::getUserId, staffId));
            if (CollectionUtils.isNotEmpty(balanceListPOList)) {
                balanceListPOList.forEach(po -> {
                    po.setEmployeeName(staffDetailDTO.getName());
                    po.setEmployeeOrganization(staffDetailDTO.getOrganizationId());
                    po.setTimeOfInduction(staffDetailDTO.getInductionDate());
                    holidayBalanceListMapper.updateById(po);
                });
            }
        }
    }

}
