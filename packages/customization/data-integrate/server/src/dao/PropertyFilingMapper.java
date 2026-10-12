package com.guhecloud.cube.base.dataintegrate.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.base.dataintegrate.server.entity.PropertyFiling;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：资产数据采集配置 Repository接口
 *
 * @author Appet
 * @date 2023-09-13 15:08:36
 */
@Repository("di-propertyfiling-mapper")
public interface PropertyFilingMapper extends BaseMapper<PropertyFiling> {

    void insertOrUpdateBatch(List<PropertyFiling> dataList);
}