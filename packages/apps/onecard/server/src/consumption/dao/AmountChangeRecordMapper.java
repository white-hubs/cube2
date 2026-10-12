package com.guhecloud.cube.onecard.server.consumption.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.onecard.server.consumption.entity.AmountChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.param.AmountRecordQueryParam;
import com.guhecloud.cube.onecard.server.consumption.vo.AmountChangeRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface AmountChangeRecordMapper extends BaseMapper<AmountChangeRecord> {
    Page<AmountChangeRecordVo> getAmountChangeRecord(@Param("param") AmountRecordQueryParam param, @Param("startTime") String startTime,
                                                     @Param("endTime") String endTime, Page page
                                                    );

    List<Map> getAmountClassification(@Param("param") AmountRecordQueryParam param, @Param("startTime") String startTime,
                                      @Param("endTime") String endTime
                                     );
}
