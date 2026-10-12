package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencySuppliesTypeMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencySuppliesType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesTypeParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencySuppliesTypeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencySuppliesTypeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencySuppliesTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 描述：应急物资类型管理服务层
 *
 * @author Appet
 * @date 2024-01-016 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencySuppliesTypeServiceImpl extends ServiceImpl<EmergencySuppliesTypeMapper, EmergencySuppliesType> implements EmergencySuppliesTypeService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencySuppliesTypeParam param) {
        EmergencySuppliesType emergencySuppliesType = ObjectUtils.copying(param, EmergencySuppliesType.class);
        this.baseMapper.insert(emergencySuppliesType);
        return emergencySuppliesType.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencySuppliesTypeParam param) {
        EmergencySuppliesType emergencySuppliesType = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencySuppliesType, "未查询到相关信息");
        ObjectUtils.copying(param, emergencySuppliesType);
        this.baseMapper.updateById(emergencySuppliesType);
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
    public EmergencySuppliesTypeVO getById(Long id) {
        EmergencySuppliesType emergencySuppliesType = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencySuppliesType, EmergencySuppliesTypeVO.class);
    }

    @Override
    public Pagination<EmergencySuppliesTypeVO> getPage(EmergencySuppliesTypeQueryParam param) {
        //请自行添加查询条件
        Page<EmergencySuppliesType> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EmergencySuppliesTypeVO.class);
    }
}