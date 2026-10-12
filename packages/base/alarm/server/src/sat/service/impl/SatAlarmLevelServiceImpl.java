package com.guhecloud.cube.base.alarm.server.sat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.alarm.server.sat.dao.SatAlarmLevelMapper;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmLevel;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmLevelModifyParam;
import com.guhecloud.cube.base.alarm.server.sat.service.SatAlarmLevelService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SatAlarmLevelServiceImpl extends ServiceImpl<SatAlarmLevelMapper, SatAlarmLevel> implements SatAlarmLevelService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull SatAlarmLevelModifyParam param) {
        SatAlarmLevel satAlarmLevel = ObjectUtils.copying(param, SatAlarmLevel.class);
        this.baseMapper.insert(satAlarmLevel);
        return satAlarmLevel.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull SatAlarmLevelModifyParam param) {
        SatAlarmLevel satAlarmLevel = this.getById(id);
        Assert.isNotFound(satAlarmLevel, "报警等级不存在");
        ObjectUtils.copying(param, satAlarmLevel);
        this.baseMapper.updateById(satAlarmLevel);
    }

    @Override
    public SatAlarmLevel getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<SatAlarmLevel> listAlarmLevels(String keyword) {
        return this.lambdaQuery().like(StringUtils.isNotEmpty(keyword), SatAlarmLevel::getName, keyword).list();
    }

    @Override
    public List<SatAlarmLevel> listAlarmLevels(@NonNull Collection<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : this.lambdaQuery().in(SatAlarmLevel::getId, ids).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
