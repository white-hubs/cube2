package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyTypeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyTypeService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * 预案类型服务实现
 *
 * @author lanchun
 * @date 2022/9/2 10:31
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyTypeServiceImpl extends ServiceImpl<EmergencyTypeMapper, EmergencyType> implements EmergencyTypeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull EmergencyTypeModifyParam param) {
        EmergencyType emergencyType = ObjectUtils.copying(param, EmergencyType.class);
        this.baseMapper.insert(emergencyType);
        return emergencyType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull EmergencyTypeModifyParam param) {
        EmergencyType emergencyType = this.getById(id);
        Assert.isNotFound(emergencyType, "预案类型不存在或已被删除");
        ObjectUtils.copying(param, emergencyType);
    }

    @Override
    public EmergencyType getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public EmergencyType getEmergencyTypeByAppCode(@NonNull String applicationCode) {
        return this.baseMapper.selectOne(Wrappers.<EmergencyType>lambdaQuery().eq(EmergencyType::getApplicationCode, applicationCode));
    }

    @Override
    public List<EmergencyType> listEmergencyTypes(EmergencyTypeQueryParam param) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(EmergencyType::getCode, param.getKeyword())
                        .or()
                        .like(EmergencyType::getName, param.getKeyword())
                    )
                .eq(StringUtils.isNotEmpty(param.getApplicationCode()), EmergencyType::getApplicationCode, param.getApplicationCode())
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
