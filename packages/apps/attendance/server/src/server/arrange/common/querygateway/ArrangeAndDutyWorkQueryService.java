package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.querygateway;

import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.querygateway.query.ArrangeAndDutyWorkQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.utils.PageResponse;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.validator.AttendanceGroupValidator;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffNotInParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/12/29
 * @description 排班值班公共查询
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("OptionalOfRedundantMethod")
public class ArrangeAndDutyWorkQueryService {

    private final StaffClient staffClient;
    private final AttendanceGroupStaffMapper attendanceGroupStaffMapper;
    private final AttendanceGroupValidator attendanceGroupQueryService;
    private final OrganizationClient organizationClient;

    /**
     * 分页查询员工列表，过滤掉已经在其他考勤组的人员
     *
     * @param param
     *
     * @return
     */
    public Pagination<StaffDetailDTO> getStaffPage(ArrangeAndDutyWorkQueryParam param) {
        Assert.isParameterError(StringUtils.isEmpty(param.getGroupType()), "请选择考勤组类型");
        // 查询是否存在全员考勤组,已经有全员考勤组时不能再建部分人的考勤组
        if (attendanceGroupQueryService.verifyHaveAllStaffGroup(param.getGroupType(), param.getGroupId())) {
            return Pagination.empty();
        }
        List<StaffDetailDTO> staffList = new ArrayList<>();
        staffList = staffClient.getList(param);
        if (CollectionUtils.isEmpty(staffList)) {
            return Pagination.empty();
        }
        return this.filterStaff(staffList, param);
    }

    public Pagination<StaffDetailDTO> getStaffDTOPage(ArrangeAndDutyWorkQueryParam param) {
        Assert.isParameterError(StringUtils.isEmpty(param.getGroupType()), "请选择考勤组类型");
        // 查询是否存在全员考勤组,已经有全员考勤组时不能再建部分人的考勤组
        if (attendanceGroupQueryService.verifyHaveAllStaffGroup(param.getGroupType(), param.getGroupId())) {
            return Pagination.empty();
        }
        // 获取已排班的人员id(获取人员时需要过滤掉这些人)
        List<Long> staffIds = attendanceGroupStaffMapper.getStaffIdByGroupId(param.getGroupType(), param.getGroupId());
        // 获取人员信息
        Long organizationId = param.getOrganizationId() == null ? null : Long.parseLong(param.getOrganizationId());
        StaffNotInParam staffNotInParam = new StaffNotInParam(organizationId, param.getKeyword(), staffIds, param.getPageNumber()
                .longValue(), param.getPageSize().longValue());
        Pagination<StaffDetailDTO> pagination = staffClient.getStaffPageNotIn(staffNotInParam);
        if (pagination.getRecords() == null || pagination.getRecords().isEmpty()) {
            return pagination;
        }
        // 处理查询部门的入参
        List<Long> organizationIds = Optional.of(pagination.getRecords()).orElse(Collections.emptyList()).parallelStream()
                .map(StaffDetailDTO::getOrganizationId).collect(Collectors.toList());

        if (organizationIds.isEmpty()) {
            return pagination;
        }
        // 查询部门信息
        List<OrganizationDTO> organizationDTOS = organizationClient.getOrganizationsByIds(organizationIds);
        // 将部门list信息转为map(方便取值)
        Map<Long, String> organizationMap = Optional.ofNullable(organizationDTOS).orElse(Collections.emptyList())
                .parallelStream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));
        // 将部门名称填入结果集
        Optional.ofNullable(pagination.getRecords()).orElse(Collections.emptyList())
                .parallelStream().forEach(dto -> dto.setOrganizationName(organizationMap.get(dto.getOrganizationId())));
        return pagination;
    }

    private Pagination<StaffDetailDTO> filterStaff(List<StaffDetailDTO> staffList, ArrangeAndDutyWorkQueryParam param) {
        // 过滤排班人员
        List<Long> staffIds = attendanceGroupStaffMapper.getStaffIdByGroupId(param.getGroupType(), param.getGroupId());
        if (ObjectUtils.isEmpty(staffIds)) {
            return PageResponse.doPage(staffList, param.getPageNumber(), param.getPageSize());
        }
        List<Long> staffIdList = staffList.stream()
                .map(StaffDetailDTO::getId)
                .collect(Collectors.toList());
        // 求差集
        staffIdList.removeAll(staffIds);
        if (ObjectUtils.isEmpty(staffIdList)) {
            return Pagination.empty();
        }

        Map<Long, StaffDetailDTO> map = new HashMap<>();
        for (StaffDetailDTO staff : staffList) {
            map.put(staff.getId(), staff);
        }

        List<StaffDetailDTO> data = new ArrayList<>();
        int start = (param.getPageNumber() - 1) * param.getPageSize();
        int end = start + param.getPageSize();
        for (int i = start; i < end; i++) {
            if (staffIdList.size() <= i) {
                break;
            }
            data.add(map.get(staffIdList.get(i)));
        }
        // 内存分页
        return new Pagination<>(Long.parseLong(String.valueOf(staffList.size())), Long.parseLong(param.getPageNumber().toString()), data);
    }
}
