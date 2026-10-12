package com.guhecloud.cube.srv.gen.property.management.server.consumable.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.entity.ConsumableCheckDetail;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.param.AppConsumablePageParam;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.AppConsumablePageVO;
import com.guhecloud.cube.srv.gen.property.management.server.consumable.vo.ConsumableCheckExcelVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 描述：耗材盘点明细 Repository接口
 *
 * @author Appet
 * @date 2023-03-16 10:08:36
 */
@Repository
public interface ConsumableCheckDetailMapper extends BaseMapper<ConsumableCheckDetail> {
    List<AppConsumablePageVO> getAppPhysicsPage(@Param("param") AppConsumablePageParam param);

    Page<AppConsumablePageVO> getAppPage(@Param("page") Page page, @Param("param") AppConsumablePageParam param);

    List<Long> getCheckDetail(@Param("param") AppConsumablePageParam param);

    Page<AppConsumablePageVO> getCheckDetailBySataus(@Param("page") Page page, @Param("param") AppConsumablePageParam param);

    List<ConsumableCheckExcelVO> getExcel(@Param("planId") String planId);

    Integer getAddNum(@Param("consumableId") Long consumableId, @Param("startDate") LocalDateTime startDate,
                      @Param("endDate") LocalDateTime endDate
                     );

    Integer getReduceNum(@Param("consumableId") Long consumableId, @Param("startDate") LocalDateTime startDate,
                         @Param("endDate") LocalDateTime endDate
                        );
}