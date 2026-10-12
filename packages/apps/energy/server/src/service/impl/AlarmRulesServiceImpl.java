package com.guhecloud.cube.srv.gen.energymanagement.server.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.client.param.RuleModifyParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.adapter.servcieadapter.AdapterAlarmService;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.AlarmRulesMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.EnergyAlarmRuleMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.AlarmRules;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyAlarmRule;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.cube.srv.gen.energymanagement.server.enumeration.InstrumentTypeEnum;
import com.guhecloud.cube.srv.gen.energymanagement.server.param.AlarmRulesParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.AlarmRulesQueryParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.service.AlarmRulesService;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.AlarmRulesVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：报警规则配置服务层
 *
 * @author lrj
 * @date 2021-12-30 15:12:36
 */
@Slf4j
@Service
public class AlarmRulesServiceImpl extends ServiceImpl<AlarmRulesMapper, AlarmRules> implements AlarmRulesService {

    @Autowired
    private EnergyAlarmRuleMapper energyAlarmRuleMapper;
    @Autowired
    private MeterDeviceMapper meterDeviceMapper;
    @Autowired
    private AdapterAlarmService adapterAlarmService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(AlarmRulesParam param) {
        RuleModifyParam ruleModifyParam = new RuleModifyParam();
        ruleModifyParam.setBasicModify(param.getBasicModify());
        ruleModifyParam.setLinkageEmergencyActionModifies(param.getLinkageEmergencyActionModifies());
        ruleModifyParam.setRuleConfig(JSON.toJSONString(param.getAlarmRulesBasicParam()));
        Long save = adapterAlarmService.save(ruleModifyParam);
        if (save != null || save.longValue() != 0) {
            AlarmRules alarmRules = ObjectUtils.copying(param.getAlarmRulesBasicParam(), AlarmRules.class);
            alarmRules.setMessageId(save);
            this.baseMapper.insert(alarmRules);
            //绑定告警设备
            if (!CollectionUtils.isEmpty(param.getAlarmRulesBasicParam().getDeviceIds())) {
                this.bindAlarmDevice(alarmRules.getId(), param.getAlarmRulesBasicParam().getDeviceIds());
            }
            return alarmRules.getId();
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, AlarmRulesParam param) {
        AlarmRules alarmRules = this.baseMapper.selectById(id);
        Assert.isNotFound(alarmRules, "未查询到相关信息");
        //保存报警消息内容
        RuleModifyParam ruleModifyParam = new RuleModifyParam();
        ruleModifyParam.setBasicModify(param.getBasicModify());
        ruleModifyParam.setLinkageEmergencyActionModifies(param.getLinkageEmergencyActionModifies());
        ruleModifyParam.setRuleConfig(JSON.toJSONString(param.getAlarmRulesBasicParam()));
        adapterAlarmService.update(alarmRules.getMessageId(), ruleModifyParam);

        ObjectUtils.copying(param.getAlarmRulesBasicParam(), alarmRules);
        this.baseMapper.updateById(alarmRules);
        if (!CollectionUtils.isEmpty(param.getAlarmRulesBasicParam().getDeviceIds())) {
            //删除之前配置项
            energyAlarmRuleMapper.deleteByRulesId(id);
            //重新进行绑定
            this.bindAlarmDevice(alarmRules.getId(), param.getAlarmRulesBasicParam().getDeviceIds());
        }
    }

    /**
     * 修改告警配置状态
     *
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Boolean status) {
        AlarmRules alarmRules = this.baseMapper.selectById(id);
        Assert.isNotFound(alarmRules, "未查询到相关信息");

        adapterAlarmService.updateEnabled(alarmRules.getMessageId(), status);

        this.lambdaUpdate().set(AlarmRules::getEnabled, status).eq(AlarmRules::getId, id).update();

    }

    @Override
    public List<AlarmRules> getRules(Long id, Integer tenant) {

        return energyAlarmRuleMapper.getDeviceRules(id, tenant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        AlarmRules alarmRules = this.lambdaQuery().eq(AlarmRules::getId, id).one();
        adapterAlarmService.remove(alarmRules.getMessageId());
        DatabaseContextHolder.softDelete(this, id);
        //删除之前配置项
        energyAlarmRuleMapper.deleteByRulesId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public AlarmRulesVO getById(Long id) {
        //获取基本信息
        AlarmRules alarmRules = this.baseMapper.selectById(id);
        AlarmRulesVO alarmRulesVO = ObjectUtils.copying(alarmRules, AlarmRulesVO.class);
        if (StringUtils.isNotEmpty(alarmRules.getAlarmLevelName())) {
            alarmRulesVO.setAlarmLevelName(alarmRules.getAlarmLevelName());
        }
        if (StringUtils.isNotEmpty(alarmRules.getAlarmTypeName())) {
            alarmRulesVO.setAlarmTypeName(alarmRules.getAlarmTypeName());
        }
        //关联表中查询所有的关联设备
        List<EnergyAlarmRule> energyAlarmRules = energyAlarmRuleMapper.selectList(new LambdaQueryWrapper<EnergyAlarmRule>().eq(EnergyAlarmRule::getRulesId, alarmRulesVO.getId()));
        if (!CollectionUtils.isEmpty(energyAlarmRules)) {
            List<MeterDevice> meterDeviceList = new ArrayList<>();
            energyAlarmRules.forEach(e -> {
                MeterDevice meterDevice = meterDeviceMapper.selectOne(new LambdaQueryWrapper<MeterDevice>().eq(MeterDevice::getDeviceId, e.getDeviceId()));
                meterDevice.setDeviceType(InstrumentTypeEnum.getFireinspEnum(meterDevice.getInstrumentType()));
                meterDeviceList.add(meterDevice);
            });
            alarmRulesVO.setMeterDeviceList(meterDeviceList);
        }
        alarmRulesVO.setEmergencyActionVOS(adapterAlarmService.getLinkageActions(alarmRulesVO.getMessageId()));
        return alarmRulesVO;
    }

    @Override
    public Pagination<AlarmRulesVO> getPage(AlarmRulesQueryParam param) {
        //请自行添加查询条件
        Page<AlarmRules> page = this.lambdaQuery()
                .eq(StringUtils.isNotEmpty(param.getAlarmLevel()), AlarmRules::getAlarmLevel, param.getAlarmLevel())
                .like(StringUtils.isNotEmpty(param.getAlarmName()), AlarmRules::getAlarmName, param.getAlarmName())
                .page(PagingUtils.convert(param));
        Pagination<AlarmRulesVO> rulesVOPagination = PagingUtils.convert(page, AlarmRulesVO.class);
        //统计报警事件所配置的设备
        rulesVOPagination.getRecords().forEach(e -> {
            Long count = energyAlarmRuleMapper.selectCount(new LambdaQueryWrapper<EnergyAlarmRule>().eq(EnergyAlarmRule::getRulesId, e.getId()));
            e.setDeviceNum(count);
        });
        return rulesVOPagination;
    }

    /**
     * 绑定告警设备
     *
     * @param rulesId
     * @param deviceIds
     */
    public void bindAlarmDevice(Long rulesId, List<String> deviceIds) {
        //绑定告警设备
        deviceIds.forEach(id -> {
            MeterDevice meterDevice = meterDeviceMapper.selectOne(new LambdaQueryWrapper<MeterDevice>().eq(MeterDevice::getId, id));
            EnergyAlarmRule energyAlarmRule = new EnergyAlarmRule();
            energyAlarmRule.setDeviceId(meterDevice.getDeviceId());
            energyAlarmRule.setRulesId(rulesId);
            energyAlarmRuleMapper.insert(energyAlarmRule);
        });

    }
}