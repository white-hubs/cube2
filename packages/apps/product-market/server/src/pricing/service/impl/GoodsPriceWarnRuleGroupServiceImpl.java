package com.guhecloud.cube.srv.gen.product.market.server.pricing.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnCategoryMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnRuleGroupMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.dao.GoodsPriceWarnUserMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnCategory;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRule;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRuleGroup;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnUser;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleGroupParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRuleGroupQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRuleGroupService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.service.GoodsPriceWarnRuleService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleGroupPageVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleGroupVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRuleVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnUserVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：采价预警规则组服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsPriceWarnRuleGroupServiceImpl extends ServiceImpl<GoodsPriceWarnRuleGroupMapper, GoodsPriceWarnRuleGroup> implements GoodsPriceWarnRuleGroupService {
    private final UserClient userClient;
    private final GoodsPriceWarnRuleService goodsPriceWarnRuleService;
    private final GoodsPriceWarnCategoryMapper goodsPriceWarnCategoryMapper;
    private final GoodsPriceWarnUserMapper goodsPriceWarnUserMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(GoodsPriceWarnRuleGroupParam param) {
        GoodsPriceWarnRuleGroup goodsPriceWarnRuleGroup = ObjectUtils.copying(param, GoodsPriceWarnRuleGroup.class);
        //扩展创建人
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        goodsPriceWarnRuleGroup.setCreatorName(user != null ? user.getName() : "");
        this.baseMapper.insert(goodsPriceWarnRuleGroup);
        return goodsPriceWarnRuleGroup.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addWarnRuleGroup(GoodsPriceWarnRuleGroupParam param) {
        //先保存规则组
        Long groupId = this.add(param);
        //再保存规则
        param.getRules().stream().forEach(rule -> {
            rule.setGroupId(groupId);
            rule.setName(param.getGroupName());
            goodsPriceWarnRuleService.add(rule);
        });
        //保存预警通知人员
        this.addWarnUsers(groupId, param.getUserIds());
    }

    /**
     * 添加通知人员
     *
     * @param groupId
     * @param userIds
     */
    private void addWarnUsers(Long groupId, List<Long> userIds) {
        //先删除
        goodsPriceWarnUserMapper.delete(Wrappers.<GoodsPriceWarnUser>lambdaQuery()
                                                .eq(GoodsPriceWarnUser::getGroupId, groupId));
        if (CollectionUtils.isNotEmpty(userIds)) {
            userIds.stream().forEach(userId -> {
                GoodsPriceWarnUser warnUser = new GoodsPriceWarnUser();
                warnUser.setGroupId(groupId);
                warnUser.setUserId(userId);
                UserDTO user = userClient.getUser(userId);
                if (user != null) {
                    warnUser.setUserName(user.getName());
                }
                goodsPriceWarnUserMapper.insert(warnUser);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWarnRuleGroup(Long id, GoodsPriceWarnRuleGroupParam param) {
        //先保存规则组
        GoodsPriceWarnRuleGroup ruleGroup = this.update(id, param);
        //先删除告警组下的所有规则
        goodsPriceWarnRuleService.remove(Wrappers.<GoodsPriceWarnRule>lambdaQuery()
                                                 .eq(GoodsPriceWarnRule::getGroupId, id));
        //删除告警适用的品类
        goodsPriceWarnCategoryMapper.delete(Wrappers.<GoodsPriceWarnCategory>lambdaQuery()
                                                    .eq(GoodsPriceWarnCategory::getGroupId, id));
        //再保存规则
        param.getRules().stream().forEach(rule -> {
            //再保存报警规则
            rule.setGroupId(id);
            rule.setName(ruleGroup.getGroupName());
            goodsPriceWarnRuleService.add(rule);
        });
    }

    @Override
    public void updateStatus(Long id, Boolean status) {
        GoodsPriceWarnRuleGroup goodsPriceWarnRuleGroup = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPriceWarnRuleGroup, "未查询到相关信息");
        goodsPriceWarnRuleGroup.setStatus(status);
        this.updateById(goodsPriceWarnRuleGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public GoodsPriceWarnRuleGroup update(Long id, GoodsPriceWarnRuleGroupParam param) {
        GoodsPriceWarnRuleGroup goodsPriceWarnRuleGroup = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPriceWarnRuleGroup, "未查询到相关信息");
        if (param.getStatus() == null) {
            param.setStatus(goodsPriceWarnRuleGroup.getStatus());
        }
        ObjectUtils.copying(param, goodsPriceWarnRuleGroup);
        this.baseMapper.updateById(goodsPriceWarnRuleGroup);

        //保存预警通知人员
        this.addWarnUsers(id, param.getUserIds());

        return goodsPriceWarnRuleGroup;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        this.baseMapper.deleteById(id);
        //删除组下的规则
        goodsPriceWarnRuleService.remove(Wrappers.<GoodsPriceWarnRule>lambdaQuery()
                                                 .eq(GoodsPriceWarnRule::getGroupId, id));
        //删除规则明细warn_category
        goodsPriceWarnCategoryMapper.delete(Wrappers.<GoodsPriceWarnCategory>lambdaQuery()
                                                    .eq(GoodsPriceWarnCategory::getGroupId, id));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public GoodsPriceWarnRuleGroupVO getById(Long id) {
        GoodsPriceWarnRuleGroup goodsPriceWarnRuleGroup = this.baseMapper.selectById(id);
        Assert.isNotFound(goodsPriceWarnRuleGroup, "未找到对应的告警规则");
        GoodsPriceWarnRuleGroupVO groupVO = ObjectUtils.copying(goodsPriceWarnRuleGroup, GoodsPriceWarnRuleGroupVO.class);
        //查询规则信息
        List<GoodsPriceWarnRuleVO> rules = goodsPriceWarnRuleService.getByGroupId(id);
        groupVO.setRules(rules);
        //查询通知的人员
        List<GoodsPriceWarnUser> users = goodsPriceWarnUserMapper.selectList(Wrappers.<GoodsPriceWarnUser>lambdaQuery()
                                                                                     .eq(GoodsPriceWarnUser::getGroupId, id));
        if (CollectionUtils.isNotEmpty(users)) {
            groupVO.setUsers(ObjectUtils.copying(users, GoodsPriceWarnUserVO.class));
        }
        return groupVO;
    }

    @Override
    public Pagination<GoodsPriceWarnRuleGroupPageVO> getPage(GoodsPriceWarnRuleGroupQueryParam param) {
        //请自行添加查询条件
        Page<GoodsPriceWarnRuleGroup> page = this.lambdaQuery()
                .like(StringUtils.isNotBlank(param.getGroupName()), GoodsPriceWarnRuleGroup::getGroupName, param.getGroupName())
                .eq(param.getStatus() != null, GoodsPriceWarnRuleGroup::getStatus, param.getStatus())
                .orderByDesc(GoodsPriceWarnRuleGroup::getUpdateTime)
                .page(PagingUtils.convert(param));
        Pagination<GoodsPriceWarnRuleGroupPageVO> pagination = PagingUtils.convert(page, GoodsPriceWarnRuleGroupPageVO.class);
        if (CollectionUtils.isNotEmpty(pagination.getRecords())) {
            pagination.getRecords().stream().forEach(g -> {
                //查询规则
                List<GoodsPriceWarnRuleVO> rules = goodsPriceWarnRuleService.getByGroupId(g.getId());
                List<String> goodsCategoryNames = rules.stream().map(r -> r.getGoodsCategoryNames())
                        .filter(list -> CollectionUtils.isNotEmpty(list))
                        .flatMap(list -> list.stream()).collect(Collectors.toList());
                g.setCategories(goodsCategoryNames);
            });
        }
        return pagination;
    }
}