package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.client.MsgRecordClient;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.dao.MedicalRuleTriggerMapper;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleTrigger;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.RuleTriggerParam;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleTriggerNormService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service.MedicalRuleTriggerService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.vo.RuleTriggerVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalRuleTriggerServiceImpl extends ServiceImpl<MedicalRuleTriggerMapper, MedicalRuleTrigger> implements MedicalRuleTriggerService {

    private final MedicalRuleTriggerNormService medicalRuleTriggerNormService;
    private final MsgRecordClient msgRecordClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@Nonnull RuleTriggerParam param) {
        MedicalRuleTrigger medicalRuleTrigger = convertMedicalRuleTrigger(param);
        this.baseMapper.insert(medicalRuleTrigger);
        if (!CollectionUtils.isEmpty(param.getNorms())) {
            medicalRuleTriggerNormService.saveBatch(medicalRuleTrigger.getId(), param.getNorms());
        }
        return medicalRuleTrigger.getId();
    }

    @Override
    public RuleTriggerVO getDetail(@Nonnull Long id, Long messageId) {
        var medicalRuleTrigger = this.baseMapper.getAllById(id);
        if (ObjectUtils.isNull(medicalRuleTrigger)) {
            return null;
        }
        var msgRecordDTO = msgRecordClient.getMsgRecord(messageId);
        if (ObjectUtils.isNull(msgRecordDTO)) {
            return medicalRuleTrigger;
        }
        medicalRuleTrigger.setSendTime(msgRecordDTO.getSendTime());
        medicalRuleTrigger.setSourceTenantName(msgRecordDTO.getSourceTenantName());
        medicalRuleTrigger.setMsgContent(msgRecordDTO.getMsgContent());
        return medicalRuleTrigger;
    }

    public MedicalRuleTrigger convertMedicalRuleTrigger(RuleTriggerParam param) {
        return MedicalRuleTrigger.builder()
                .ruleId(param.getRuleId())
                .ruleName(param.getRuleName())
                .iotInstanceId(param.getIotInstanceId())
                .productId(param.getProductId())
                .deviceId(param.getDeviceId())
                .deviceName(param.getDeviceName())
                .departId(param.getDepartId())
                .departName(param.getDepartName())
                .build();
    }
}
