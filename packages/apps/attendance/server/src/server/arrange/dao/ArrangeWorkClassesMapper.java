package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto.ClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkTodayStatVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.CurrentClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query.ArrangeWorkClassesQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.platform.base.model.Period;
import lombok.NonNull;
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
public interface ArrangeWorkClassesMapper extends BaseMapper<ArrangeWorkClassesPO> {

    List<ClassesDTO> getArrangeWorkClasses(@Param("param") ArrangeWorkClassesQueryParam param);

    /**
     * 查询当前用户当前时间所在的班次
     *
     * @param userId   用户ID
     * @param date     日期
     * @param dateTime 时间点
     */
    CurrentClassesVO getUserClassesInCurrentDate(@Param("userId") Long userId, @Param("date") String date,
                                                 @Param("dateTime") LocalDateTime dateTime
                                                );

    /**
     * 查询当前用户当日在当前时间之后的班次
     *
     * @param userId   用户ID
     * @param date     日期
     * @param dateTime 时间点
     */
    List<CurrentClassesVO> getUserClassesAfterCurrentDate(@Param("userId") Long userId, @Param("date") String date,
                                                          @Param("dateTime") LocalDateTime dateTime
                                                         );

    /**
     * 查询当前用户当日在当前时间之前的班次
     *
     * @param userId   用户ID
     * @param date     日期
     * @param dateTime 时间点
     */
    List<CurrentClassesVO> getUserClassesBeforeCurrentDate(@Param("userId") Long userId, @Param("date") String date,
                                                           @Param("dateTime") LocalDateTime dateTime
                                                          );

    /**
     * 查询当前用户当日在当前时间之前的班次
     *
     * @param userId   用户ID
     * @param date     日期
     * @param dateTime 时间点
     */
    List<CurrentClassesVO> getUserClassesBeforeCurrentDateNotBeforeTimeWhere(@Param("userId") Long userId,
                                                                             @Param("date") String date,
                                                                             @Param("dateTime") LocalDateTime dateTime
                                                                            );

    /**
     * 查询用户在一段时间内的班次
     *
     * @param userId         用户ID
     * @param queryStartTime 查询上班时间在此时间段内的
     * @param queryEndTime   查询下时间在此时间段内的
     * @param period         时间段
     */
    List<CurrentClassesVO> getUserClassesByPeriod(@Param("userId") Long userId,
                                                  @Param("queryStartTime") boolean queryStartTime,
                                                  @Param("queryEndTime") boolean queryEndTime,
                                                  @NonNull @Param("period") Period period
                                                 );

    /**
     * 查询用户的排班班次信息
     *
     * @param userId           用户ID
     * @param date             日期
     * @param arrangeClassesId 排班班次ID
     *
     * @return
     */
    ArrangeWorkClassesVO getUserClassesByClassesId(@Param("userId") Long userId, @Param("date") String date,
                                                   @Param("arrangeClassesId") Long arrangeClassesId
                                                  );

    /**
     * 获取员工指定时间段内所有班次的休息时长
     *
     * @param userId 用户ID
     * @param period 时间段
     */
    List<ArrangeWorkClassesVO> getUserClassesByRestPeriod(@Param("userId") Long userId,
                                                          @NonNull @Param("period") Period period
                                                         );

    /**
     * 暂不用此接口，因为定义当班是打卡之后，而这里虽然给他排班了，但是他并没有打上班卡
     * 筛选当班人员，参数传人员ID数组，筛选出当前时间有班次的人员
     * 注意：${userIds} 不能写成#{userIds} 会把我们想要的参数格式化为 ‘‘xxx’,'yyy'’ ，这样就成了一个参数
     *
     * @param userIds 需要筛选的用户IDS
     * @param date    时间
     *
     * @return
     */
    @Select("select aw.`staff_id`\n" +
            "from `arrange_work` aw\n" +
            "left join `arrange_work_classes` awc on aw.`arrange_work_classes_ids` LIKE CONCAT( '%', awc.`id`, '%' )\n"
            +
            "where aw.`staff_id` in (${userIds})\n" +
            "and awc.`start_real_time` <= #{date} and awc.`end_real_time` >= #{date}"
    )
    List<Long> filterUserInClasses(@Param("userIds") String userIds, @Param("date") String date);

    /**
     * 查询员工某天的班次列表
     *
     * @param userId 用户ID
     * @param date   日期
     *
     * @return
     */
    List<Classes> getUserClassesByDay(@Param("userId") Long userId, @Param("date") LocalDateTime date);

    /**
     * 查询员工包含在指定时间段内的班次，不仅限于全包含，部分班次时间在内即可
     *
     * @param staffId 员工ID
     * @param period  时间段
     */
    List<ArrangeWorkClassesVO> getUserClassesInPeriod(@Param("staffId") Long staffId,
                                                      @Param("period") Period period
                                                     );

    /**
     * 查询当日当班人员（班次时间在今日之内，且是排班制不是固定班制的，产品定的）
     *
     * @param date       当前日期
     * @param beginOfDay 当前日期开始时间
     * @param endOfDay   当前日期结束时间
     */
    List<ArrangeWorkTodayStatVO.Staff> getToadyStaff(@Param("date") String date,
                                                     @Param("beginOfDay") LocalDateTime beginOfDay,
                                                     @Param("endOfDay") LocalDateTime endOfDay
                                                    );

    /**
     * 查询某个时间之前未被引用的排班班次IDS
     *
     * @param dateTime
     *
     * @return
     */
    List<Long> getNoUseWorkClasses(@Param("dateTime") LocalDateTime dateTime);

    /**
     * 查询今日无排班的人员
     *
     * @param date 当前日期
     *
     * @return
     */
    List<ArrangeWorkTodayStatVO.Staff> getToadyStaffNoClasses(@Param("date") String date);

    /**
     * 获取换班的排班班次
     *
     * @param wrapper
     *
     * @return
     */
    @Select(" select if (awc.`id` is null,date_format(aw.`date`,'%Y%m%d'),awc.`id`) as `id` " +
            " , if(awc.`id` is null,concat(aw.`date`,' 休息'),concat(aw.`date`,' (',awc.`start_work_time` ,'-',awc.`end_work_time` ,') 班')) as `name` "
            +
            " FROM `arrange_work` aw " +
            " LEFT JOIN `arrange_work_classes` awc on find_in_set(awc.`id` ,aw.`arrange_work_classes_ids`) ${ew.customSqlSegment} "
            +
            " order by aw.`date` ,awc.`start_real_time` "
    )
    List<FormFieldVO> getChangeClasses(@Param(Constants.WRAPPER) Wrapper wrapper);

}
