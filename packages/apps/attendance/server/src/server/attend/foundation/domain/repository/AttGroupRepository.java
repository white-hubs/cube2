package com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.vo.AttGroupFunctionVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.param.AttGroupStaffQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupAwayBusinessRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupCardRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupHolidayRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupLeavePostRuleResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.interfaces.result.AttGroupOvertimeRuleResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

public interface AttGroupRepository {

    /**
     * 获取考勤所有的租户
     */
    List<Integer> getAttTenantAll();

    /**
     * 新增考勤组
     *
     * @param param 考勤组
     *
     * @return id
     */
    Long insertAttGroup(AttGroupParam param, AttGroupFunctionVO newPO);

    /**
     * 修改考勤组
     *
     * @param param 考勤组对象
     *
     * @return id
     */
    Long updateAttGroup(AttGroupFunctionVO oldPO, AttGroupFunctionVO newPO, AttGroupParam param);

    /**
     * 删除考勤组
     *
     * @param id 主键
     *
     * @return id
     */
    Long deleteAttGroup(AttGroupPO groupPO);

    /**
     * 获取人员考勤组id
     *
     * @param staffId 员工id
     *
     * @return id
     */
    Long getGroupIdByStaffId(Long staffId);

    /**
     * 获取人员考勤组名称
     *
     * @param staffId 员工id
     *
     * @return 名称
     */
    String getGroupNameByStaffId(Long staffId);

    /**
     * 获取人员考勤组
     *
     * @param staffId 员工id
     *
     * @return 考勤组
     */
    AttGroupPO getGroupByStaffId(Long staffId);

    /**
     * 获取补卡规则
     *
     * @param cardRuleIdStr 规则ids
     *
     * @return 规则集合
     */
    List<AttGroupCardRuleResult> getGroupCardRuleList(String cardRuleIdStr);

    /**
     * 获取假期规则
     *
     * @param holidayRuleIdStr 假期规则ids
     *
     * @return 规则集合
     */
    List<AttGroupHolidayRuleResult> getGroupHolidayRuleList(String holidayRuleIdStr);

    /**
     * 获取加班规则列表
     *
     * @param overtimeRuleId 规则ids
     *
     * @return 规则列表
     */
    AttGroupOvertimeRuleResult getGroupOvretimeRuleList(Long overtimeRuleId);

    /**
     * 离岗规则
     *
     * @param leavePostRuleId
     *
     * @return 离岗规则
     */
    AttGroupLeavePostRuleResult getGroupLeavePostRule(Long leavePostRuleId);

    AttGroupAwayBusinessRuleResult getGroupAwayBusinessRule(Long awayBusinessRuleId);

    /**
     * 获取用户考勤组关系
     *
     * @return 用户考勤组关系map
     */
    Map<Long, AttGroupPO> getStaffGroupMap();

    Pagination<AttGroupStaffPO> getGroupStaffPage(AttGroupStaffQueryParam param);

    List<AttGroupStaffPO> getGroupStaffList(AttGroupStaffQueryParam param);
}
