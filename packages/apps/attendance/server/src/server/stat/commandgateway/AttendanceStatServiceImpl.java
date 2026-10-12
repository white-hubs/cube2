package com.guhecloud.cube.srv.gen.attendance.management.server.stat.commandgateway;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.ArrangeWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.ArrangeWorkQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query.ArrangeWorkQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.dao.AttendanceStatMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.stat.vo.po.StaffAttendanceStatPO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.base.model.ActorTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttendanceStatServiceImpl extends ServiceImpl<AttendanceStatMapper, StaffAttendanceStatPO> {
    private final ArrangeWorkMapper arrangeWorkMapper;
    private final ArrangeWorkQueryService arrangeWorkQueryService;

    /**
     * 定时统计员工统计数据
     *
     * @param period 时间段
     */
    @Transactional(rollbackFor = Exception.class)
    public void staffAttendanceStat(Period period, Long staffId) {
        LocalDateTime nowDate = LocalDateTime.now();
        if (period == null) {
            period = new Period(DateUtils.beginOfMonth(nowDate), DateUtils.endOfMonth(nowDate));
        }
        List<Integer> workTenantList = arrangeWorkMapper.getWorkTenantAll(Wrappers.<ArrangeWorkPO>lambdaQuery());
        // 循环处理租户的统计信息
        for (Integer tenant : workTenantList) {
            this.staffStat(period, staffId, tenant);
        }
        SessionContextHolder.requestTenant(null);
    }

    /**
     * 员工统计
     *
     * @param period  日期段(月开始-月结束)
     * @param staffId 员工id
     * @param tenant  租户id
     */
    public void staffStat(Period period, Long staffId, Integer tenant) {
        LocalDateTime nowDate = LocalDateTime.now();
        SessionContextHolder.requestTenant(tenant);

        if (period == null) {
            period = new Period(DateUtils.beginOfMonth(nowDate), DateUtils.endOfMonth(nowDate));
        }
        // 获取所有排班人员
        List<ArrangeWorkPO> arrangeWorkPOS = arrangeWorkMapper.getListOnlyStaffId(
                Wrappers.<ArrangeWorkPO>lambdaQuery()
                        .eq(staffId != null, ArrangeWorkPO::getStaffId, staffId)
                        .between(ArrangeWorkPO::getDate, period.getBegin(), period.getEnd())
                        .eq(tenant != null, ArrangeWorkPO::getTenant, tenant)
                                                                                 );

        ArrangeWorkQueryParam param = new ArrangeWorkQueryParam();
        param.setPeriod(period);
        // 扩展人员统计数据
        List<ArrangeWorkVO> arrangeWorkVOS = arrangeWorkQueryService.extWorkVo(arrangeWorkPOS, param);
        // 组装统计对象

        for (ArrangeWorkVO workVo : arrangeWorkVOS) {
            StaffAttendanceStatPO po = new StaffAttendanceStatPO();
            po.setMonth(DateUtils.format(period.getEnd(), "yyyy-MM"));
            po.setStaffId(workVo.getStaffId());
            po.setStaffName(workVo.getStaffName());
            po.setStaffOrganizationId(workVo.getStaffOrganizationId());
            po.setGroupId(workVo.getGroupId());
            po.setStatContent(JsonUtils.serialize(workVo));
            po.setStaffOrganizationId(workVo.getStaffOrganizationId());
            po.setTenant(tenant);
            po.setDeleted(false);
            EntityAuditor.populateAudit(po, "1", StringUtils.EMPTY_STRING, ActorTypeEnum.SYSTEM, nowDate);
            boolean exit = this.exists(Wrappers.<StaffAttendanceStatPO>lambdaQuery()
                                               .eq(StaffAttendanceStatPO::getMonth, DateUtils.format(period.getEnd(), "yyyy-MM"))
                                               .eq(StaffAttendanceStatPO::getStaffId, po.getStaffId()));
            if (exit) {
                this.lambdaUpdate()
                        .set(StaffAttendanceStatPO::getMonth, DateUtils.format(period.getEnd(), "yyyy-MM"))
                        .set(StaffAttendanceStatPO::getStaffId, workVo.getStaffId())
                        .set(StaffAttendanceStatPO::getStaffName, workVo.getStaffName())
                        .set(StaffAttendanceStatPO::getStaffOrganizationId, workVo.getStaffOrganizationId())
                        .set(StaffAttendanceStatPO::getGroupId, workVo.getGroupId())
                        .set(StaffAttendanceStatPO::getStatContent, JsonUtils.serialize(workVo))
                        .set(StaffAttendanceStatPO::getTenant, tenant)
                        .set(StaffAttendanceStatPO::getDeleted, false)
                        .set(StaffAttendanceStatPO::getCreateTime, nowDate)
                        .set(StaffAttendanceStatPO::getCreateBy, "1")
                        .set(StaffAttendanceStatPO::getUpdateBy, "1")
                        .set(StaffAttendanceStatPO::getUpdateTime, nowDate)
                        .eq(StaffAttendanceStatPO::getMonth, DateUtils.format(period.getEnd(), "yyyy-MM"))
                        .eq(StaffAttendanceStatPO::getStaffId, po.getStaffId()).update();
            } else {
                this.save(po);
            }

        }
    }
}
