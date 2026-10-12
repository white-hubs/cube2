package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyUserCategoryMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyUserCategory;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserCategoryParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserCategoryQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyUserCategoryService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyUserCategoryVO;
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
 * 描述：应急人员分类管理服务层
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyUserCategoryServiceImpl extends ServiceImpl<EmergencyUserCategoryMapper, EmergencyUserCategory> implements EmergencyUserCategoryService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencyUserCategoryParam param) {
        EmergencyUserCategory emergencyUserCategory = ObjectUtils.copying(param, EmergencyUserCategory.class);
        this.baseMapper.insert(emergencyUserCategory);
        return emergencyUserCategory.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencyUserCategoryParam param) {
        EmergencyUserCategory emergencyUserCategory = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencyUserCategory, "未查询到相关信息");
        ObjectUtils.copying(param, emergencyUserCategory);
        this.baseMapper.updateById(emergencyUserCategory);
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
    public EmergencyUserCategoryVO getById(Long id) {
        EmergencyUserCategory emergencyUserCategory = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencyUserCategory, EmergencyUserCategoryVO.class);
    }

    @Override
    public Pagination<EmergencyUserCategoryVO> getPage(EmergencyUserCategoryQueryParam param) {
        //请自行添加查询条件
        Page<EmergencyUserCategory> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EmergencyUserCategoryVO.class);
    }
}