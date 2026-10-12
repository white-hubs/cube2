package com.guhecloud.cube.srv.prepaid.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.prepaid.server.po.CollectRecordsPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 抄表记录
 */
@Mapper
public interface CollectRecordsMapper extends BaseMapper<CollectRecordsPO> {
}
