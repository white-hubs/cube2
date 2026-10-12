package com.guhecloud.cube.onecard.server.consumption.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.onecard.server.consumption.entity.CopiesChangeRecord;
import com.guhecloud.cube.onecard.server.consumption.param.CopiesRecordQueryParam;
import com.guhecloud.cube.onecard.server.consumption.vo.CopiesChangeRecordVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface CopiesChangeRecordMapper extends BaseMapper<CopiesChangeRecord> {
    Page<CopiesChangeRecordVo> getCopiesChangeRecord(@Param("param") CopiesRecordQueryParam param, @Param("startTime") String startTime,
                                                     @Param("endTime") String endTime, Page page
                                                    );

    List<Map> getShareStatistics(@Param("param") CopiesRecordQueryParam param, @Param("startTime") String startTime,
                                 @Param("endTime") String endTime
                                );
}
