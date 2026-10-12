package com.guhecloud.cube.base.alarm.server.sat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.sat.dao.SatAlarmBusinessUrlMapper;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmBusinessUrl;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmBusinessUrlModifyParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmBusinessUrlService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatAlarmBusinessUrlServiceImpl extends ServiceImpl<SatAlarmBusinessUrlMapper, SatAlarmBusinessUrl> implements SatAlarmBusinessUrlService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull SatAlarmBusinessUrlModifyParam param) {
        SatAlarmBusinessUrl satAlarmBusinessUrl = ObjectUtils.copying(param, SatAlarmBusinessUrl.class);
        this.baseMapper.insert(satAlarmBusinessUrl);
        return satAlarmBusinessUrl.getId();
    }

    @Override
    public void update(@NonNull Long id, @NonNull SatAlarmBusinessUrlModifyParam param) {
        SatAlarmBusinessUrl satAlarmBusinessUrl = this.getById(id);
        if (Objects.nonNull(satAlarmBusinessUrl)) {
            ObjectUtils.copying(param, satAlarmBusinessUrl);
            this.baseMapper.updateById(satAlarmBusinessUrl);
        }
    }

    @Override
    public SatAlarmBusinessUrl getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<SatAlarmBusinessUrl> listByBusinessTypes(@NonNull Collection<String> businessTypes, @NonNull String terminal) {
        return CollectionUtils.isEmpty(businessTypes) ? Collections.emptyList() : this.lambdaQuery()
                .in(SatAlarmBusinessUrl::getBusinessType, businessTypes)
                .eq(SatAlarmBusinessUrl::getTerminal, terminal)
                .list();
    }

    @Override
    public SatAlarmBusinessUrl getSatAlarmBusinessUrl(@NonNull String businessType, @NonNull String applicationCode,
                                                      @NonNull String terminal
                                                     ) {
        return this.baseMapper.selectOne(Wrappers.<SatAlarmBusinessUrl>lambdaQuery()
                                                 .eq(SatAlarmBusinessUrl::getBusinessType, businessType)
                                                 .eq(SatAlarmBusinessUrl::getApplicationCode, applicationCode)
                                                 .eq(SatAlarmBusinessUrl::getTerminal, terminal));
    }
}
