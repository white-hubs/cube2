package com.guhecloud.cube.biz.smartcard.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.smartcard.server.entity.VisitingRecord;
import com.guhecloud.cube.biz.smartcard.server.queryparam.CommonStatQueryParam;
import com.guhecloud.cube.biz.smartcard.server.vo.CommonStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("smartcard_VisitingRecord_mapper")
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
