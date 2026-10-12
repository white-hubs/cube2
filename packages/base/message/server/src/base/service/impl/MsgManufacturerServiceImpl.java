package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.MsgManufacturerMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgManufacturer;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerUpdateParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgManufacturerService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class MsgManufacturerServiceImpl extends ServiceImpl<MsgManufacturerMapper, MsgManufacturer> implements MsgManufacturerService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull MsgManufacturerModifyParam param) {
        MsgManufacturer msgManufacturer = this.baseMapper.selectOne(Wrappers.<MsgManufacturer>lambdaQuery()
                                                                            .eq(MsgManufacturer::getCode, param.getCode()));
        Assert.isConflict(Objects.nonNull(msgManufacturer), "厂家编号已存在");
        msgManufacturer = ObjectUtils.copying(param, MsgManufacturer.class);
        this.baseMapper.insert(msgManufacturer);
        return msgManufacturer.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull String code, @NonNull MsgManufacturerUpdateParam param) {
        MsgManufacturer msgManufacturer = this.getByCode(code);
        if (Objects.nonNull(msgManufacturer)) {
            ObjectUtils.copying(param, msgManufacturer);
            this.baseMapper.updateById(msgManufacturer);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MsgManufacturer getByCode(@NonNull String code) {
        return this.baseMapper.selectOne(Wrappers.<MsgManufacturer>lambdaQuery().eq(MsgManufacturer::getCode, code));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MsgManufacturer> listMsgManufacturers(MsgManufacturerQueryParam param) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(MsgManufacturer::getCode, param.getKeyword())
                        .or()
                        .like(MsgManufacturer::getName, param.getKeyword())
                    )
                .list();
    }

    @Override
    public List<MsgManufacturer> listMsgManufacturersByCodes(@NonNull Collection<ManufacturerCodeEnum> codes) {
        return CollectionUtils.isEmpty(codes) ? Collections.emptyList() : this.lambdaQuery()
                .in(MsgManufacturer::getCode, codes)
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull String code) {
        this.baseMapper.delete(Wrappers.<MsgManufacturer>lambdaQuery().eq(MsgManufacturer::getCode, code));
    }
}
