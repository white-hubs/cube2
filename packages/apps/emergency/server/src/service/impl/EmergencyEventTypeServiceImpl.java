package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyEventTypeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyEventType;
import com.guhecloud.cube.biz.emergency.server.initialize.EmergencyTypeInitConfig;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyEventTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyEventTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyEventTypeService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyEventTypeServiceImpl extends ServiceImpl<EmergencyEventTypeMapper, EmergencyEventType> implements EmergencyEventTypeService {

    private final EmergencyTypeInitConfig emergencyTypeInitConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull EmergencyEventTypeModifyParam param) {
        EmergencyEventType emergencyEventType = ObjectUtils.copying(param, EmergencyEventType.class);
        Long id = IdWorker.getId();
        emergencyEventType.setId(id);
        emergencyEventType.setInitialized(false);
        emergencyEventType.setCode(id.toString());
        this.baseMapper.insert(emergencyEventType);
        return emergencyEventType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull EmergencyEventTypeModifyParam param) {
        EmergencyEventType emergencyEventType = this.baseMapper.selectById(id);
        if (Objects.nonNull(emergencyEventType)) {
            Assert.isNotAcceptable(emergencyEventType.getInitialized(), "初始化分类不支持编辑");
            emergencyEventType.setName(param.getName());
            emergencyEventType.setRemark(param.getRemark());
            this.baseMapper.updateById(emergencyEventType);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initEventTypes(@NonNull Integer tenant) {
        if (!CollectionUtils.isEmpty(this.emergencyTypeInitConfig.getEmergencyTypeConfigs())) {
            SessionContextHolder.requestTenant(tenant);
            List<EmergencyEventType> emergencyEventTypes = new ArrayList<>(this.emergencyTypeInitConfig.getEmergencyTypeConfigs().size());
            this.emergencyTypeInitConfig.getEmergencyTypeConfigs().forEach(emergencyTypeConfig ->
                                                                                   emergencyEventTypes.add(EmergencyEventType
                                                                                                                   .builder()
                                                                                                                   .code(emergencyTypeConfig.getCode())
                                                                                                                   .name(emergencyTypeConfig.getName())
                                                                                                                   .remark(emergencyTypeConfig.getRemark())
                                                                                                                   .initialized(true)
                                                                                                                   .build())
                                                                          );
            this.saveBatch(emergencyEventTypes);
        }
    }

    @Override
    public EmergencyEventType getByCode(@NonNull String code) {
        List<EmergencyEventType> emergencyEventTypes = this.getByCodes(Collections.singleton(code));
        return CollectionUtils.isEmpty(emergencyEventTypes) ? null : emergencyEventTypes.get(0);
    }

    @Override
    public List<EmergencyEventType> getByCodes(@NonNull Collection<String> codes) {
        return CollectionUtils.isEmpty(codes) ? Collections.emptyList() : this.lambdaQuery()
                .in(EmergencyEventType::getCode, codes)
                .list();
    }

    @Override
    public Page<EmergencyEventType> getPage(EmergencyEventTypeQueryParam query) {
        return this.baseMapper.selectPage(PagingUtils.convert(query),
                                          Wrappers.<EmergencyEventType>lambdaQuery()
                                                  .like(StringUtils.isNotEmpty(query.getKeyword()), EmergencyEventType::getName, query.getKeyword())
                                                  .orderByDesc(EmergencyEventType::getCreateTime));
    }

    @Override
    public List<EmergencyEventType> getList(EmergencyEventTypeQueryParam query) {
        return this.baseMapper.selectList(Wrappers.<EmergencyEventType>lambdaQuery()
                                                  .like(StringUtils.isNotEmpty(query.getKeyword()), EmergencyEventType::getName, query.getKeyword())
                                                  .orderByDesc(EmergencyEventType::getCreateTime));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        EmergencyEventType emergencyEventType = this.baseMapper.selectById(id);
        if (Objects.nonNull(emergencyEventType)) {
            Assert.isNotAcceptable(emergencyEventType.getInitialized(), "初始化分类不支持删除");
            DatabaseContextHolder.softDelete(this, id);
        }
    }
}
