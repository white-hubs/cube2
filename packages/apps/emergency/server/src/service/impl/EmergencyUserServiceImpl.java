package com.guhecloud.cube.biz.emergency.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyOrganizationMapper;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyUserCategoryMapper;
import com.guhecloud.cube.biz.emergency.server.dao.EmergencyUserMapper;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyOrganization;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyUser;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyUserCategory;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyUserQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyUserService;
import com.guhecloud.cube.biz.emergency.server.vo.CommonStatVO;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyUserVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：应急人员管理服务层
 *
 * @author Appet
 * @date 2024-01-22 10:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyUserServiceImpl extends ServiceImpl<EmergencyUserMapper, EmergencyUser> implements EmergencyUserService {
    private final EmergencyOrganizationMapper emergencyOrganizationMapper;
    private final EmergencyUserCategoryMapper emergencyUserCategoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(EmergencyUserParam param) {
        EmergencyUser emergencyUser = ObjectUtils.copying(param, EmergencyUser.class);
        this.baseMapper.insert(emergencyUser);
        //扩展人员类型名称
        this.extendEmergencyUser(emergencyUser);
        return emergencyUser.getId();
    }

    /**
     * 扩展应急人员相关信息
     *
     * @param user
     */
    private void extendEmergencyUser(EmergencyUser user) {
        if (StringUtils.isNotBlank(user.getCategoryCode())) {
            List<EmergencyUserCategory> categoryList = emergencyUserCategoryMapper.selectList(Wrappers.<EmergencyUserCategory>lambdaQuery()
                                                                                                      .and(q -> q
                                                                                                              .eq(EmergencyUserCategory::getId, user.getCategoryCode())
                                                                                                              .or()
                                                                                                              .eq(EmergencyUserCategory::getCode, user.getCategoryCode())));
            user.setCategoryName(CollectionUtils.isNotEmpty(categoryList) ? categoryList.getFirst().getName() : "");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, EmergencyUserParam param) {
        EmergencyUser emergencyUser = this.baseMapper.selectById(id);
        Assert.isNotFound(emergencyUser, "未查询到相关信息");
        ObjectUtils.copying(param, emergencyUser);
        //扩展人员类型
        this.extendEmergencyUser(emergencyUser);

        this.baseMapper.updateById(emergencyUser);
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
    public EmergencyUserVO getById(Long id) {
        EmergencyUser emergencyUser = this.baseMapper.selectById(id);
        return ObjectUtils.copying(emergencyUser, EmergencyUserVO.class);
    }

    @Override
    public Pagination<EmergencyUserVO> getPage(EmergencyUserQueryParam param) {
        //处理人员类型条件
        List<String> categoryIdList = new ArrayList<>();
        if (StringUtils.isNotBlank(param.getCategoryCode())) {
            List<EmergencyUserCategory> userCategories = emergencyUserCategoryMapper.selectList(Wrappers
                                                                                                        .<EmergencyUserCategory>lambdaQuery()
                                                                                                        .like(EmergencyUserCategory::getSequence, param.getCategoryCode()));
            categoryIdList = userCategories.stream().map(cate -> {
                String sequence = cate.getSequence();
                return sequence.substring(sequence.indexOf(param.getCategoryCode())).split("-");
            }).flatMap(Arrays::stream).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(categoryIdList)) {
                return Pagination.empty();
            }
        }
        //请自行添加查询条件
        Page<EmergencyUser> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()), EmergencyUser::getName, param.getName())
                .in(CollectionUtils.isNotEmpty(categoryIdList), EmergencyUser::getCategoryCode, categoryIdList)
                .like(StringUtils.isNotBlank(param.getCategoryName()), EmergencyUser::getCategoryName, param.getCategoryName())
                .eq("local".equals(param.getQueryRange()) && param.getEmgOrgId() != null, EmergencyUser::getEmgOrgId, param.getEmgOrgId())
                .in("all".equals(param.getQueryRange()) &&
                    param.getEmgOrgId() != null, EmergencyUser::getEmgOrgId, getLocalAndChildrenIds(param.getEmgOrgId()))
                .page(PagingUtils.convert(param));
        Pagination<EmergencyUserVO> convert = PagingUtils.convert(page, EmergencyUserVO.class);
        if (CollectionUtils.isNotEmpty(convert.getRecords())) {
            convert.getRecords().forEach(u -> {
                if (u.getEmgOrgId() != null && StringUtils.isBlank(u.getEmgOrgName())) {
                    EmergencyOrganization org = emergencyOrganizationMapper.selectById(u.getEmgOrgId());
                    u.setEmgOrgName(org != null ? org.getName() : "");
                }
            });
        }
        return convert;
    }

    /**
     * 获取本身及子节点的ID
     *
     * @param emgOrgId
     *
     * @return
     */
    private List<Long> getLocalAndChildrenIds(Long emgOrgId) {
        List<EmergencyOrganization> emergencyOrganizations = emergencyOrganizationMapper.selectList(Wrappers
                                                                                                            .<EmergencyOrganization>lambdaQuery()
                                                                                                            .like(EmergencyOrganization::getSequence, emgOrgId));
        if (CollectionUtils.isNotEmpty(emergencyOrganizations)) {
            return emergencyOrganizations.stream().map(EmergencyOrganization::getId).collect(Collectors.toList());
        }
        return Collections.singletonList(emgOrgId);
    }

    @Override
    public List<CommonStatVO> getEmergencyUserStat() {
        List<CommonStatVO> emergencyUserStat = this.baseMapper.getEmergencyUserStat();
        //查询人员类型
        List<EmergencyUserCategory> categoryList = emergencyUserCategoryMapper.selectList(Wrappers.<EmergencyUserCategory>lambdaQuery()
                                                                                                  .eq(EmergencyUserCategory::getLevel, 1));
        Map<Long, String> categoryMap = categoryList.stream()
                .collect(Collectors.toMap(EmergencyUserCategory::getId, EmergencyUserCategory::getName));
        if (CollectionUtils.isNotEmpty(emergencyUserStat)) {
            emergencyUserStat.forEach(u -> u.setName(categoryMap.get(u.getId())));
        }
        return emergencyUserStat;
    }
}