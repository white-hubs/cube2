package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSitePublishConfig;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSitePublishConfigMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSitePublishConfigParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSitePublishConfigService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSitePublishConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 网站发布配置服务实现
 */
@Service
public class SysSitePublishConfigServiceImpl implements SysSitePublishConfigService {

    @Autowired
    private SysSitePublishConfigMapper publishConfigMapper;

    @Override
    public SysSitePublishConfigVO getDetail() {
        List<SysSitePublishConfig> list = publishConfigMapper
            .selectList(new LambdaQueryWrapper<SysSitePublishConfig>()
                .orderByAsc(SysSitePublishConfig::getId)
                .last("LIMIT 1"));
        if (list == null || list.isEmpty()) {
            return null;
        }
        SysSitePublishConfig entity = list.get(0);
        SysSitePublishConfigVO vo = new SysSitePublishConfigVO();
        vo.setId(entity.getId());
        vo.setProvider(entity.getProvider());
        vo.setProviderConfig(entity.getProviderConfig());
        return vo;
    }

    @Override
    @Transactional
    public void saveOrUpdate(SysSitePublishConfigParam param) {
        List<SysSitePublishConfig> list = publishConfigMapper
            .selectList(new LambdaQueryWrapper<SysSitePublishConfig>()
                .orderByAsc(SysSitePublishConfig::getId)
                .last("LIMIT 1"));
        if (list != null && !list.isEmpty()) {
            SysSitePublishConfig entity = list.get(0);
            entity.setProvider(param.getProvider());
            entity.setProviderConfig(param.getProviderConfig());
            publishConfigMapper.updateById(entity);
        } else {
            SysSitePublishConfig entity = new SysSitePublishConfig();
            entity.setProvider(param.getProvider());
            entity.setProviderConfig(param.getProviderConfig());
            publishConfigMapper.insert(entity);
        }
    }

}
