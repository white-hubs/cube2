package com.guhecloud.cube.biz.mdata.server.grid.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.server.grid.dao.GridModelConfigMapper;
import com.guhecloud.cube.biz.mdata.server.grid.entity.GridModelConfig;
import com.guhecloud.cube.biz.mdata.server.grid.service.GridModelConfigService;
import com.guhecloud.cube.biz.mdata.server.grid.vo.GridModelConfigVO;
import org.springframework.stereotype.Service;

/**
 * 网格模型全局配置表 Service 实现
 */
@Service
public class GridModelConfigServiceImpl extends ServiceImpl<GridModelConfigMapper, GridModelConfig> implements GridModelConfigService {

    @Override
    public GridModelConfigVO getGridModelConfig() {
        var config = this.getOne(Wrappers.<GridModelConfig>lambdaQuery().last("LIMIT 1"));
        var vo = new GridModelConfigVO();
        if (config != null) {
            vo.setModelItemId(config.getModelItemId());
        }
        return vo;
    }

    @Override
    public void setGridModelConfig(Long modelItemId) {
        var config = this.getOne(Wrappers.<GridModelConfig>lambdaQuery().last("LIMIT 1"));
        if (config == null) {
            config = new GridModelConfig();
            config.setId(IdWorker.getId());
        }
        config.setModelItemId(modelItemId);
        this.saveOrUpdate(config);
    }
}

