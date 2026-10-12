package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.dto.ClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.ArrangeWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.FormFieldVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author bq
 * @date 2022/11/16
 */
@Mapper
public interface DutyWorkMapper extends BaseMapper<DutyWorkPO> {

    /**
     * 排班人数
     */
    @Select("select count(distinct w.`staff_id`) from `duty_work` w ${ew.customSqlSegment} ")
    int getDutyWorkUserCount(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 置休（清除班次id）
     */
    @Select("update `duty_work` set `duty_work_classes_ids` = '' ${ew.customSqlSegment} ")
    void cleanDutyWorkClassesIds(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 修改值班id
     *
     * @param classIds
     * @param id
     */
    @Select("update `duty_work` set `duty_work_classes_ids` = '${classIds}' where `id` = ${id}")
    void updateDutyWorkClassesIds(@Param("classIds") String classIds, @Param("id") Long id);

    /**
     * 获取值班人员
     *
     * @param wrapper
     *
     * @return
     */
    @Select("select distinct w.`staff_id` as `id`, w.`staff_name` as `name` from `duty_work` w ${ew.customSqlSegment} ")
    List<FormFieldVO> getDutyUsers(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 获取换班班次
     *
     * @param wrapper
     *
     * @return
     */
    @Select(" select if (dwc.`id` is null,date_format(dw.`date`,'%Y%m%d'),dwc.`id`) as `id` " +
            " , if(dwc.`id` is null,concat(dw.`date`,' 休息'),concat(dw.`date`,' (',dwc.`start_work_time`,'-',dwc.`end_work_time`,') 班')) as `name` "
            +
            " FROM `duty_work` dw " +
            " LEFT join `duty_work_classes` dwc on find_in_set(dwc.`id` ,dw.`duty_work_classes_ids`) " +
            " ${ew.customSqlSegment} " +
            " order by dw.`date` "
    )
    List<FormFieldVO> getChangeClasses(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 查询值班的班次数据(查询成排班对象)
     * 值班对象数据不全，所以处理成排班对象
     *
     * @param id
     *
     * @return
     */
    @Select("    select dwc.`id`,concat(dw.`date`,' ',dwc.`start_work_time`,':59') as `start_real_time`,concat(dw.`date`,' ',dwc.`end_work_time`,':00') as `end_real_time` "
            +
            "    ,if(dwc.`before_start_work_time` = '' or dwc.`before_start_work_time` is null ,null,concat(dw.`date`,' ',dwc.`before_start_work_time`,':59')) as `before_start_real_time` "
            +
            "    ,if(dwc.`after_start_work_time` = '' or dwc.`after_start_work_time` is null ,null,concat(dw.`date`,' ',dwc.`after_start_work_time`,':59')) as `after_start_real_time` "
            +
            "    ,if(dwc.`before_end_work_time` = '' or dwc.`before_end_work_time` is null ,null,concat(dw.`date`,' ',dwc.`before_end_work_time`,':00')) as `before_end_real_time` "
            +
            "    ,if(dwc.`after_end_work_time` = '' or dwc.`after_end_work_time` is null ,null,concat(dw.`date`,' ',dwc.`after_end_work_time`,':00')) as `after_end_real_time` "
            +
            "    ,if(dwc.`rest_start_time` = '' or dwc.`rest_start_time` is null ,null,concat(dw.`date`,' ',dwc.`rest_start_time`,':59')) as `rest_start_real_time` "
            +
            "    ,if(dwc.`rest_end_time` = '' or dwc.`rest_end_time` is null ,null,concat(dw.`date`,' ',dwc.`rest_end_time`,':00')) as `rest_end_real_time` "
            +
            "    ,dwc.`start_work_time` ,dwc.`end_work_time`,dwc.`rest_start_time`,dwc.`rest_end_time` ,dwc.`mark` ,dwc.`end_time_is_morrow` "
            +
            "    FROM `duty_work` dw " +
            " inner join `duty_work_classes` dwc on find_in_set(dwc.`id` ,dw.`duty_work_classes_ids`) " +
            " where dwc.id = ${id} "
    )
    ArrangeWorkClassesPO getDutyClassesToArrangeClasses(@Param("id") Long id);

    @Select("    select dwc.`id`,concat(dw.`date`,' ',dwc.`start_work_time`,':59') as `start_real_time`,concat(dw.`date`,' ',dwc.`end_work_time`,':00') as `end_real_time` "
            +
            "    ,dwc.`start_work_time` ,dwc.`end_work_time` ,dwc.`mark` ,dwc.`end_time_is_morrow`,dwc.`classes_id` "
            +
            "    ,dw.`date` " +
            "    FROM `duty_work` dw " +
            " inner join `duty_work_classes` dwc on find_in_set(dwc.`id` ,dw.`duty_work_classes_ids`) " +
            " ${ew.customSqlSegment} "
    )
    List<ClassesDTO> getDutyClassesListToArrange(@Param(Constants.WRAPPER) Wrapper wrapper);

}
