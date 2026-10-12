package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyInspectTask;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：巡检数据采集 Repository接口
 *
 * @author Appet
 * @date 2023-09-12 15:08:36
 */
@Repository("di-inspect-mapper")
public interface InspectTaskMapper extends BaseMapper<SafetyInspectTask> {

    void insertOrUpdateInspectBatch(List<SafetyInspectTask> dataList);
}