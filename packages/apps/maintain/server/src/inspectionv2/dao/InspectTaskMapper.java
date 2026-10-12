package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionAnalysisParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionResultDetailVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 巡检任务 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectTaskMapper extends BaseMapper<InspectTaskPO> {

    @Select("SELECT a.* FROM inspect_task a " +
            "LEFT JOIN inspect_record b ON b.task_id = a.id ${ew.customSqlSegment}"
    )
    Page<InspectTaskVO> getPageByTarget(Page page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * @Author wql
     * @Description 按条件统计任务数量
     * @Date 15:12 2023/4/17
     **/
    @Select("SELECT count(distinct a.id) from inspect_task a ${ew.customSqlSegment}")
    Long countByCondition(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * @Author wql
     * @Description 任务明细列表
     * @Date 16:51 2023/4/17
     **/
    @Select("SELECT a.id as 'taskId',a.name as 'taskName',a.type_name as 'inspectionType',a.check_task_id as 'checkTaskId',a.create_time as 'publishTime', " +
            " timestampdiff(MINUTE,a.valid_begin_time,a.valid_end_time) as 'taskTimeLimit',a.target_num as 'objNum',a.`status` as 'state', a.receive_ids as 'receiveIds'" +
            " ,a.receive_type as 'receiveType', a.receive_dept_ids as 'receiveDeptIds',a.receive_post_ids as 'receivePostIds',a.receive_role_ids as 'receiveRoleIds',a.tenant as 'tenant'  from inspect_task a " +
            " ${ew.customSqlSegment}"
    )
    Page<InspectionResultDetailVO> getTaskDetail(Page<?> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * @Author wql
     * @Description 统计结果异常任务数
     * @Date 16:20 2023/4/18
     **/
    @Select("SELECT count(DISTINCT a.id) from inspect_task a left join inspect_record b on a.id = b.task_id ${ew.customSqlSegment}")
    Integer countAbnormalTaskNum(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * @Author wql
     * @Description 查询所有稽查任务
     * @Date 17:55 2023/4/19
     **/
    List<InspectTaskPO> getAllCheckTask(@Param("param") InspectionAnalysisParam analysisParam);

    @Select("select is_split from inspect_task where id = #{id}")
    Boolean getIsSplitById(@Param("id") Long id);

    @Select("select target_ids from inspect_task ")
    List<String> countObjNum(@Param(Constants.WRAPPER) Wrapper wrapper);

    @InterceptorIgnore(tenantLine = "on")
    List<Integer> getAllTaskTenant();

    /**
     * 根据id集合批量修改状态
     *
     * @param ids     id集合
     * @param status  状态
     * @param endTime 任务结束时间(可以为空)
     *
     * @return 修改行数
     */
    Long updateStatusByIds(@Param("ids") List<Long> ids, @Param("status") String status, @Param("result") String result,
                           @Param("endTime") LocalDateTime endTime,
                           @Param("updateTime") LocalDateTime updateTime,
                           @Param("updateUser") String updateUser
                          );
}
