package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.entity.MedicalRuleNotifyConfig;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.param.MedicalRuleNotifyConfigParam;

import java.util.Map;
import java.util.Set;

public interface MedicalRuleNotifyConfigService extends IService<MedicalRuleNotifyConfig> {

    Boolean save(MedicalRuleNotifyConfigParam param);

    MedicalRuleNotifyConfig getMedicalRuleNotifyConfig();

    /**
     * @description:
     * @author: lijie
     * @date: 2024/9/19 13:40
     * @param: tmpParams 模板参数
     * @param: messageRecordId 消息记录id
     * @param: orgIds 组织id
     **/
    void sendMessage(Map<String, String> tmpParams, String messageRecordId, Set<Long> orgIds);
}
