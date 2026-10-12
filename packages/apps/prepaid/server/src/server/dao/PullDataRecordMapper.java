package com.guhecloud.cube.srv.prepaid.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.prepaid.server.po.PullDataRecordPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 同步记录mapper
 */
@Mapper
public interface PullDataRecordMapper extends BaseMapper<PullDataRecordPO> {
}
