package com.guhecloud.cube.biz.door.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.door.server.entity.AccessEvent;
import com.guhecloud.cube.biz.door.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.door.server.vo.CommonStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AccessEventMapper extends BaseMapper<AccessEvent> {
    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void insertOrUpdateBatch(List<AccessEvent> dataList);

    List<CommonStatVO> getEventStatByName(@Param("param") AccessEventQueryParam param);
}
