package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto.ArrangeWorkDaysDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query.ArrangeWorkQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.dto.AttendanceStatisticsDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query.AttendanceStatQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.dto.WorkClassDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 */
@Mapper
public interface ArrangeWorkMapper extends BaseMapper<ArrangeWorkPO> {

    /**
     * 查询某个时间范围内的 最近班次
     *
     * @param userId
     * @param startTime
     * @param endTime
     *
     * @return
     */
    @Select("select w.`id`,wc.`id` as `classId`,w.`staff_id`,w.`staff_name`,w.`staff_mobile`,\n" +
            "wc.`start_real_time` as `start_work_time`,\n" +
            "wc.`end_real_time` as `end_work_time` from `arrange_work` w\n" +
            "left join `arrange_work_classes` wc on w.`arrange_work_classes_ids` like CONCAT('%',wc.`id`,'%')\n"
            +
            "where w.`staff_id` = #{userId}\n" +
            "and (wc.`end_real_time` <= #{endTime} or wc.`start_real_time` < #{endTime})\n" +
            "and wc.`start_real_time` >= #{startTime}\n" +
            "order by wc.`end_real_time` desc limit 1 "
    )
    WorkClassDTO getMyLastClass(@Param("userId") Long userId,
                                @Param("startTime") LocalDateTime startTime,
                                @Param("endTime") LocalDateTime endTime
                               );

    /**
     * 查询时间范围内的班次
     *
     * @param userId
     * @param startTime
     * @param endTime
     *
     * @return
     */
    @Select("select w.`id`,wc.`id` as `classId`,w.`staff_id`,w.`staff_name`,w.`staff_mobile`,\n" +
            "wc.`start_real_time` as `start_work_time`,\n" +
            "wc.`end_real_time` as `end_work_time` from `arrange_work` w\n" +
            "left join `arrange_work_classes` wc on w.`arrange_work_classes_ids` like CONCAT('%',wc.`id`,'%')\n"
            +
            "where w.`staff_id` = #{userId}\n" +
            "and ((wc.`end_real_time` > #{startTime} and wc.`end_real_time` <= #{endTime})\n" +
            "or (wc.`start_real_time` >= #{startTime} and wc.`start_real_time` < #{endTime})\n" +
            "or (wc.`start_real_time` <= #{startTime} and wc.`end_real_time` > #{endTime}))"
    )
    List<WorkClassDTO> getClassInRange(@Param("userId") Long userId,
                                       @Param("startTime") LocalDateTime startTime,
                                       @Param("endTime") LocalDateTime endTime
                                      );

    /**
     * 获取应出勤天数
     *
     * @param param
     *
     * @return
     */
    List<ArrangeWorkDaysDTO> getArrangeWorkDays(@Param("param") AttendanceStatQueryParam param);

    List<ArrangeWorkVO> getList(@Param("param") ArrangeWorkQueryParam param);

    List<ArrangeWorkVO> getListV2(@Param("param") ArrangeWorkQueryParam param);

    /**
     * 获取有考勤组的人员排班列表
     *
     * @param param
     *
     * @return
     */
    List<ArrangeWorkVO> getWorkListHaveGroup(@Param("param") ArrangeWorkQueryParam param);

    @Select("select w.`staff_id` , w.`staff_name`, w.`staff_organization_id`, w.`group_id` from `arrange_work` w " +
            "${ew.customSqlSegment} " +
            "group by w.`staff_id`  " +
            "order by w.`staff_id` "
    )
    Page<ArrangeWorkPO> getPageOnlyStaffId(Page<?> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select w.`staff_id`,count(w.`id`) from `arrange_work` w " +
            "${ew.customSqlSegment} "
    )
    Page<ArrangeWorkPO> getPageStaffId(Page<?> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("select w.`staff_id`,count(w.`id`) from `arrange_work` w " +
            "${ew.customSqlSegment} " +
            "group by w.`staff_id`  " +
            "order by w.`staff_name` "
    )
    List<ArrangeWorkPO> getListOnlyStaffId(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 排班人数
     */
    @Select("select count(distinct w.`staff_id`) from `arrange_work` w ${ew.customSqlSegment} ")
    int getArrangeWorkUserCount(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 置休（清除班次id）
     */
    @Select("update `arrange_work` set `arrange_work_classes_ids` = '' ${ew.customSqlSegment} ")
    void cleanArrangeWorkClassesIds(@Param(Constants.WRAPPER) Wrapper wrapper);

    @Select("update `arrange_work` set `arrange_work_classes_ids` = '${classIds}' where `id` = ${id}")
    void updateArrangeWorkClassesIds(@Param("classIds") String classIds, @Param("id") Long id);

    /**
     * 查询 当天有未开始排班 的数据
     *
     * @param wrapper
     *
     * @return
     */
    @Select("select distinct aw.* " +
            " from `arrange_work` aw left join `arrange_work_classes` awc on  aw.`arrange_work_classes_ids` like concat(concat('%',awc.`id`),'%') "
            +
            " ${ew.customSqlSegment} "
    )
    List<ArrangeWorkPO> getTodayNoStartClasses(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 获取考勤人员
     *
     * @return
     */
    Page<AttendanceStatisticsDTO> getAttendanceStaff(Page page, @Param("param") AttendanceStatQueryParam param);

    /**
     * 获取排班人员
     *
     * @param wrapper
     *
     * @return
     */
    @Select("select distinct w.`staff_id` as `id`, w.`staff_name` as `name` from `arrange_work` w ${ew.customSqlSegment} ")
    List<FormFieldVO> getArrangeUsers(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 获取排班租户
     *
     * @param wrapper
     *
     * @return
     */
    @Select(" select distinct w.`tenant` from `arrange_work` w ${ew.customSqlSegment} ")
    List<Integer> getWorkTenantAll(@Param(Constants.WRAPPER) Wrapper wrapper);

}
