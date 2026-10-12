package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalNormRecord;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.MonitorRecordStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.RuleNormStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.MedicalRuleMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.RuleNormMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.RuleNorm;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleNormPageParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleNormParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.RuleNormService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleNormVO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指标设置(rule_norm)表服务实现类
 *
 * @author : lijie
 * @date : 2024-3-20
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RuleNormServiceImpl extends ServiceImpl<RuleNormMapper, RuleNorm> implements RuleNormService {
    private final RuleNormMapper ruleNormMapper;
    private final MedicalRuleMapper medicalRuleMapper;
    private final DataDictionaryAdapter dataDictionaryAdapter;

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     *
     * @author : lijie
     */
    public RuleNormVO queryById(Long id) {
        RuleNorm ruleNorm = this.baseMapper.selectById(id);
        return ConvertUtils.convert(RuleNormVO.class, ruleNorm);
    }

    @Override
    public RuleNormVO queryByCode(String code) {
        RuleNorm ruleNorm = this.baseMapper.selectOne(new LambdaQueryWrapper<RuleNorm>()
                                                              .eq(RuleNorm::getNormCode, code)
                                                              .last("limit 1"));
        return ConvertUtils.convert(RuleNormVO.class, ruleNorm);
    }

    /**
     * 分页查询
     *
     * @param param: 筛选条件
     *
     * @return 查询结果
     *
     * @author : lijie
     */
    public Pagination<RuleNormVO> queryByPage(RuleNormPageParam param) {
        Page<RuleNorm> page = new Page<>(param.getPageNumber(), param.getPageSize());
        this.baseMapper.selectPage(page, new LambdaQueryWrapper<RuleNorm>());
        Pagination<RuleNormVO> pagination = new Pagination<>(page.getTotal(), page.getCurrent(), ConvertUtils.toList(RuleNormVO.class, page.getRecords()));
        return pagination;
    }

    @Override
    public List<RuleNormVO> queryAllByRuleId(Long ruleId) {
        List<RuleNorm> ruleNormList = this.baseMapper.selectList(new LambdaQueryWrapper<RuleNorm>()
                                                                         .eq(RuleNorm::getRuleId, ruleId));
        return ConvertUtils.toList(RuleNormVO.class, ruleNormList);
    }

    /**
     * 新增数据
     *
     * @param param 新增数据
     *
     * @return 上下限是否修改
     *
     * @author : lijie
     */
    public Boolean insertBatch(List<RuleNormParam> param, Long ruleId) {
        List<RuleNorm> ruleNorm = ConvertUtils.toList(RuleNorm.class, param);
        for (RuleNorm ex : ruleNorm) {
            ex.setRuleId(ruleId);
        }
        this.deleteByRuleId(ruleId);
        return this.saveBatch(ruleNorm);
    }

    /**
     * 通过规则id删除数据
     *
     * @param ruleId 主键
     *
     * @return 删除结果
     *
     * @author : lijie
     */
    public Boolean deleteByRuleId(Long ruleId) {
        return this.baseMapper.delete(new LambdaQueryWrapper<RuleNorm>().eq(RuleNorm::getRuleId, ruleId)) > 0;
    }

    @Override
    public Map<String, String> getNormStatus(List<MedicalNormRecord> recordList, Integer tenant, Long deviceId) {
        Map<String, String> map = new HashMap<>(2);
        Map<String, RuleNormVO> normMap = new HashMap<>();
        String status = MonitorRecordStatusEnum.NORMAL.name();
        List<RuleNormVO> ruleNormList = this.medicalRuleMapper.getAllRule(null, tenant, deviceId);
        for (RuleNormVO ex : ruleNormList) {
            normMap.put(ex.getTenant() + ex.getNormCode(), ex);
        }
        for (MedicalNormRecord record : recordList) {
            RuleNormVO ruleNormVO = normMap.get(tenant + record.getNormCode());
            RuleNorm ruleNorm = ConvertUtils.convert(RuleNorm.class, ruleNormVO);
            if (ObjectUtils.isNotEmpty(ruleNorm)) {
                Boolean lowerLimit = ObjectUtils.isNotEmpty(ruleNorm.getLowerLimit()) && ObjectUtils.isNotEmpty(record.getValue()) &&
                                     ruleNorm.getLowerLimit().compareTo(new BigDecimal(record.getValue())) > 0;
                Boolean upperLimit = ObjectUtils.isNotEmpty(ruleNorm.getUpperLimit()) && ObjectUtils.isNotEmpty(record.getValue()) &&
                                     ruleNorm.getUpperLimit().compareTo(new BigDecimal(record.getValue())) < 0;
                record.setStatus(MonitorRecordStatusEnum.NORMAL.name());
                if (lowerLimit || upperLimit) {
                    record.setStatus(lowerLimit ? RuleNormStatusEnum.LOWER_LIMIT.name() : RuleNormStatusEnum.UPPER_LIMIT.name());
                }
            }
        }
        map.put("valus", JsonUtils.serialize(recordList));
        map.put("status", status);
        return map;
    }

}