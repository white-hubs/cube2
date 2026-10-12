package com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.repository;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.aggregate.Holiday;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.HolidayDetail;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.entity.LeaveRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.repository.HolidayRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.ExpiryDate;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantQuota;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.HolidayGrantRule;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.domain.valueobject.RangeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayBalanceListMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayGrantRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayTypeMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.mapper.HolidayUserRangMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayBalanceListPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayGrantRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayTypePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.po.HolidayUserRangPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.service.HolidayBalanceListService;
import com.guhecloud.cube.srv.gen.attendance.management.server.holiday.infrastructure.service.HolidayUserRangService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupStaffVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffQuery;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSONWriter.Feature.WriteClassName;

/**
 * @author pengjian
 * @date 2022/11/21 14:42
 * @description 假期仓储
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultHolidayRepository implements HolidayRepository {

    private final HolidayGrantRuleMapper grantRuleMapper;
    private final HolidayTypeMapper typeMapper;
    private final HolidayUserRangMapper holidayUserRangMapper;
    private final HolidayUserRangService holidayUserRangService;
    private final StaffClient staffClient;
    private final HolidayBalanceListService holidayBalanceListService;
    private final AttendanceGroupQueryService attendanceGroupQueryService;
    private final HolidayBalanceListMapper balanceListMapper;

    /**
     * 暂时直接做转换,不要动我的代码，我知道这里需要优化!!!
     *
     * @param holiday 假期
     *
     * @return 假期主键
     */
    @Override
    public Long save(Holiday holiday) {

        HolidayTypePO existHolidayTypePO = typeMapper.selectOne(
                Wrappers.<HolidayTypePO>lambdaQuery()
                        .eq(HolidayTypePO::getId, holiday.getId())
                                                               );
        if (Objects.nonNull(existHolidayTypePO)) {
            // 先删除
            holidayUserRangMapper.delete(
                    Wrappers.<HolidayUserRangPO>lambdaQuery()
                            .eq(HolidayUserRangPO::getHolidayTypeId, holiday.getId())
                                        );

            if (Objects.nonNull(existHolidayTypePO.getGrantRuleId())) {
                grantRuleMapper.delete(
                        Wrappers.<HolidayGrantRulePO>lambdaQuery()
                                .eq(HolidayGrantRulePO::getId, existHolidayTypePO.getGrantRuleId())
                                      );
            }
        }

        // 发放规则 覆盖
        Long grantRuleId = null;
        if (Objects.nonNull(holiday.getHolidayGrantRule())) {
            HolidayGrantRulePO grantRulePO = HolidayGrantRulePO.builder()
                    .id(IdWorker.getId())
                    .expiryDate(JSONObject.toJSONString(holiday.getExpiryDate()))
                    .grantTime(Objects.isNull(holiday.getHolidayGrantRule().getGrantTime()) ? null
                                                                                            : holiday.getHolidayGrantRule().getGrantTime()
                                       .name())
                    .grantWay(Objects.isNull(holiday.getHolidayGrantRule().getGrantWay()) ? null
                                                                                          : holiday.getHolidayGrantRule().getGrantWay()
                                      .name())
                    .quota(JSONObject.toJSONString(holiday.getHolidayGrantRule().getHolidayGrantQuotas(), WriteClassName))
                    .build();
            grantRuleMapper.insert(grantRulePO);
            grantRuleId = grantRulePO.getId();
        }

        // 假期类型 覆盖
        HolidayTypePO typePO = HolidayTypePO.builder()
                .id(holiday.getId())
                .code(holiday.getHolidayType().name())
                .name(holiday.getHolidayType().getLabel())
                .timeOfInductionLimit(holiday.getLeaveRule().getNewEmployeeLeaveRule().getTimeOfInduction())
                .type(holiday.getHolidayType())
                .leaveRule(JSONObject.toJSONString(holiday.getLeaveRule(), WriteClassName))
                .grantRuleId(grantRuleId)
                .expiryDate(JSONObject.toJSONString(holiday.getExpiryDate()))
                .build();

        typePO.setIsNeedLimitQuota(Objects.isNull(grantRuleId));

        RangeEnum staffRanges = RangeEnum.All_STAFF;
        if (CollectionUtils.isNotEmpty(holiday.getEmployees())) {
            staffRanges = RangeEnum.PART_STAFF;
        }

        if (Objects.nonNull(existHolidayTypePO)) {
            // 执行更新
            existHolidayTypePO.setId(typePO.getId());
            existHolidayTypePO.setCode(typePO.getCode());
            existHolidayTypePO.setGrantRuleId(typePO.getGrantRuleId());
            existHolidayTypePO.setLeaveRule(typePO.getLeaveRule());
            existHolidayTypePO.setId(typePO.getId());
            existHolidayTypePO.setName(typePO.getName());
            existHolidayTypePO.setTimeOfInductionLimit(typePO.getTimeOfInductionLimit());
            existHolidayTypePO.setType(typePO.getType());
            existHolidayTypePO.setExpiryDate(typePO.getExpiryDate());
            existHolidayTypePO.setIsNeedLimitQuota(typePO.getIsNeedLimitQuota());
            existHolidayTypePO.setStaffRange(staffRanges);
            typeMapper.updateById(existHolidayTypePO);
        } else {
            typePO.setStaffRange(staffRanges);
            typeMapper.insert(typePO);
        }

        // 假期范围 覆盖
        if (CollectionUtils.isNotEmpty(holiday.getEmployees())) {
            List<HolidayUserRangPO> holidayUserRangPOS = new ArrayList<>(holiday.getEmployees().size());
            Map<Long, StaffDetailDTO> employeeInfoMap = staffClient.
                    getList(StaffQuery.builder().staffIds(holiday.getEmployees()).build())
                    .stream().collect(Collectors.toMap(StaffDetailDTO::getId, s -> s));

            List<HolidayBalanceListPO> holidayBalanceListPOS = new ArrayList<>();

            // 批量获取考勤组
            Map<Long, Long> employeeAttendanceGroupMap = attendanceGroupQueryService
                    .getArrangeGroupIdByStaffIds(Lists.newArrayList(holiday.getEmployees()))
                    .stream().collect(Collectors.toMap(AttendanceGroupStaffVO::getStaffId, AttendanceGroupStaffVO::getGroupId));

            holiday.getEmployees().forEach(employeeId -> {
                HolidayUserRangPO userRangPO = HolidayUserRangPO.builder()
                        .holidayTypeId(typePO.getId())
                        .employeeName(employeeInfoMap.get(employeeId).getName())
                        .employeeOrganization(employeeInfoMap.get(employeeId).getOrganizationId())
                        .timeOfInduction(employeeInfoMap.get(employeeId).getInductionDate())
                        .userId(employeeId)
                        .build();
                holidayUserRangPOS.add(userRangPO);
            });

            //批量插入-- 此处需要优化，防止数据量过大
            holidayUserRangService.saveBatch(holidayUserRangPOS);

            List<Long> all = Lists.newArrayList(holiday.getEmployees());

            //  隐藏不是当前范围的员工
            HolidayBalanceListPO entity = new HolidayBalanceListPO();
            entity.setShow(false);
            entity.setHolidayTypeId(typePO.getId());
            balanceListMapper.updateBatchNotIn(Sets.newHashSet(all), entity);

            // 插入不存在的员工
            List<HolidayBalanceListPO> existUsers = balanceListMapper.selectList(
                    Wrappers.<HolidayBalanceListPO>lambdaQuery()
                            .in(HolidayBalanceListPO::getUserId, all)
                                                                                );

            //  显示已经存在的员工
            HolidayBalanceListPO existEntity = new HolidayBalanceListPO();
            existEntity.setShow(true);
            existEntity.setHolidayTypeId(typePO.getId());
            balanceListMapper.updateBatchIn(Sets.newHashSet(all), existEntity);

            if (CollectionUtils.isNotEmpty(existUsers)) {
                all.removeAll(existUsers.stream().map(HolidayBalanceListPO::getUserId).toList());

                all.forEach(staffId -> {
                    StaffDetailDTO staffDetailDTO = employeeInfoMap.get(staffId);
                    HolidayBalanceListPO balanceListPO = HolidayBalanceListPO.builder()
                            .id(IdWorker.getId())
                            .holidayTypeId(typePO.getId())
                            .employeeName(staffDetailDTO.getName())
                            .employeeOrganization(staffDetailDTO.getOrganizationId())
                            .timeOfInduction(staffDetailDTO.getInductionDate())
                            .userId(staffDetailDTO.getId())
                            .attendanceGroupIdOfArrange(employeeAttendanceGroupMap.get(staffDetailDTO.getId()))
                            .build();
                    holidayBalanceListPOS.add(balanceListPO);
                });
                if (CollectionUtils.isNotEmpty(holidayBalanceListPOS)) {
                    holidayBalanceListService.saveBatch(holidayBalanceListPOS);
                }

            } else {
                all.forEach(staffId -> {
                    StaffDetailDTO staffDetailDTO = employeeInfoMap.get(staffId);
                    HolidayBalanceListPO balanceListPO = HolidayBalanceListPO.builder()
                            .id(IdWorker.getId())
                            .holidayTypeId(typePO.getId())
                            .employeeName(staffDetailDTO.getName())
                            .employeeOrganization(staffDetailDTO.getOrganizationId())
                            .timeOfInduction(staffDetailDTO.getInductionDate())
                            .userId(staffDetailDTO.getId())
                            .attendanceGroupIdOfArrange(employeeAttendanceGroupMap.get(staffDetailDTO.getId()))
                            .build();
                    holidayBalanceListPOS.add(balanceListPO);
                });
                if (CollectionUtils.isNotEmpty(holidayBalanceListPOS)) {
                    holidayBalanceListService.saveBatch(holidayBalanceListPOS);
                }
            }

        } else {
            List<HolidayBalanceListPO> holidayBalanceListPOS = new ArrayList<>();
            // 查询所有员工
            List<StaffDetailDTO> list = staffClient.getList(StaffQuery.builder().build());
            Map<Long, StaffDetailDTO> staffMap = list.stream().collect(Collectors.toMap(StaffDetailDTO::getId, s -> s));
            List<Long> all = list.stream().map(StaffDetailDTO::getId).collect(Collectors.toList());
            Map<Long, Long> employeeAttendanceGroupMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(all)) {
                // 批量获取考勤组
                employeeAttendanceGroupMap = attendanceGroupQueryService.getArrangeGroupIdByStaffIds(
                                all
                                                                                                    ).stream()
                        .collect(Collectors.toMap(AttendanceGroupStaffVO::getStaffId, AttendanceGroupStaffVO::getGroupId));
            }
            //  隐藏不是当前范围的员工
            HolidayBalanceListPO entity = new HolidayBalanceListPO();
            entity.setShow(false);
            entity.setHolidayTypeId(typePO.getId());
            balanceListMapper.updateBatchNotIn(Sets.newHashSet(all), entity);

            // 插入不存在的员工
            List<HolidayBalanceListPO> existUsers = balanceListMapper.selectList(
                    Wrappers.<HolidayBalanceListPO>lambdaQuery()
                            .in(HolidayBalanceListPO::getUserId, all)
                            .eq(HolidayBalanceListPO::getHolidayTypeId, existHolidayTypePO.getId())
                            .eq(HolidayBalanceListPO::getShow, true)
                                                                                );

            //  显示已经存在的员工
            HolidayBalanceListPO existEntity = new HolidayBalanceListPO();
            existEntity.setShow(true);
            existEntity.setHolidayTypeId(typePO.getId());
            balanceListMapper.updateBatchIn(Sets.newHashSet(all), existEntity);

            all.removeAll(existUsers.stream().map(HolidayBalanceListPO::getUserId).toList());

            Map<Long, Long> finalEmployeeAttendanceGroupMap = employeeAttendanceGroupMap;
            all.forEach(staffId -> {
                StaffDetailDTO staffDetailDTO = staffMap.get(staffId);
                HolidayBalanceListPO balanceListPO = HolidayBalanceListPO.builder()
                        .id(IdWorker.getId())
                        .holidayTypeId(typePO.getId())
                        .employeeName(staffDetailDTO.getName())
                        .employeeOrganization(staffDetailDTO.getOrganizationId())
                        .timeOfInduction(staffDetailDTO.getInductionDate())
                        .userId(staffDetailDTO.getId())
                        .attendanceGroupIdOfArrange(finalEmployeeAttendanceGroupMap.get(staffDetailDTO.getId()))
                        .build();
                holidayBalanceListPOS.add(balanceListPO);
            });
            if (CollectionUtils.isNotEmpty(holidayBalanceListPOS)) {
                holidayBalanceListService.saveBatch(holidayBalanceListPOS);
            }

        }

        return holiday.getId();
    }

    @Override
    public Optional<Holiday> getById(Long holidayId) {

        HolidayTypePO holidayTypePO = typeMapper.selectById(holidayId);
        if (Objects.isNull(holidayTypePO)) {
            return Optional.empty();
        }

        Holiday.HolidayBuilder holidayBuilder = Holiday.builder()
                .id(holidayTypePO.getId())
                .holidayType(holidayTypePO.getType())
                .leaveRule(JSONObject.parseObject(holidayTypePO.getLeaveRule(), LeaveRule.class));
        holidayBuilder.expiryDate(JSONObject.parseObject(holidayTypePO.getExpiryDate(), ExpiryDate.class));
        // 查询范围--如果为空表示全部用户
        List<HolidayUserRangPO> holidayUserRangPOS = holidayUserRangMapper.selectList(
                Wrappers.<HolidayUserRangPO>lambdaQuery()
                        .eq(HolidayUserRangPO::getHolidayTypeId, holidayId)
                                                                                     );

        if (CollectionUtils.isNotEmpty(holidayUserRangPOS)) {
            Set<Long> employeeIds = holidayUserRangPOS.stream().map(HolidayUserRangPO::getUserId).collect(Collectors.toSet());
            holidayBuilder.employees(employeeIds);
        }

        // 查询假期发放规则 -- 没有规则说明不限制
        HolidayGrantRulePO holidayGrantRulePO = grantRuleMapper.selectOne(
                Wrappers.<HolidayGrantRulePO>lambdaQuery()
                        .eq(HolidayGrantRulePO::getId, holidayTypePO.getGrantRuleId())
                                                                         );
        if (Objects.nonNull(holidayGrantRulePO)) {
            holidayBuilder.holidayGrantRule(
                    new HolidayGrantRule(
                            Objects.isNull(holidayGrantRulePO.getGrantWay()) ? null
                                                                             : HolidayGrantRule.GrantWay.valueOf(holidayGrantRulePO.getGrantWay()),
                            Objects.isNull(holidayGrantRulePO.getGrantTime()) ? null
                                                                              : HolidayGrantRule.GrantTime.valueOf(holidayGrantRulePO.getGrantTime()),
                            JSON.parseArray(holidayGrantRulePO.getQuota(), HolidayGrantQuota.class)
                    )
                                           );

        }
        holidayBuilder.holidayDetail(new HolidayDetail(holidayTypePO.getName()));
        return Optional.of(holidayBuilder.build());
    }

    @Override
    public Holiday getByType(Holiday.HolidayType holidayType) {
        HolidayTypePO holidayTypePO = typeMapper.selectOne(
                Wrappers.<HolidayTypePO>lambdaQuery()
                        .eq(HolidayTypePO::getType, holidayType)
                                                          );

        Assert.isNotFound(holidayTypePO, "无此假期类型");
        return this.getById(holidayTypePO.getId()).orElse(null);

    }
}
