package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.MedicalRuleMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRule;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleAllParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RulePageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.RuleNormService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RulePageVO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleVO;
import com.guhecloud.cube.srv.gen.securitymanagement.client.SecurityDeviceClient;
import com.guhecloud.cube.srv.gen.securitymanagement.param.InsideSecurityDeviceParam;
import com.guhecloud.cube.srv.gen.securitymanagement.vo.DeviceMonitoringVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * (rule)表服务实现类
 *
 * @author : lijie
 * @date : 2024-3-20
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalMedicalRuleServiceImpl extends ServiceImpl<MedicalRuleMapper, MedicalRule> implements MedicalRuleService {
    private final RuleNormService ruleNormService;
    private final FileClient fileClient;
    private final FileRefClient fileRefClient;
    private final SecurityDeviceClient securityDeviceClient;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     *
     * @author : lijie
     */
    public RuleVO queryById(Long id) {
        MedicalRule medicalRule = this.baseMapper.selectById(id);
        RuleVO ruleVO = ConvertUtils.convert(RuleVO.class, medicalRule);
        if (ObjectUtils.isNotEmpty(ruleVO.getDeviceIds())) {
            List<Long> deviceIdList = Arrays.stream(ruleVO.getDeviceIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            InsideSecurityDeviceParam param = new InsideSecurityDeviceParam();
            param.setApplicationCode("OPERATING_ROOM");
            param.setDeviceIds(deviceIdList);
            List<DeviceMonitoringVo> deviceMonitoringVos = securityDeviceClient.getDeviceMonitoringList(param);
            ruleVO.setDeviceDTO(deviceMonitoringVos);
        }
        List<FileVO> file = fileClient.getByBusinessKey(ruleVO.getId());
        if (ObjectUtils.isNotEmpty(file)) {
            ruleVO.setFile(file.get(0));
        }
        ruleVO.setRuleNormList(ruleNormService.queryAllByRuleId(id));
        return ruleVO;
    }

    @Override
    public RuleVO queryByIotRuleId(Long iotRuleId) {
        MedicalRule medicalRule = this.baseMapper.selectOne(new LambdaQueryWrapper<MedicalRule>().eq(MedicalRule::getIotRuleId, iotRuleId));
        if (Objects.isNull(medicalRule)) {
            log.warn("洁净手术室报警规则触发失败,规则不存在,规则id:{}", iotRuleId);
            return null;
        }
        return this.queryById(medicalRule.getId());
    }

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     *
     * @author : lijie
     */
    public Pagination<RulePageVO> queryByPage(RulePageParam param) {
        Page<RulePageVO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.queryByPage(page, param);
        page.getRecords().stream().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getDeviceIds())) {
                String[] ids = ex.getDeviceIds().split(",");
                ex.setAreaNumber(ids.length + "个");
                ex.setRuleNormNumber(ex.getRuleNormNumber() + "个");
            }
        });
        Pagination<RulePageVO> pagination = new Pagination<>(page.getTotal(), page.getCurrent(), page.getRecords());
        return pagination;
    }

    @Override
    public List<RuleVO> queryByAll(RuleAllParam param) {
        List<RuleVO> ruleNormVOList = new ArrayList<>();
        if (ObjectUtils.isEmpty(param.getDeviceIds())) {
            List<RuleVO> ruleNorm = this.baseMapper.queryByDeviceIds(null);
            if (ObjectUtils.isNotEmpty(ruleNorm)) {
                ruleNormVOList.addAll(ruleNorm);
            }
        } else {
            List<String> deviceIdList = Arrays.stream(param.getDeviceIds().split(","))
                    .toList();
            deviceIdList.forEach(ex -> {
                List<RuleVO> ruleNorm = this.baseMapper.queryByDeviceIds(ex);
                if (ObjectUtils.isNotEmpty(ruleNorm)) {
                    ruleNormVOList.addAll(ruleNorm);
                }
            });
        }
        ruleNormVOList.forEach(ex -> {
            List<FileVO> file = fileClient.getByBusinessKey(ex.getId());
            if (ObjectUtils.isNotEmpty(file)) {
                ex.setFile(file.getFirst());
            }
        });
        return ruleNormVOList;
    }

    @Override
    public List<RuleVO> queryByAll() {
        List<MedicalRule> ruleNorm = this.baseMapper.selectList(new LambdaQueryWrapper<>(MedicalRule.class));
        return ConvertUtils.toList(RuleVO.class, ruleNorm);
    }

    /**
     * 新增数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    public Long insert(RuleParam param) {
        // 判断区域是否已经绑定规则
        this.alreadyBound(param.getDeviceIds());
        MedicalRule medicalRule = ConvertUtils.convert(MedicalRule.class, param);
        medicalRule.setId(IdUtils.next());
        var bool = this.baseMapper.insert(medicalRule) > 0;
        if (bool && ObjectUtils.isNotEmpty(param.getRuleNormList())) {
            ruleNormService.insertBatch(param.getRuleNormList(), medicalRule.getId());
        }
        fileRefClient.removeByBusinessKey(medicalRule.getId());
        if (ObjectUtils.isNotEmpty(param.getFile())) {
            fileRefClient.save(medicalRule.getId(), param.getFile());
        }
        return medicalRule.getId();
    }

    /**
     * 更新数据
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    public Boolean update(RuleParam param) {
        this.alreadyBound(param.getId(), param.getDeviceIds());
        MedicalRule medicalRule = this.baseMapper.selectById(param.getId());
        medicalRule.setName(param.getName());
        medicalRule.setDeviceIds(param.getDeviceIds());
        medicalRule.setAreaOptions(param.getAreaOptions());
        Boolean bool = this.baseMapper.updateById(medicalRule) > 0;
        if (bool && ObjectUtils.isNotEmpty(param.getRuleNormList())) {
            bool = ruleNormService.insertBatch(param.getRuleNormList(), medicalRule.getId());
        }
        fileRefClient.removeByBusinessKey(param.getId());
        if (ObjectUtils.isNotEmpty(param.getFile())) {
            fileRefClient.save(param.getId(), param.getFile());
        }
        return bool;
    }

    @Override
    public Boolean updateByIotRuleId(RuleParam param) {
        return super.update(new LambdaUpdateWrapper<MedicalRule>()
                                    .set(MedicalRule::getIotRuleId, param.getIotRuleId())
                                    .eq(MedicalRule::getId, param.getId()));
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    public Boolean deleteById(Long id) {
        return DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public Set<Long> getMedicalRuleIds() {
        return this.baseMapper.queryRuleIds();
    }

    @Override
    public void getNormAllByDevice() {

    }

    /**
     * 判断区域是否已经绑定规则
     */
    private void alreadyBound(String deviceIds) {
        this.alreadyBound(null, deviceIds);
    }

    private void alreadyBound(Long id, String deviceIds) {
        List<String> areaId = Arrays.asList(deviceIds.split(","));
        areaId.forEach(ex -> {
            List<MedicalRule> medicalRule = this.baseMapper.selectList(new LambdaQueryWrapper<MedicalRule>()
                                                                               .like(MedicalRule::getDeviceIds, ex));
            if (ObjectUtils.isNotEmpty(id)) {
                if (medicalRule.size() == 1) {
                    Assert.isConflict(!medicalRule.getFirst().getId().equals(id), "该设备已经绑定规则");
                }
            } else {
                Assert.isConflict(CollectionUtils.isNotEmpty(medicalRule), "该设备已经绑定规则");
            }
        });
    }
}
