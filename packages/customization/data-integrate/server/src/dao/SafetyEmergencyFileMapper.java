package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.SafetyEmergencyFile;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：预案数据采集配置 Repository接口
 *
 * @author Appet
 * @date 2023-09-13 15:08:36
 */
@Repository
public interface SafetyEmergencyFileMapper extends BaseMapper<SafetyEmergencyFile> {

    void insertOrUpdateBatch(List<SafetyEmergencyFile> dataList);
}