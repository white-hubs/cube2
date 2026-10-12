package com.guhecloud.cube.srv.gen.property.management.server.risk.source.fire.controller;

import com.guhecloud.cube.base.dictionary.client.CascadeDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.CascadeDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.PropertyTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jakarta.validation.Valid;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 消防安全风险源
 *
 * @author lanchun
 * @date 2025/3/18 13:41
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceFireController {

    private final RiskSourceLedgerService riskSourceLedgerService;

    private final CascadeDictionaryClient cascadeDictionaryClient;

    /**
     * 获取风险源消防分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/risk/source/ledger/fire/page")
    public Pagination<RiskSourceLedgerVO> getFireRiskSourcePage(@Valid RiskSourceLedgerPageParam param) {
        param.setTypeCodes(getFireDictionaryCodes());
        return this.riskSourceLedgerService.queryByPage(param);
    }

    /**
     * 下载风险源消防导入模板
     *
     * @param request
     * @param response
     */
    @GetMapping("/risk/source/ledger/fire/getTemplateExcel")
    public void getTemplateExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            riskSourceLedgerService.getTemplateExcel(getFireDictionary(), request, response);
        } catch (Exception e) {
            log.error("消防导入模板下载失败", e);
            throw new ServiceException("消防导入模板下载失败，请联系管理员");
        }
    }

    public List<CascadeDictionaryDTO> getFireDictionary() {
        CascadeDictionaryParam dictionaryParam = new CascadeDictionaryParam();
        dictionaryParam.setItemCode("risk-source-type");
        dictionaryParam.setCodes(getFireDictionaryCodes());
        return cascadeDictionaryClient.getAllCascadeDictionaryItem(dictionaryParam);
    }

    public Set<String> getFireDictionaryCodes() {
        Set<String> codes = new HashSet<>();
        codes.add(PropertyTypeEnum.ANNIHILATOR.name());
        codes.add(PropertyTypeEnum.FIRE_HYDRANT.name());
        return codes;
    }
}
