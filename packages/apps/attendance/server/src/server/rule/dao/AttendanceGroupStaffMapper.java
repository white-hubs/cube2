package com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query.AttendanceStatQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupStaffVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupStaffPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author bq
 * @date 2022/11/14
 */
@Mapper
public interface AttendanceGroupStaffMapper extends BaseMapper<AttendanceGroupStaffPO> {

    /**
     * 部分员工总共存在于多少个考勤组
     *
     * @param groupId   排除的考勤组ID
     * @param groupType 考勤组类型
     * @param staffIds  员工ID数组
     *
     * @return
     */
    Integer theStaffIdsHaveManyGroup(@Param("groupId") Long groupId, @Param("groupType") String groupType,
                                     @Param("staffIds") Set<Long> staffIds
                                    );

    /**
     * 查询一些员工所在的考勤组
     *
     * @param staffIds  员工ID数组
     * @param groupType 考勤组类型（排班、值班）
     *
     * @return
     */
    List<AttendanceGroupStaffVO> getGroupIdByStaffIds(@Param("staffIds") List<Long> staffIds, @Param("groupType") String groupType);

    /**
     * 查询一个员工所在的考勤组
     *
     * @param staffId   员工ID
     * @param groupType 考勤组类型（排班、值班）
     * @param groupId   排除的考勤组ID
     *
     * @return 考勤组ID
     */
    Long getGroupIdByStaffId(@Param("staffId") Long staffId, @Param("groupType") String groupType, @Param("groupId") Long groupId);

    List<Long> getStaffIdByGroupId(@Param("groupType") String groupType, @Param("groupId") Long groupId);

    /**
     * 查询考勤人员
     *
     * @param param
     *
     * @return
     */
    List<AttendanceGroupStaffPO> getAttendanceStaff(@Param("param") AttendanceStatQueryParam param);

    /**
     * 查询考勤人员
     *
     * @param page
     * @param param
     *
     * @return
     */
    Page<AttendanceGroupStaffPO> getAttendanceStaff(Page page, @Param("param") AttendanceStatQueryParam param);
}
