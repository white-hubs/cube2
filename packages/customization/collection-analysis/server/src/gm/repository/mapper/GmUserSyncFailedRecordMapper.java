package com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.GmUserSyncFailedRecordPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 光明人员同步失败记录 mapper
 */
@Mapper
public interface GmUserSyncFailedRecordMapper extends BaseMapper<GmUserSyncFailedRecordPO> {
}
