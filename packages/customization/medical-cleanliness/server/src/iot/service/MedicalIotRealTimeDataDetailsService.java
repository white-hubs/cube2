package com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.service;

import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.srv.gen.medical.cleanliness.server.iot.entity.MedicalIotRealTimeDataDetails;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-10-21  17:37
 * @Description:
 * @Version: 1.0
 */
public interface MedicalIotRealTimeDataDetailsService {
    boolean saveBatch(List<MedicalIotRealTimeDataDetails> param);

    /**
     * 判断指标状态
     */
    void getNormStatus(MedicalIotRealTimeDataDetails normRecord,List<DataDictionaryDTO> dictionaryList);
    /**删除指标*/
    void deleteNorm(Long deviceId);

}
