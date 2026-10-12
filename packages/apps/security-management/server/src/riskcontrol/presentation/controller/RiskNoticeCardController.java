package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskNoticeCardPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskNoticeCardVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.presentation.service.RiskPresentationQueryService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 风险告知卡接口。
 */
@RestController("securityManagementRiskNoticeCardController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskNoticeCardController {
    private final RiskPresentationQueryService queryService;

    /**
     * 查询风险告知卡列表。
     *
     * @param param 查询条件，keyword 按单元名称模糊匹配，riskLevel 按现状风险等级筛选
     * @return 告知卡分页结果，包含警示标志、风险对象和管控措施
     */
    @GetMapping("/risk-control/notice-cards")
    public Pagination<RiskNoticeCardVO> noticeCards(RiskNoticeCardPageParam param) {
        return queryService.noticeCards(param);
    }

    /**
     * 导出风险告知卡列表。
     *
     * @param param 导出条件，keyword 按单元名称模糊匹配，riskLevel 按现状风险等级筛选
     * @param response HTTP 响应，用于写出 xlsx 文件流
     */
    @GetMapping("/risk-control/notice-cards/export")
    public void exportNoticeCards(RiskNoticeCardPageParam param, HttpServletResponse response) {
        queryService.exportNoticeCards(param, response);
    }

    /**
     * 查询风险告知卡详情。
     *
     * @param unitId 风险单元 ID
     * @return 告知卡详情，包含警示标志、风险对象和管控措施
     */
    @GetMapping("/risk-control/notice-cards/{unitId}")
    public RiskNoticeCardVO noticeCardDetail(@PathVariable Long unitId) {
        return queryService.noticeCardDetail(unitId);
    }
}
