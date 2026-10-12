package com.guhecloud.cube.base.message2.server.sat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.sat.dao.SatMsgManufacturerMapper;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgManufacturerModifyParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgManufacturerService;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class SatMsgManufacturerServiceImpl extends ServiceImpl<SatMsgManufacturerMapper, SatMsgManufacturer> implements SatMsgManufacturerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull SatMsgManufacturerModifyParam param) {
        SatMsgManufacturer satMsgManufacturer = ObjectUtils.copying(param, SatMsgManufacturer.class);
        this.baseMapper.insert(satMsgManufacturer);
        return satMsgManufacturer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull SatMsgManufacturerModifyParam param) {
        SatMsgManufacturer satMsgManufacturer = this.getById(id);
        if (Objects.nonNull(satMsgManufacturer)) {
            ObjectUtils.copying(param, satMsgManufacturer);
            this.baseMapper.updateById(satMsgManufacturer);
        }
    }

    @Override
    public List<SatMsgManufacturer> listSatMsgManufacturerByIds(@NonNull Collection<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(SatMsgManufacturer::getId, ids).list();
    }

    @Override
    public SatMsgManufacturer getById(@NonNull Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
