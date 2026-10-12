package com.guhecloud.cube.base.flow.work.order.statistics.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.flow.work.order.statistics.dto.WordOrderStatisticsDTO;
import com.guhecloud.cube.base.flow.work.order.statistics.dto.WorkOrderStatisticsDTO1;
import com.guhecloud.cube.base.flow.work.order.statistics.vo.WordOrderStatisticsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-20  15:50
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface FlowStatisticsMapper {
    Page<WordOrderStatisticsDTO> getWordOrderStatisticsByUser(@Param("page") Page page, @Param("parame") WordOrderStatisticsVO parame);

    List<WordOrderStatisticsDTO> exportWordOrderStatisticsByUser(@Param("parame") WordOrderStatisticsVO parame);

    Page<WorkOrderStatisticsDTO1> selectNumberGroupByBusinessType(@Param("page") Page<?> page, @Param("query") WordOrderStatisticsVO query);

    List<WorkOrderStatisticsDTO1> selectNumberGroupByBusinessType(@Param("query") WordOrderStatisticsVO query);

    /**
     * 根据分组ID和应用编码查询分组下的业务类型集合
     */
    List<String> selectBusinessTypesByFormGroup(@Param("formGroupId") Long formGroupId,
                                                @Param("applicationCode") String applicationCode);
}
