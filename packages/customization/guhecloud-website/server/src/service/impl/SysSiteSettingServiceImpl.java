package com.guhecloud.cube.customized.guhecloudwebsite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.customized.guhecloudwebsite.converts.SysSiteSettingConverts;
import com.guhecloud.cube.customized.guhecloudwebsite.dao.SysSiteSettingMapper;
import com.guhecloud.cube.customized.guhecloudwebsite.entity.SysSiteSetting;
import com.guhecloud.cube.customized.guhecloudwebsite.param.SysSiteSettingParam;
import com.guhecloud.cube.customized.guhecloudwebsite.service.SysSiteSettingService;
import com.guhecloud.cube.customized.guhecloudwebsite.vo.SysSiteSettingVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysSiteSettingServiceImpl extends ServiceImpl<SysSiteSettingMapper, SysSiteSetting>
        implements SysSiteSettingService {

    private final SysSiteSettingMapper siteSettingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysSiteSettingParam param) {
        SysSiteSetting entity = siteSettingMapper.selectOne(null);
        if(null == entity){
             entity = SysSiteSettingConverts.toEntity(param);
            siteSettingMapper.insert(entity);
        }else {
            Long id = entity.getId();
            entity = SysSiteSettingConverts.toEntity(param);
            entity.setId(id);
            siteSettingMapper.updateById(entity);
        }
    }

    @Override
    public SysSiteSettingVO detail() {
        SysSiteSetting entity = siteSettingMapper.selectOne(null);
        if (ObjectUtils.isEmpty(entity)) {
            return null;
        }
        return SysSiteSettingConverts.toVO(entity);
    }

}
