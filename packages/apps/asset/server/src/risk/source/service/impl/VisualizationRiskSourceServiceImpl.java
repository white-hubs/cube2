package com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.base.dictionary.client.CascadeDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.CascadeDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryParam;
import com.guhecloud.cube.property.management.client.common.vo.RiskSourceTypeVO;
import com.guhecloud.cube.property.management.client.common.vo.RiskSourceVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.dao.RiskSourceLedgerMapper;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.entity.RiskSourceLedger;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.param.RiskSourceRecordParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.VisualizationRiskSourceService;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-15  11:16
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class VisualizationRiskSourceServiceImpl implements VisualizationRiskSourceService {
    private final RiskSourceLedgerMapper riskSourceLedgerMapper;
    private final CascadeDictionaryClient cascadeDictionaryClient;

    @Override
    public RiskSourceVO archiveStatistics() {
        RiskSourceVO riskSourceVO = new RiskSourceVO();
        Long totalNum = riskSourceLedgerMapper.selectCount(new LambdaQueryWrapper<>());
        Long expirationNum = riskSourceLedgerMapper.selectCount(new LambdaQueryWrapper<RiskSourceLedger>()
                                                                        .le(RiskSourceLedger::getUsageDate, DateUtils.now()));
        riskSourceVO.setTotalNum(Optional.ofNullable(totalNum).orElse(0L));
        riskSourceVO.setExpirationNum(Optional.ofNullable(expirationNum).orElse(0L));
        List<RiskSourceTypeVO> typeVOList = new ArrayList<>();
        RiskSourceTypeVO riskSourceTypeVO = new RiskSourceTypeVO();
        riskSourceTypeVO.setTypeName("其他");
        Long total = 0L;
        String typeCode = "";

        CascadeDictionaryParam dictionaryParam = new CascadeDictionaryParam();
        dictionaryParam.setItemCode("risk-source-type");
        List<CascadeDictionaryDTO> cascadeDictionaryDTOList = cascadeDictionaryClient.getCascadeDictionaryItem(dictionaryParam);
        List<String> types = new ArrayList<>();
        cascadeDictionaryDTOList.forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getNextList())) {
                ex.getNextList().forEach(item -> {
                    types.add(item.getCode());
                });
            }
        });
        if (ObjectUtils.isNotEmpty(types)) {
            List<RiskSourceTypeVO> typeList = riskSourceLedgerMapper.getAllGroupTypeCode(types);
            for (RiskSourceTypeVO ex : typeList) {
                if (typeVOList.size() < 3) {
                    typeVOList.add(ex);
                } else {
                    total += ex.getTotalNum();
                    if (ObjectUtils.isNotEmpty(typeCode)) {
                        typeCode = typeCode + "/";
                    }
                    typeCode = typeCode + ex.getTypeCode();
                }
            }
        }
        riskSourceTypeVO.setTypeCode(typeCode);
        riskSourceTypeVO.setTotalNum(total);
        if (riskSourceTypeVO.getTotalNum() > 0) {
            typeVOList.add(riskSourceTypeVO);
        }
        riskSourceVO.setTypeVOList(typeVOList);
        return riskSourceVO;
    }

    @Override
    public List<RiskSourceTypeVO> riskSourceRecord(RiskSourceRecordParam param) {
        List<RiskSourceTypeVO> riskSourceTypeVOS = new ArrayList<>();
        if (ObjectUtils.isEmpty(param.getTypeCodes())) {
            return riskSourceTypeVOS;
        }
        List<CascadeDictionaryDTO> dictionaryData = this.getDictionaryData();
        if (ObjectUtils.isEmpty(dictionaryData)) {
            return riskSourceTypeVOS;
        }
        param.getTypeCodes().forEach(ex -> {
            List<String> riskSourceTypeCodes = new ArrayList<>();
            dictionaryData.forEach(item -> {
                if (item.getCode().equals(ex)) {
                    riskSourceTypeCodes.add(ex);
                    this.getRiskSourceTypeCode(riskSourceTypeCodes, item);
                } else {
                    this.getRiskSourceTypeCode(riskSourceTypeCodes, item, ex);
                }
            });
            Long total = 0L;
            if (ObjectUtils.isNotEmpty(riskSourceTypeCodes)) {
                total = riskSourceLedgerMapper.selectCount(new LambdaQueryWrapper<RiskSourceLedger>()
                                                                   .in(RiskSourceLedger::getTypeCode, riskSourceTypeCodes)
                                                                   .lt(ObjectUtils.isNotEmpty(param.getExpireTime()),
                                                                       RiskSourceLedger::getUsageDate, param.getExpireTime()));
            }
            riskSourceTypeVOS.add(RiskSourceTypeVO.builder().typeCode(ex).totalNum(Optional.ofNullable(total).orElse(0L)).build());
        });
        return riskSourceTypeVOS;
    }

    /**
     * 获取风险源所有类型
     */
    private List<CascadeDictionaryDTO> getDictionaryData() {
        CascadeDictionaryParam dictionaryParam = new CascadeDictionaryParam();
        dictionaryParam.setItemCode("risk-source-type");
        return cascadeDictionaryClient.getCascadeDictionaryItem(dictionaryParam);
    }

    /**
     * 递归获取所有类型code
     */
    private void getRiskSourceTypeCode(List<String> typeCodes, CascadeDictionaryDTO dictionaryData, String typeCode) {
        if (typeCode.equals(dictionaryData.getCode())) {
            typeCodes.add(typeCode);
        }
        if (ObjectUtils.isNotEmpty(dictionaryData.getNextList())) {
            dictionaryData.getNextList().forEach(item -> {
                if (typeCode.equals(item.getCode())) {
                    typeCodes.add(item.getCode());
                    this.getRiskSourceTypeCode(typeCodes, item);
                } else {
                    this.getRiskSourceTypeCode(typeCodes, item, typeCode);
                }
            });
        }
    }

    private void getRiskSourceTypeCode(List<String> typeCodes, CascadeDictionaryDTO dictionaryData) {
        if (ObjectUtils.isNotEmpty(dictionaryData.getNextList())) {
            dictionaryData.getNextList().forEach(item -> {
                typeCodes.add(item.getCode());
                this.getRiskSourceTypeCode(typeCodes, item);
            });
        }
    }
}
