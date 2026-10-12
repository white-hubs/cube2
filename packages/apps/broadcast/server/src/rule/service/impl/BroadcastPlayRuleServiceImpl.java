package com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.dao.BroadcastPlayRuleMapper;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param.BroadcastPlayRuleModifyParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.param.BroadcastPlayRuleQueryParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po.BroadcastPlayRuleDevicePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po.BroadcastPlayRulePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.po.BroadcastPlayRuleTemplatePO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.BroadcastPlayRuleDeviceService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.BroadcastPlayRuleService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.service.BroadcastPlayRuleTemplateService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRuleDeviceVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRulePageVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRuleTemplateVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.rule.vo.BroadcastPlayRuleVO;
import com.guhecloud.platform.base.model.Pagination;
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

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastPlayRuleServiceImpl extends ServiceImpl<BroadcastPlayRuleMapper, BroadcastPlayRulePO> implements BroadcastPlayRuleService {

    private final BroadcastPlayRuleTemplateService broadcastPlayRuleTemplateService;

    private final BroadcastPlayRuleDeviceService broadcastPlayRuleDeviceService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull BroadcastPlayRuleModifyParam param) {
        BroadcastPlayRulePO broadcastPlayRulePO = ObjectUtils.copying(param, BroadcastPlayRulePO.class);
        this.baseMapper.insert(broadcastPlayRulePO);
        saveTemplate(param, broadcastPlayRulePO.getId());
        saveDevices(param, broadcastPlayRulePO.getId());
        return broadcastPlayRulePO.getId();
    }

    private void saveDevices(@NonNull BroadcastPlayRuleModifyParam param, Long ruleId) {
        if (!CollectionUtils.isEmpty(param.getDevices())) {
            List<BroadcastPlayRuleDevicePO> broadcastPlayRuleDevicePOS = param.getDevices().stream().map(device -> {
                BroadcastPlayRuleDevicePO broadcastPlayRuleDevicePO = ObjectUtils.copying(device, BroadcastPlayRuleDevicePO.class);
                broadcastPlayRuleDevicePO.setRuleId(ruleId);
                return broadcastPlayRuleDevicePO;
            }).collect(Collectors.toList());
            this.broadcastPlayRuleDeviceService.saveBatch(broadcastPlayRuleDevicePOS);
        }
    }

    private void saveTemplate(@NonNull BroadcastPlayRuleModifyParam param, Long ruleId) {
        if (!CollectionUtils.isEmpty(param.getTemplates())) {
            List<BroadcastPlayRuleTemplatePO> broadcastPlayRuleTemplatePOS = param.getTemplates().stream().map(template -> {
                BroadcastPlayRuleTemplatePO broadcastPlayRuleTemplatePO = ObjectUtils.copying(template, BroadcastPlayRuleTemplatePO.class);
                broadcastPlayRuleTemplatePO.setRuleId(ruleId);
                return broadcastPlayRuleTemplatePO;
            }).collect(Collectors.toList());
            this.broadcastPlayRuleTemplateService.saveBatch(broadcastPlayRuleTemplatePOS);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull BroadcastPlayRuleModifyParam param) {
        BroadcastPlayRulePO broadcastPlayRulePO = this.baseMapper.selectById(id);
        if (Objects.isNull(broadcastPlayRulePO)) {
            return;
        }
        this.broadcastPlayRuleDeviceService.removeByRuleId(id);
        this.broadcastPlayRuleTemplateService.removeByRuleId(id);
        broadcastPlayRulePO.setName(param.getName());
        broadcastPlayRulePO.setRemark(param.getRemark());
        this.baseMapper.updateById(broadcastPlayRulePO);
        saveTemplate(param, broadcastPlayRulePO.getId());
        saveDevices(param, broadcastPlayRulePO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEnable(@NonNull Long id, @NonNull Boolean enabled) {
        BroadcastPlayRulePO broadcastPlayRulePO = this.baseMapper.selectById(id);
        if (Objects.nonNull(broadcastPlayRulePO)) {
            broadcastPlayRulePO.setEnabled(enabled);
            this.baseMapper.updateById(broadcastPlayRulePO);
        }
    }

    @Override
    public Pagination<BroadcastPlayRulePageVO> getPage(BroadcastPlayRuleQueryParam param) {
        Page<BroadcastPlayRulePO> broadcastPlayRulePOPage = this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers
                                                                                               .<BroadcastPlayRulePO>lambdaQuery()
                                                                                               .eq(Objects.nonNull(param.getEnabled()), BroadcastPlayRulePO::getEnabled, param.getEnabled())
                                                                                               .like(StringUtils.isNotBlank(param.getKeyword()), BroadcastPlayRulePO::getName, param.getKeyword())
                                                                                               .orderByDesc(BroadcastPlayRulePO::getCreateTime)
                                                                                      );
        return PagingUtils.convert(broadcastPlayRulePOPage, BroadcastPlayRulePageVO.class);
    }

    @Override
    public BroadcastPlayRuleVO getDetail(@NonNull Long id) {
        BroadcastPlayRulePO broadcastPlayRulePO = this.baseMapper.selectById(id);
        if (Objects.isNull(broadcastPlayRulePO)) {
            return null;
        }
        BroadcastPlayRuleVO broadcastPlayRuleVO = ObjectUtils.copying(broadcastPlayRulePO, BroadcastPlayRuleVO.class);
        List<BroadcastPlayRuleTemplatePO> broadcastPlayRuleTemplates = this.broadcastPlayRuleTemplateService.getListByRuleId(id);
        List<BroadcastPlayRuleDevicePO> broadcastPlayRuleDevices = this.broadcastPlayRuleDeviceService.getListByRuleId(id);
        broadcastPlayRuleVO.setTemplates(ObjectUtils.copying(broadcastPlayRuleTemplates, BroadcastPlayRuleTemplateVO.class));
        broadcastPlayRuleVO.setDevices(ObjectUtils.copying(broadcastPlayRuleDevices, BroadcastPlayRuleDeviceVO.class));
        return broadcastPlayRuleVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeById(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
        this.broadcastPlayRuleDeviceService.removeByRuleId(id);
        this.broadcastPlayRuleTemplateService.removeByRuleId(id);
    }
}
