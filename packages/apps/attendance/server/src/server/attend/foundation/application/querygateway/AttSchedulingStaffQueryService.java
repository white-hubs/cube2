package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.service.AttSchedulingStaffService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param.AttSchedulingPageParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 排班人员
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingStaffQueryService {

    private final AttGroupMapper attGroupMapper;
    private final AttSchedulingStaffService attSchedulingStaffService;
    private final UserClient userClient;

    /**
     * 获取排班分页人员信息
     *
     * @param param 条件
     *
     * @return 分页的人员信息(排班对象但是只有人员id)
     */
    public Pagination<AttSchedulingStaffPO> getSchedulingStaffIdPage(AttSchedulingPageParam param) {
        // 先获取排班人员表
        Page<AttSchedulingStaffPO> schedulingStaffPage = attSchedulingStaffService.getAttSchedulingStaffPOPage(param);
        if (schedulingStaffPage.getTotal() > 0) {
            return new Pagination<>(schedulingStaffPage.getTotal(), schedulingStaffPage.getPages(), schedulingStaffPage.getRecords());
        }
        // 再获取考勤组人员表
        Page<AttGroupStaffPO> groupStaffPage = attSchedulingStaffService.getAttGroupStaffPOPage(param);
        if (groupStaffPage.getTotal() > 0) {
            return new Pagination<>(groupStaffPage.getTotal(), groupStaffPage.getPages(), groupStaffPage.getRecords().stream()
                    .map(po -> AttSchedulingStaffPO.builder()
                            .staffId(po.getStaffId())
                            .staffName(po.getName())
                            .build()).collect(Collectors.toList())
            );
        }
        // 最后看有没有全部人员考勤组
        AttGroupPO allGroupPo = attGroupMapper.selectOne(Wrappers.<AttGroupPO>lambdaQuery()
                                                                 .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name()));
        if (allGroupPo == null) {
            return Pagination.empty();
        }
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setPageNumber(param.getPageNumber());
        userQueryParam.setPageSize(param.getPageSize());
        userQueryParam.setEnabled(true);
        if (StringUtils.isNotBlank(param.getKeyword())) {
            userQueryParam.setKeyword(param.getKeyword());
        }
        Pagination<UserDTO> userDTOPagination = userClient.listUserByTenant(SessionContextHolder.getRequester()
                                                                                    .getTenant(), userQueryParam);
        return new Pagination<>(userDTOPagination.getTotal(), userDTOPagination.getPages(), userDTOPagination.getRecords().stream()
                .map(userDTO -> AttSchedulingStaffPO.builder()
                        .staffId(userDTO.getId())
                        .staffName(userDTO.getName())
                        .build()).collect(Collectors.toList())
        );
    }

    /**
     * 获取(所有的)排班人员
     *
     * @param param 条件
     *
     * @return 人员信息(排班对象但是只有人员id)
     */
    public List<AttSchedulingStaffPO> getSchedulingStaffIdAll(AttSchedulingPageParam param) {
        // 先看排班人员是否有当月的；
        List<AttSchedulingStaffPO> resultList = attSchedulingStaffService.getAttSchedulingStaffPOS(param);
        if (resultList != null && !resultList.isEmpty()) {
            return resultList;
        }

        // 再看考勤组人员数据
        List<AttGroupStaffPO> staffGoupList = attSchedulingStaffService.getAttGroupStaffPOS(param);
        if (staffGoupList != null) {
            resultList = staffGoupList.stream().map(staffPO -> AttSchedulingStaffPO.builder()
                    .staffId(staffPO.getStaffId())
                    .staffName(staffPO.getName())
                    .build()).collect(Collectors.toList());
        }
        if (resultList != null && !resultList.isEmpty()) {
            return resultList;
        }

        // 最后看全部人员的
        AttGroupPO allGroupPo = attGroupMapper.selectOne(Wrappers.<AttGroupPO>lambdaQuery()
                                                                 .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name()));
        if (allGroupPo == null) {
            return resultList;
        }
        UserQueryParam userQueryParam = new UserQueryParam();
        if (StringUtils.isNotBlank(param.getKeyword())) {
            userQueryParam.setKeyword(param.getKeyword());
        }
        userQueryParam.setEnabled(true);
        userQueryParam.setPageNumber(1);
        userQueryParam.setPageSize(999);

        Pagination<UserDTO> userDTOPagination = userClient.listUserByTenant(SessionContextHolder.getRequester()
                                                                                    .getTenant(), userQueryParam);
        resultList = userDTOPagination.getRecords().stream().map(userDTO -> AttSchedulingStaffPO.builder()
                .staffId(userDTO.getId())
                .staffName(userDTO.getName())
                .build()).collect(Collectors.toList());
        return resultList;

    }
}
