package com.guhecloud.cube.sn.meattrade.plugin.common.controller;

import com.guhecloud.cube.biz.meattrade.client.PorkArrangeAuditClient;
import com.guhecloud.cube.biz.meattrade.client.dto.PorkArrangeAuditVO;
import com.guhecloud.cube.biz.meattrade.client.param.PorkArrangeAuditQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.common.enumeration.PorkArrangePushDataStatusEnum;
import com.guhecloud.cube.sn.meattrade.plugin.common.param.PorkArrangeQueryParam;
import com.guhecloud.cube.sn.meattrade.plugin.common.service.PorkArrangePushDataService;
import com.guhecloud.cube.sn.meattrade.plugin.common.vo.PorkArrangeVO;
import com.guhecloud.cube.sn.meattrade.plugin.utils.DateUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PorkArrangePushController {

    private final PorkArrangePushDataService porkArrangePushDataService;

    private final PorkArrangeAuditClient porkArrangeAuditClient;

    /**
     * 理货数据同步分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/pork-arrange/push/page")
    public Pagination<PorkArrangeVO> getPorkArrangePushPage(@Valid PorkArrangeQueryParam param) {
        if (Objects.nonNull(param.getPeriod())) {
            param.setPeriod(DateUtil.deriveNaturalDays(param.getPeriod()));
        }
        Set<String> tallIds = new HashSet<>();
        if (Objects.nonNull(param.getPeriod()) || Objects.nonNull(param.getStatus())) {
            tallIds = this.porkArrangePushDataService.getTallIds(param);
            if (CollectionUtils.isEmpty(tallIds)) {
                return Pagination.empty();
            }
        }
        PorkArrangeAuditQueryParam queryParam = new PorkArrangeAuditQueryParam();
        queryParam.setPageNumber(param.getPageNumber());
        queryParam.setDeviceIds(param.getDeviceIds());
        queryParam.setPageSize(param.getPageSize());
        queryParam.setKeyword(param.getKeyword());
        queryParam.setPorkReportCodes(tallIds);
        Pagination<PorkArrangeAuditVO> porkArrangeAuditVOPagination = this.porkArrangeAuditClient.pagePorkArrangeAudit(queryParam);
        if (CollectionUtils.isEmpty(porkArrangeAuditVOPagination.getRecords())) {
            return Pagination.empty();
        }
        List<PorkArrangeVO> porkArrangeVOS = ObjectUtils.copying(porkArrangeAuditVOPagination.getRecords(), PorkArrangeVO.class);
        Set<String> porkReportIds = porkArrangeVOS.stream().map(PorkArrangeVO::getPorkReportCode).collect(Collectors.toSet());
        //查询推送失败的批次
        Set<String> failTallIds = this.porkArrangePushDataService.getFailPushTallIds(porkReportIds);
        //查询推送中的批次
        Set<String> stayPushTallIds = this.porkArrangePushDataService.getStayPushTallIds(porkReportIds);
        porkArrangeVOS.forEach(porkArrangeVO -> {
            if (failTallIds.contains(porkArrangeVO.getPorkReportCode())) {
                porkArrangeVO.setPushStatus(PorkArrangePushDataStatusEnum.PUSH_FAIL);
            } else if (stayPushTallIds.contains(porkArrangeVO.getPorkReportCode())) {
                porkArrangeVO.setPushStatus(PorkArrangePushDataStatusEnum.PUSH_PROCESS);
            } else {
                porkArrangeVO.setPushStatus(PorkArrangePushDataStatusEnum.PUSH_SUCCESS);
            }
        });
        return PagingUtils.convert(porkArrangeAuditVOPagination, porkArrangeVOS);
    }

    /**
     * 重置批次推送状态
     *
     * @param tallyIds 批次号
     */
    @PostMapping("/pork-arrange/push/reset")
    public void resetPorkArrangePushStatus(@RequestBody Collection<String> tallyIds) {
        this.porkArrangePushDataService.updatePushStatusByTallIds(tallyIds,
                                                                  PorkArrangePushDataStatusEnum.UN_PUSH,
                                                                  0);
    }
}
