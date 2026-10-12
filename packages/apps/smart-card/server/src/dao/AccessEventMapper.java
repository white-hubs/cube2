package com.guhecloud.cube.biz.smartcard.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.smartcard.server.entity.AccessEvent;
import com.guhecloud.cube.biz.smartcard.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.smartcard.server.vo.CommonStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("smartcard_AccessRecord_mapper")
public interface AccessEventMapper extends BaseMapper<AccessEvent> {
    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void insertOrUpdateBatch(List<AccessEvent> dataList);

    List<CommonStatVO> getEventStatByName(@Param("param") AccessEventQueryParam param);
}
