package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyFacilityMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFacility;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFacilityParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFacilityQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyFacilityService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFacilityStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyFacilityVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：应急设施管理服务层
 *
 * @author Appet
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyFacilityServiceImpl extends ServiceImpl<EmergencyFacilityMapper, EmergencyFacility> implements EmergencyFacilityService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencyFacilityParam param) {
        EmergencyFacility emergencyFacility = ObjectUtils.copying(param, EmergencyFacility.class);
        this.baseMapper.insert(emergencyFacility);
        return emergencyFacility.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencyFacilityParam param) {
        EmergencyFacility emergencyFacility = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencyFacility, "未查询到相关信息");
        ObjectUtils.copying(param, emergencyFacility);
        this.baseMapper.updateById(emergencyFacility);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EmergencyFacilityVO getById(Long id) {
        EmergencyFacility emergencyFacility = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencyFacility, EmergencyFacilityVO.class);
    }

    @Override
    public Pagination<EmergencyFacilityVO> getPage(EmergencyFacilityQueryParam param) {
        //请自行添加查询条件
        Page<EmergencyFacility> page = this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getName()), EmergencyFacility::getName, param.getName())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EmergencyFacilityVO.class);
    }

    @Override
    public EmergencyFacilityStatVO getEmergencyFacilityStatForUsed() {
        return this.baseMapper.getEmergencyFacilityStatForUsed();
    }
}