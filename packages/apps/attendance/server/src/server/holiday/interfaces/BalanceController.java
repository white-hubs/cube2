package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.commandgateway.command.ChangeBalanceCommand;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.EmployeeHolidayQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.dto.EmployeeBalanceDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.application.querygateway.query.CollectionQuery;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.EmployeeHoliday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayBalance;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.ChangeHolidayBalanceLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceListMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.ChangeHolidayBalanceLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceListPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalancePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.interfaces.vo.EmployeeBalanceVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.commandgateway.operationservice.HolidayBalanceOperationService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 假期余额
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BalanceController {

    private final HolidayBalanceMapper holidayBalanceMapper;
    private final EmployeeHolidayQueryService employeeHolidayQueryService;
    private final HolidayTypeMapper holidayTypeMapper;
    private final HolidayBalanceListMapper holidayBalanceListMapper;
    private final ChangeHolidayBalanceLogMapper changeHolidayBalanceLogMapper;
    private final HolidayBalanceOperationService holidayBalanceOperationService;

    /**
     * @param collectionQuery 查询条件
     *
     * @return
     */
    @GetMapping("/holiday/balance/page")
    public Pagination<EmployeeBalanceVO> list(CollectionQuery collectionQuery) {
        Map<Long, List<HolidayBalanceListPO>> holidayUserMap = holidayBalanceListMapper
                .selectList(Wrappers.<HolidayBalanceListPO>lambdaQuery().eq(HolidayBalanceListPO::getShow, true))
                .stream().collect(Collectors.groupingBy(HolidayBalanceListPO::getHolidayTypeId));
        if (CollectionUtils.isEmpty(holidayUserMap.keySet())) {
            return Pagination.empty();
        }

        List<HolidayTypePO> holidayTypePOS = holidayTypeMapper.selectList(Wrappers.<HolidayTypePO>emptyWrapper());
        Map<Holiday.HolidayType, Long> holidayTypeMap = holidayTypePOS
                .stream().collect(Collectors.toMap(HolidayTypePO::getType, HolidayTypePO::getId));
        Map<Holiday.HolidayType, Boolean> isLImitQuotaMap = holidayTypePOS.stream()
                .collect(Collectors.toMap(HolidayTypePO::getType, HolidayTypePO::getIsNeedLimitQuota));

        // 显示关联员工--余额显示0和不限制；员工花名册删除了这里就删除了
        Page<EmployeeBalanceDTO> page = holidayBalanceMapper.page(PagingUtils.convert(collectionQuery), collectionQuery, SessionContextHolder
                .getRequester().getTenant());

        page.getRecords().forEach(employeeBalanceDTO -> {

            // 先检查是否配置
            if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.ANNUAL_LEAVE)))) {
                boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.ANNUAL_LEAVE))
                        .stream().map(HolidayBalanceListPO::getUserId).toList()
                        .contains(employeeBalanceDTO.getUserId());
                if (!contains) {
                    employeeBalanceDTO.setAnnualLeave("NOT_CONFIGURED");
                }
            }

            if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.BEREAVEMENT_LEAVE)))) {
                boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.BEREAVEMENT_LEAVE))
                        .stream().map(HolidayBalanceListPO::getUserId).toList()
                        .contains(employeeBalanceDTO.getUserId());
                if (!contains) {
                    employeeBalanceDTO.setBereavementLeave("NOT_CONFIGURED");
                }
            }

            if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.COMPASSIONATE_LEAVE)))) {
                boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.COMPASSIONATE_LEAVE)).stream()
                        .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                if (!contains) {
                    employeeBalanceDTO.setCompassionateLeave("NOT_CONFIGURED");
                }
            }

            if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.COMPENSATORY_LEAVE)))) {
                boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.COMPENSATORY_LEAVE)).stream()
                        .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                if (!contains) {
                    employeeBalanceDTO.setCompensatoryLeave("NOT_CONFIGURED");
                }
            }

            if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.MARRIAGE_LEAVE)))) {
                boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.MARRIAGE_LEAVE)).stream()
                        .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                if (!contains) {
                    employeeBalanceDTO.setMarriageLeave("NOT_CONFIGURED");
                }
            }

            if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.MATERNITY_LEAVE)))) {
                boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.MATERNITY_LEAVE)).stream()
                        .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                if (!contains) {
                    employeeBalanceDTO.setMaternityLeave("NOT_CONFIGURED");
                }
            }

            if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.PATERNITY_LEAVE)))) {
                boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.PATERNITY_LEAVE)).stream()
                        .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                if (!contains) {
                    employeeBalanceDTO.setPaternityLeave("NOT_CONFIGURED");
                }
            }

            if (Objects.nonNull(holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.SICK_LEAVE)))) {
                boolean contains = holidayUserMap.get(holidayTypeMap.get(Holiday.HolidayType.SICK_LEAVE)).stream()
                        .map(HolidayBalanceListPO::getUserId).toList().contains(employeeBalanceDTO.getUserId());
                if (!contains) {
                    employeeBalanceDTO.setSickLeave("NOT_CONFIGURED");
                }
            }

            if (!employeeBalanceDTO.getSickLeave().equals("NOT_CONFIGURED") && isLImitQuotaMap.get(Holiday.HolidayType.SICK_LEAVE)) {
                employeeBalanceDTO.setSickLeave("NO_LIMITED");
            } else if (!employeeBalanceDTO.getSickLeave().equals("NOT_CONFIGURED") && Objects.nonNull(employeeBalanceDTO.getSickLeave())) {
                employeeBalanceDTO.setSickLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getSickLeave()) / 8.0f));
            }
            if (!employeeBalanceDTO.getPaternityLeave().equals("NOT_CONFIGURED") &&
                isLImitQuotaMap.get(Holiday.HolidayType.PATERNITY_LEAVE)) {
                employeeBalanceDTO.setPaternityLeave("NO_LIMITED");
            } else if (!employeeBalanceDTO.getPaternityLeave().equals("NOT_CONFIGURED") &&
                       Objects.nonNull(employeeBalanceDTO.getPaternityLeave())) {
                employeeBalanceDTO.setPaternityLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getPaternityLeave()) / 8.0f));
            }
            if (!employeeBalanceDTO.getMarriageLeave().equals("NOT_CONFIGURED") &&
                isLImitQuotaMap.get(Holiday.HolidayType.MARRIAGE_LEAVE)) {
                employeeBalanceDTO.setMarriageLeave("NO_LIMITED");
            } else if (!employeeBalanceDTO.getMarriageLeave().equals("NOT_CONFIGURED") &&
                       Objects.nonNull(employeeBalanceDTO.getMarriageLeave())) {
                employeeBalanceDTO.setMarriageLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getMarriageLeave()) / 8.0f));
            }
            if (!employeeBalanceDTO.getMaternityLeave().equals("NOT_CONFIGURED") &&
                isLImitQuotaMap.get(Holiday.HolidayType.MATERNITY_LEAVE)) {
                employeeBalanceDTO.setMaternityLeave("NO_LIMITED");
            } else if (!employeeBalanceDTO.getMaternityLeave().equals("NOT_CONFIGURED") &&
                       Objects.nonNull(employeeBalanceDTO.getMaternityLeave())) {
                employeeBalanceDTO.setMaternityLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getMaternityLeave()) / 8.0f));
            }
            if (!employeeBalanceDTO.getCompensatoryLeave().equals("NOT_CONFIGURED") &&
                isLImitQuotaMap.get(Holiday.HolidayType.COMPENSATORY_LEAVE)) {
                employeeBalanceDTO.setCompensatoryLeave("NO_LIMITED");
            } else if (!employeeBalanceDTO.getCompensatoryLeave().equals("NOT_CONFIGURED") &&
                       Objects.nonNull(employeeBalanceDTO.getCompensatoryLeave())) {
                employeeBalanceDTO.setCompensatoryLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getCompensatoryLeave())));
            }
            if (!employeeBalanceDTO.getCompassionateLeave().equals("NOT_CONFIGURED") &&
                isLImitQuotaMap.get(Holiday.HolidayType.COMPASSIONATE_LEAVE)) {
                employeeBalanceDTO.setCompassionateLeave("NO_LIMITED");
            } else if (!employeeBalanceDTO.getCompassionateLeave().equals("NOT_CONFIGURED") &&
                       Objects.nonNull(employeeBalanceDTO.getCompassionateLeave())) {
                employeeBalanceDTO.setCompassionateLeave(String.valueOf(
                        Float.parseFloat(employeeBalanceDTO.getCompassionateLeave()) / 8.0f));
            }
            if (!employeeBalanceDTO.getBereavementLeave().equals("NOT_CONFIGURED") &&
                isLImitQuotaMap.get(Holiday.HolidayType.BEREAVEMENT_LEAVE)) {
                employeeBalanceDTO.setBereavementLeave("NO_LIMITED");
            } else if (!employeeBalanceDTO.getBereavementLeave().equals("NOT_CONFIGURED") &&
                       Objects.nonNull(employeeBalanceDTO.getBereavementLeave())) {
                employeeBalanceDTO.setBereavementLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getBereavementLeave()) / 8.0f));
            }
            if (!employeeBalanceDTO.getAnnualLeave().equals("NOT_CONFIGURED") && isLImitQuotaMap.get(Holiday.HolidayType.ANNUAL_LEAVE)) {
                employeeBalanceDTO.setAnnualLeave("NO_LIMITED");
            } else if (!employeeBalanceDTO.getAnnualLeave().equals("NOT_CONFIGURED") &&
                       Objects.nonNull(employeeBalanceDTO.getAnnualLeave())) {
                employeeBalanceDTO.setAnnualLeave(String.valueOf(Float.parseFloat(employeeBalanceDTO.getAnnualLeave()) / 8.0f));
            }
        });
        if (page.getTotal() == 0) {
            return Pagination.empty();
        }

        List<EmployeeBalanceVO> result = new ArrayList<>();
        page.getRecords().forEach(employeeBalanceDTO -> {
            EmployeeBalanceVO employeeBalanceVO = ObjectUtils.copying(employeeBalanceDTO, EmployeeBalanceVO.class);
            employeeBalanceVO.setCompassionateLeaveDay(employeeBalanceDTO.getCompassionateLeave());
            employeeBalanceVO.setCompensatoryLeaveHour(employeeBalanceDTO.getCompensatoryLeave());
            employeeBalanceVO.setBereavementLeaveDay(employeeBalanceDTO.getBereavementLeave());
            employeeBalanceVO.setAnnualLeaveDay(employeeBalanceDTO.getAnnualLeave());
            employeeBalanceVO.setMaternityLeaveDay(employeeBalanceDTO.getMaternityLeave());
            employeeBalanceVO.setSickLeaveDay(employeeBalanceDTO.getSickLeave());
            employeeBalanceVO.setPaternityLeaveDay(employeeBalanceDTO.getPaternityLeave());
            employeeBalanceVO.setMarriageLeaveDay(employeeBalanceDTO.getMarriageLeave());
            result.add(employeeBalanceVO);
        });
        return Pagination.<EmployeeBalanceVO>builder().pages(page.getPages()).total(page.getTotal()).records(result).build();
    }

    /**
     * 获取员工余额
     *
     * @param holidayType 假期类型
     * @param staffId     员工id
     *
     * @return 员工假期余额  显示方式： 不限额, --, 余额值
     */
    @GetMapping("/staff/{staffId}/holiday/{holidayType}/balance")
    public String getBalanceByEmployee(
            @PathVariable("holidayType") @NotNull Holiday.HolidayType holidayType,
            @PathVariable("staffId") @NotNull Long staffId
                                      ) {
        EmployeeHoliday employeeHoliday = employeeHolidayQueryService.getById(staffId);
        List<HolidayBalance> holidayBalances = employeeHoliday.getHolidayBalances().stream()
                .filter(holidayBalance -> holidayBalance.getHolidayType() == holidayType).toList();
        if (CollectionUtils.isEmpty(holidayBalances)) {
            throw new ServiceException("此员工无此假期");
        }
        HolidayTypePO holidayTypePO = holidayTypeMapper.selectOne(
                Wrappers.<HolidayTypePO>lambdaQuery()
                        .eq(HolidayTypePO::getType, holidayType)
                                                                 );
        if (holidayTypePO.getIsNeedLimitQuota()) {
            return "NO_LIMITED";
        }
        LeaveRule leaveRule = JSONObject.parseObject(holidayTypePO.getLeaveRule(), LeaveRule.class);
        String rsTimeString = holidayBalances.getFirst().getRemainingBalance() + "小时";
        if (LeaveRule.MinimumLeaveTimeUnit.DAY.equals(leaveRule.getMinimumLeaveTimeUnitConvert().getMinimumLeaveTimeUnit()) ||
            LeaveRule.MinimumLeaveTimeUnit.HALF_DAY.equals(leaveRule.getMinimumLeaveTimeUnitConvert().getMinimumLeaveTimeUnit())) {
            rsTimeString = holidayBalances.getFirst().getRemainingBalance() / 8 + "天";
        }
        return rsTimeString;
    }

    /**
     * 修改假期余额
     *
     * @param changeBalanceCommand 修改假期余额命令
     */
    @PutMapping("/employee/holiday/balance")
    public void changeBalance(@RequestBody @Valid ChangeBalanceCommand changeBalanceCommand) {
        // 不限制  未配置不能修改假期余额
        HolidayTypePO holidayTypePO = holidayTypeMapper.selectOne(Wrappers.<HolidayTypePO>lambdaQuery()
                                                                          .eq(HolidayTypePO::getType, changeBalanceCommand.getHolidayType()));
        Boolean isNeedLimitQuota = holidayTypePO.getIsNeedLimitQuota();
        if (isNeedLimitQuota) {
            throw new ServiceException("不限制不能修改假期余额");
        }

        HolidayBalancePO holidayBalancePO = holidayBalanceMapper.selectOne(
                Wrappers.<HolidayBalancePO>lambdaQuery()
                        .eq(HolidayBalancePO::getHolidayType, changeBalanceCommand.getHolidayType())
                        .eq(HolidayBalancePO::getUserId, changeBalanceCommand.getEmployeeId())
                                                                          );
        if (Objects.isNull(holidayBalancePO)) {
            holidayBalancePO = new HolidayBalancePO();
            holidayBalancePO.setRemainBalance(changeBalanceCommand.getBalance());
            holidayBalancePO.setHolidayType(changeBalanceCommand.getHolidayType());
            holidayBalancePO.setUserId(changeBalanceCommand.getEmployeeId());
            holidayBalancePO.setRemark(changeBalanceCommand.getRemark());
            saveInsertLog(holidayBalancePO);
            holidayBalanceMapper.insert(holidayBalancePO);
        } else {
            HolidayBalancePO oldPO = new HolidayBalancePO();
            ObjectUtils.copying(holidayBalancePO, oldPO);
            holidayBalancePO.setRemainBalance(changeBalanceCommand.getBalance());
            holidayBalancePO.setRemark(changeBalanceCommand.getRemark());
            saveUpdateLog(oldPO, holidayBalancePO);
            holidayBalanceMapper.updateById(holidayBalancePO);
        }

        List<HolidayBalanceListPO> holidayBalanceListPOS = holidayBalanceListMapper.selectList(
                Wrappers.<HolidayBalanceListPO>lambdaQuery()
                        .eq(HolidayBalanceListPO::getHolidayTypeId, holidayTypePO.getId())
                        .eq(HolidayBalanceListPO::getUserId, changeBalanceCommand.getEmployeeId())
                                                                                              );

        holidayBalanceListMapper.delete(
                Wrappers.<HolidayBalanceListPO>lambdaQuery()
                        .eq(HolidayBalanceListPO::getHolidayTypeId, holidayTypePO.getId())
                        .eq(HolidayBalanceListPO::getUserId, changeBalanceCommand.getEmployeeId())
                                       );

        HolidayBalanceListPO balanceListPO = holidayBalanceListPOS.getFirst();
        HolidayBalanceListPO listPO = ObjectUtils.copying(balanceListPO, HolidayBalanceListPO.class);
        listPO.setRemainBalance(changeBalanceCommand.getBalance().toString());
        listPO.setShow(true);
        holidayBalanceListMapper.insert(listPO);

        ChangeHolidayBalanceLogPO changeHolidayBalanceLogPO = ChangeHolidayBalanceLogPO.builder()
                .userId(SecurityContextHolder.getPrincipal().getId())
                .staffId(changeBalanceCommand.getEmployeeId())
                .originalBalance(changeBalanceCommand.getOriginalBalance())
                .expectedBalance(changeBalanceCommand.getBalance())
                .holidayType(changeBalanceCommand.getHolidayType())
                .build();
        changeHolidayBalanceLogMapper.insert(changeHolidayBalanceLogPO);
    }

    private void saveUpdateLog(HolidayBalancePO oldPO, HolidayBalancePO holidayBalancePO) {
        holidayBalanceOperationService.updateHolidayBalance(getOperationPO(oldPO), getOperationPO(holidayBalancePO));
    }

    private void saveInsertLog(HolidayBalancePO holidayBalancePO) {
        holidayBalanceOperationService.insert(getOperationPO(holidayBalancePO));
    }

    private HolidayBalancePO getOperationPO(HolidayBalancePO holidayBalancePO) {
        HolidayBalancePO operation = new HolidayBalancePO();
        ObjectUtils.copying(holidayBalancePO, operation);
        if (!Holiday.HolidayType.COMPENSATORY_LEAVE.equals(holidayBalancePO.getHolidayType())) {
            operation.setRemainBalance(holidayBalancePO.getRemainBalance() / 8);
        }
        return operation;
    }
}
