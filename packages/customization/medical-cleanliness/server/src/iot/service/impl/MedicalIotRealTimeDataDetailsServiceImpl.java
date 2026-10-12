package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.dao.MedicalIotRealTimeDataDetailsMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeDataDetails;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.MonitorRecordStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.RuleNormStatusEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.enumeration.RuleNormTypeEnum;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service.MedicalIotRealTimeDataDetailsService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.RuleNormMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.RuleNorm;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-22  14:07
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalIotRealTimeDataDetailsServiceImpl extends ServiceImpl<MedicalIotRealTimeDataDetailsMapper, MedicalIotRealTimeDataDetails> implements MedicalIotRealTimeDataDetailsService {
    private final RuleNormMapper ruleNormMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(List<MedicalIotRealTimeDataDetails> param) {
        if (ObjectUtils.isEmpty(param)) {
            return true;
        }
        super.remove(new LambdaQueryWrapper<MedicalIotRealTimeDataDetails>().eq(MedicalIotRealTimeDataDetails::getDeviceId, param.get(0)
                .getDeviceId()));
        return super.saveBatch(param);
    }

    @Override
    public void getNormStatus(MedicalIotRealTimeDataDetails normRecord, List<DataDictionaryDTO> dictionaryList) {
        String status = MonitorRecordStatusEnum.NORMAL.name();
        try {
            List<RuleNorm> ruleNormList = ruleNormMapper.getRuleNormsByDeviceId(normRecord.getDeviceId());
            if (ObjectUtils.isNotEmpty(ruleNormList)) {
                Map<String, RuleNorm> ruleNormMap = ruleNormList.stream()
                        .collect(Collectors.toMap(RuleNorm::getNormCode, RuleNorm -> RuleNorm));
                RuleNorm ruleNorm = ruleNormMap.get(normRecord.getRuleNormCode());
                normRecord.setRuleNormName(ruleNorm.getNormName());
                normRecord.setUnit(ruleNorm.getUnit());
                normRecord.setType(RuleNormTypeEnum.NORMAL.name());
                boolean lowerLimit = ObjectUtils.isNotEmpty(ruleNorm.getLowerLimit()) && ObjectUtils.isNotEmpty(normRecord.getValue()) &&
                                     ruleNorm.getLowerLimit().compareTo(normRecord.getValue()) > 0;
                boolean upperLimit = ObjectUtils.isNotEmpty(ruleNorm.getUpperLimit()) && ObjectUtils.isNotEmpty(normRecord.getValue()) &&
                                     ruleNorm.getUpperLimit().compareTo(normRecord.getValue()) < 0;
                if (lowerLimit) {
                    status = RuleNormStatusEnum.LOWER_LIMIT.name();
                } else if (upperLimit) {
                    status = RuleNormStatusEnum.UPPER_LIMIT.name();
                }
                this.setSpecialNorm(normRecord, dictionaryList);
            }
        } catch (Exception e) {
            log.error("指标状态数据解析报错", e);
        }
        normRecord.setStatus(status);
    }

    @Override
    public void deleteNorm(Long deviceId) {
        super.remove(new LambdaQueryWrapper<MedicalIotRealTimeDataDetails>().eq(MedicalIotRealTimeDataDetails::getDeviceId, deviceId));
    }

    private static final String NORM_CODE = "normCode";

    private void setSpecialNorm(MedicalIotRealTimeDataDetails normRecord, List<DataDictionaryDTO> dictionaryList) {
        if (ObjectUtils.isNotEmpty(dictionaryList)) {
            dictionaryList.forEach(item -> {
                Map<String, Object> values = JsonUtils.deserialize(item.getVariable(), new TypeReference<Map<String, Object>>() {});
                if (normRecord.getRuleNormCode().equals(values.get(NORM_CODE).toString())) {
                    normRecord.setType(RuleNormTypeEnum.SPECIAL.name());
                }
            });
        }
    }
}
