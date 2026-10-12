package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyOrganizationMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyOrganization;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyOrganizationParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyOrganizationQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyOrganizationService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyOrganizationVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
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
 * 描述：应急组织管理服务层
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyOrganizationServiceImpl extends ServiceImpl<EmergencyOrganizationMapper, EmergencyOrganization> implements EmergencyOrganizationService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencyOrganizationParam param) {
        EmergencyOrganization emergencyOrganization = ObjectUtils.copying(param, EmergencyOrganization.class);
        this.baseMapper.insert(emergencyOrganization);
        return emergencyOrganization.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencyOrganizationParam param) {
        EmergencyOrganization emergencyOrganization = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencyOrganization, "未查询到相关信息");
        ObjectUtils.copying(param, emergencyOrganization);
        this.baseMapper.updateById(emergencyOrganization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        //查询是否有下级组织
        List<EmergencyOrganization> orgList = this.baseMapper.selectList(Wrappers.<EmergencyOrganization>lambdaQuery()
                                                                                 .eq(EmergencyOrganization::getParentId, id));
        Assert.isNotAcceptable(CollectionUtils.isNotEmpty(orgList), "该组织下存在子节点，无法删除");
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public EmergencyOrganizationVO getById(Long id) {
        EmergencyOrganization emergencyOrganization = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencyOrganization, EmergencyOrganizationVO.class);
    }

    @Override
    public Pagination<EmergencyOrganizationVO> getPage(EmergencyOrganizationQueryParam param) {
        //请自行添加查询条件
        Page<EmergencyOrganization> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, EmergencyOrganizationVO.class);
    }
}