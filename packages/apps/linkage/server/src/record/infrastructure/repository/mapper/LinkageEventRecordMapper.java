package com.guhecloud.cube.biz.linkage.server.record.infrastructure.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.linkage.server.record.application.querygateway.query.EventRecordQuery;
import com.guhecloud.cube.biz.linkage.server.record.domain.po.LinkageEventRecordPO;
import com.guhecloud.cube.biz.linkage.server.record.interfaces.vo.EventRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LinkageEventRecordMapper extends BaseMapper<LinkageEventRecordPO> {

    Page<EventRecordVO> selectEventRecordPage(Page<LinkageEventRecordPO> page, @Param("query") EventRecordQuery query);

    EventRecordVO selectEventRecordById(@Param("id") Long id);
}
