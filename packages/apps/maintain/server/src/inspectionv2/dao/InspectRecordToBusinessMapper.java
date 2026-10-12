package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordToBusinessPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordToBusinessVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 巡检记录转其他业务的关联表(工单、隐患、其他等) Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectRecordToBusinessMapper extends BaseMapper<InspectRecordToBusinessPO> {
    /**
     * @Author wql
     * @Description 根据任务id统计隐患单总数
     * @Date 16:59 2023/4/14
     **/
    @Select(value = "select count(b.id) from inspect_record_to_business b " +
                    "left join  inspect_record r on b.record_id = r.id and b.business_category = #{businessCategory} " +
                    "where r.task_id = #{taskId}"
    )
    Integer countByTaskId(@Param("taskId") Long id, @Param("businessCategory") String businessCategory);

    /**
     * @Author wql
     * @Description 查询巡检任务关联的故障单流程实例id
     * @Date 20:03 2023/11/6
     **/
    @Select(value = "select distinct b.business_id from inspect_record_to_business b " +
                    "left join  inspect_record r on b.record_id = r.id " +
                    "where r.task_id = #{taskId}"
    )
    List<String> getProcInstanceIds(Long taskId);

    /**
     * 根据巡检记录id集合批量获取记录转工单信息
     *
     * @param recordIdList 记录id集合
     *
     * @return 转工单信息集合
     */
    List<InspectRecordToBusinessVO> getRecordToBusinessByRecordIdList(@Param("recordIdList") List<Long> recordIdList);
}
