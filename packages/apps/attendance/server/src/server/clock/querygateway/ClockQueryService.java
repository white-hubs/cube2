package com.guhecloud.cube.srv.gen.attendance.management.server.clock.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.dao.ClassesClockMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.interfaces.vo.ClockVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.po.ClassesClockPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.clock.querygateway.query.ClockQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/24
 * @description 打卡查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ClockQueryService extends ServiceImpl<ClassesClockMapper, ClassesClockPO> {

    private final OrganizationClient organizationClient;
    private final AttendanceGroupMapper attendanceGroupMapper;
    private final ClassesClockMapper classesClockMapper;

    /**
     * 查询当前用户当前班次或者当天的打卡记录
     *
     * @param param
     *
     * @return
     */
    public ClockVO getCurrentClassesClock(ClockQueryParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        ClassesClockPO clockPO = this.lambdaQuery().eq(ClassesClockPO::getClockDate, param.getClockDate())
                .eq(param.getArrangeClassesId() != null, ClassesClockPO::getArrangeClassesId, param.getArrangeClassesId())
                .eq(ClassesClockPO::getUserId, userId).one();
        return ObjectUtils.copying(clockPO, ClockVO.class);
    }

    /**
     * 分页查询班次打卡列表
     *
     * @param param
     *
     * @return
     */
    public Pagination<ClockVO> getPage(ClockQueryParam param) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Page<ClassesClockPO> page = classesClockMapper.getPage(PagingUtils.convert(param), param, tenant);
        Pagination<ClockVO> pagination = PagingUtils.convert(page, ClockVO.class);
        pagination.getRecords().forEach(this::extend);
        this.extendList(pagination.getRecords());
        return pagination;
    }

    /**
     * 导出打卡记录
     *
     * @param param
     *
     * @return
     */
    public void exportClockRecord(ClockQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        List<ClassesClockPO> datas = classesClockMapper.getPage(param, tenant);
        Assert.isNotFound(CollectionUtils.isEmpty(datas), "未查询到打卡数据，无法导出");
        List<ClockVO> clockVOList = ObjectUtils.copying(datas, ClockVO.class);
        this.extendList(clockVOList);
        //导出excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("userName", "姓名");
        //        writer.addHeaderAlias("staffNo","工号");
        writer.addHeaderAlias("userDeptName", "部门");
        writer.addHeaderAlias("attendanceGroupName", "考勤组");
        writer.addHeaderAlias("clockDate", "打卡日期");
        writer.addHeaderAlias("startWorkTime", "上班时间");
        writer.addHeaderAlias("startWorkClockTime", "上班打卡时间");
        writer.addHeaderAlias("endWorkTime", "下班时间");
        writer.addHeaderAlias("endWorkClockTime", "下班打卡时间");

        writer.setOnlyAlias(true);
        //默认配置
        writer.write(clockVOList, true);
        writer.setColumnWidth(-1, 20);

        ExcelUtils.downloadExcel(request, response, "打卡记录", writer);
    }

    /**
     * 查询用户某个班次的打卡情况
     *
     * @param userId           用户ID
     * @param arrangeClassesId 班次ID
     *
     * @return
     */
    public ClockVO getUserClockByClassesId(Long userId, Long arrangeClassesId) {
        List<ClassesClockPO> clockPOS = this.lambdaQuery().eq(ClassesClockPO::getArrangeClassesId, arrangeClassesId)
                .eq(ClassesClockPO::getUserId, userId).orderByDesc(ClassesClockPO::getEndRealTime).list();
        return CollectionUtils.isEmpty(clockPOS) ? new ClockVO() : ObjectUtils.copying(clockPOS.getFirst(), ClockVO.class);
    }

    /**
     * 筛选当前时间在岗的用户
     * 在岗定义：当前打了上班卡，没有打下班卡
     *
     * @param userIds
     *
     * @return
     */
    public List<Long> filterUserInClasses(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        List<ClassesClockPO> clockPOS = this.lambdaQuery().in(ClassesClockPO::getUserId, userIds)
                .lt(ClassesClockPO::getStartWorkClockTime, LocalDateTime.now())
                .gt(ClassesClockPO::getEndRealTime, LocalDateTime.now())
                .isNull(ClassesClockPO::getEndWorkClockStatus)
                .isNull(ClassesClockPO::getEndWorkClockTime)
                .select(ClassesClockPO::getUserId).list();
        return clockPOS.parallelStream().map(ClassesClockPO::getUserId).collect(Collectors.toList());
    }

    private void extend(ClockVO clockVO) {
        Long userDeptId = clockVO.getUserDeptId();
        Long attendanceGroupId = clockVO.getAttendanceGroupId();
        // 查询部门名称
        if (null != userDeptId) {
            try {
                OrganizationDTO dto = organizationClient.getOrganizationsById(userDeptId);
                clockVO.setUserDeptName(dto == null ? "" : dto.getName());
            } catch (Exception e) {
                clockVO.setUserDeptName("");
            }
        }
        // 查询考勤组名称
        if (null != attendanceGroupId) {
            try {
                AttendanceGroupPO po = attendanceGroupMapper.selectById(attendanceGroupId);
                clockVO.setAttendanceGroupName(po == null ? "" : po.getName());
            } catch (Exception e) {
                clockVO.setAttendanceGroupName("");
            }
        }
    }

    private void extendList(List<ClockVO> clockVOList) {
        if (CollectionUtils.isEmpty(clockVOList)) {
            return;
        }
        List<Long> deptIds = clockVOList.stream().map(ClockVO::getUserDeptId).distinct().collect(Collectors.toList());
        List<Long> groupIds = clockVOList.stream().map(ClockVO::getAttendanceGroupId).distinct().collect(Collectors.toList());
        List<OrganizationDTO> organizationDTOS = null;
        if (!groupIds.isEmpty()) {
            organizationDTOS = organizationClient.getOrganizationsByIds(deptIds);
        }
        Map<Long, String> deptMap;
        if (organizationDTOS != null) {
            deptMap = organizationDTOS.stream().collect(Collectors.toMap(OrganizationDTO::getId, OrganizationDTO::getName));
        } else {
            deptMap = new HashMap<>();
        }
        List<AttendanceGroupPO> attendanceGroupPOS = null;
        if (!groupIds.isEmpty()) {
            attendanceGroupPOS = attendanceGroupMapper.selectList(Wrappers.<AttendanceGroupPO>lambdaQuery()
                                                                          .in(AttendanceGroupPO::getId, groupIds));
        }
        Map<Long, String> groupMap;
        if (attendanceGroupPOS != null) {
            groupMap = attendanceGroupPOS.stream().collect(Collectors.toMap(AttendanceGroupPO::getId, AttendanceGroupPO::getName));
        } else {
            groupMap = new HashMap<>();
        }

        clockVOList.forEach(clockVO -> {
            Long userDeptId = clockVO.getUserDeptId();
            Long attendanceGroupId = clockVO.getAttendanceGroupId();
            // 查询部门名称
            if (null != userDeptId) {
                String deptName = deptMap.get(clockVO.getUserDeptId());
                clockVO.setUserDeptName(deptName == null ? "" : deptName);
            }
            // 查询考勤组名称
            if (null != attendanceGroupId) {
                String groupName = groupMap.get(attendanceGroupId);
                clockVO.setAttendanceGroupName(groupName == null ? "" : groupName);

            }
        });

    }

}
