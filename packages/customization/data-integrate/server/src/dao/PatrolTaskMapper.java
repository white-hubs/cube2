package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyPatrolTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：巡更数据采集 Repository接口
 *
 * @author Appet
 * @date 2023-09-12 15:08:36
 */
@Repository("di-patrol-mapper")
public interface PatrolTaskMapper extends BaseMapper<SafetyPatrolTask> {
    void insertOrUpdateBatch(List<SafetyPatrolTask> dataList);
}