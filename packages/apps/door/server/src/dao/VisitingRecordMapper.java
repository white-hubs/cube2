package com.guhecloud.cube.biz.door.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.door.server.entity.VisitingRecord;
import com.guhecloud.cube.biz.door.server.queryparam.CommonStatQueryParam;
import com.guhecloud.cube.biz.door.server.vo.CommonStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VisitingRecordMapper extends BaseMapper<VisitingRecord> {
    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void insertOrUpdateBatch(List<VisitingRecord> dataList);

    /**
     * 按时间统计访问量
     *
     * @param param
     *
     * @return
     */
    List<CommonStatVO> getVisitingRecordStatByTime(@Param("param") CommonStatQueryParam param);
}
