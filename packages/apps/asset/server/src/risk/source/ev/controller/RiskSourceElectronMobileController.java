package com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.controller;

import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.maintain.client.InspectRecordClient;
import com.guhecloud.cube.srv.gen.maintain.dto.TargetMaxRecordDTO;
import com.guhecloud.cube.srv.gen.maintain.vo.TargetMaxRecordVO;
import com.guhecloud.cube.srv.gen.property.management.server.common.enumeration.PropertyTypeEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.enumeration.EvInspectionItemTagEnum;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.param.RiskSourceLedgeEvStatQueryParam;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.service.RiskSourceLedgerElectronMobileService;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.ev.vo.ElectronMobileStatPageVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 风险源电动车分页列表
 *
 * @author lanchun
 * @date 2025/3/18 9:42
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceElectronMobileController {

    private final RiskSourceLedgerService riskSourceLedgerService;

    private final RiskSourceLedgerElectronMobileService riskSourceLedgerElectronMobileService;

    private final InspectRecordClient inspectRecordClient;

    /**
     * 获取风险源电动车分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/risk/source/ledger/electron-mobile/page")
    public Pagination<RiskSourceLedgerVO> getEvRiskSourcePage(@Valid RiskSourceLedgerPageParam param) {
        param.setTypeCode(PropertyTypeEnum.ELECTRO_MOBILE.name());
        return this.riskSourceLedgerService.queryByPage(param);
    }

    /**
     * 下载风险源电动车导入模板
     *
     * @param request
     * @param response
     */
    @GetMapping("/risk/source/ledger/electron-mobile/getTemplateExcel")
    public void getTemplateExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            riskSourceLedgerElectronMobileService.getTemplateExcel(request, response);
        } catch (Exception e) {
            log.error("电动车导入模板下载失败", e);
            throw new ServiceException("电动车导入模板下载失败，请联系管理员");
        }
    }

    /**
     * 电动车风险源导入
     *
     * @param file
     */
    @PostMapping("/risk/source/ledger/electron-mobile/import/template")
    public Boolean importTemplate(@RequestParam("file") MultipartFile file) {
        riskSourceLedgerElectronMobileService.importTemplate(file);
        return Boolean.TRUE;
    }

    /**
     * 电动车风险源总数
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/risk/source/ledger/electron-mobile/stat")
    public Long getElectronMobileCount(@Valid RiskSourceLedgeEvStatQueryParam param) {
        return riskSourceLedgerElectronMobileService.getElectronMobileCount(param);
    }

    /**
     * 电动车统计分页
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/risk/source/ledger/electron-mobile/stat/page")
    public Pagination<ElectronMobileStatPageVO> getElectronMobileStatPage(@Valid RiskSourceLedgerPageParam param) {
        param.setTypeCode(PropertyTypeEnum.ELECTRO_MOBILE.name());
        Pagination<RiskSourceLedgerVO> page = this.riskSourceLedgerService.queryByPage(param);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<ElectronMobileStatPageVO> statPage = ObjectUtils.copying(page.getRecords(), ElectronMobileStatPageVO.class);
        List<Long> ids = page.getRecords().stream().map(RiskSourceLedgerVO::getId).collect(Collectors.toList());
        List<String> tags = Arrays.stream(EvInspectionItemTagEnum.values())
                .map(EvInspectionItemTagEnum::name)
                .collect(Collectors.toList());
        TargetMaxRecordDTO targetMaxRecordDTO = new TargetMaxRecordDTO();
        targetMaxRecordDTO.setTargetIdList(ids);
        targetMaxRecordDTO.setTagList(tags);
        Map<Long, Map<String, TargetMaxRecordVO>> inspectErrorMap = inspectRecordClient.postTargetMaxError(targetMaxRecordDTO);
        statPage.forEach(stat -> {
            Map<String, TargetMaxRecordVO> targetMaxRecordVOMap = inspectErrorMap.get(stat.getId());
            if (!MapUtils.isEmpty(targetMaxRecordVOMap)) {
                TargetMaxRecordVO parkingMaxRecordVO = targetMaxRecordVOMap.get(EvInspectionItemTagEnum.PARKING.name());
                if (Objects.nonNull(parkingMaxRecordVO)) {
                    stat.setParkingNum(parkingMaxRecordVO.getCount());
                    stat.setLastParkingInspectItem(parkingMaxRecordVO.getItem());
                }
                TargetMaxRecordVO chargeMaxRecordVO = targetMaxRecordVOMap.get(EvInspectionItemTagEnum.CHARGE.name());
                if (Objects.nonNull(chargeMaxRecordVO)) {
                    stat.setChargeNum(chargeMaxRecordVO.getCount());
                    stat.setLastChargeInspectItem(chargeMaxRecordVO.getItem());
                }
            }
        });
        return PagingUtils.convert(page, statPage);
    }

}
