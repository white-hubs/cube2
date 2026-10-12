package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.common.util.DatePeriodUtils;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.enums.InspectionItemTagEnum;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectRecordItemTagCodeStatParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.param.InspectRecordPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.model.vo.InspectStatVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.statistics.service.InspectRecordStatService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

/**
 * 电动车巡检统计接口
 *
 * @author lanchun
 * @date 2025/3/18 15:33
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectElectronMobileStatController {

    private final InspectRecordStatService inspectRecordStatService;

    /**
     * 电动车违规充电统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect/electron-mobile/charge/stat")
    public InspectStatVO getChargeStat(@Valid InspectRecordItemTagCodeStatParam param) {
        param.setCodes(Collections.singleton(InspectionItemTagEnum.CHARGE.name()));
        return getInspectItemTagCodeStatVO(param);
    }

    /**
     * 电动车违规停放统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect/electron-mobile/parking/stat")
    public InspectStatVO getParkingStat(@Valid InspectRecordItemTagCodeStatParam param) {
        param.setCodes(Collections.singleton(InspectionItemTagEnum.PARKING.name()));
        return getInspectItemTagCodeStatVO(param);
    }

    /**
     * 电动车违规充电巡检记录分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect/electron-mobile/charge/page")
    public Pagination<InspectRecordListVO> getChargePage(@Valid InspectRecordPageParam param) {
        param.setCodes(Collections.singleton(InspectionItemTagEnum.CHARGE.name()));
        Page<InspectRecordListVO> page = this.inspectRecordStatService.getInspectRecordPageByTagCode(param);
        return PagingUtils.convert(page);
    }

    /**
     * 电动车违规停放巡检记录分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/inspect/electron-mobile/parking/page")
    public Pagination<InspectRecordListVO> getParkingPage(@Valid InspectRecordPageParam param) {
        param.setCodes(Collections.singleton(InspectionItemTagEnum.PARKING.name()));
        Page<InspectRecordListVO> page = this.inspectRecordStatService.getInspectRecordPageByTagCode(param);
        return PagingUtils.convert(page);
    }

    private InspectStatVO getInspectItemTagCodeStatVO(InspectRecordItemTagCodeStatParam param) {
        Integer currentStat = inspectRecordStatService.getInspectRecordItemTagCodeStat(param);
        Period previousPeriod = DatePeriodUtils.getPreviousPeriod(param.getType(), param.getPeriod());
        param.setPeriod(previousPeriod);
        Integer lastStat = inspectRecordStatService.getInspectRecordItemTagCodeStat(param);
        return InspectStatVO.builder()
                .total(currentStat)
                .chainRatio(MathUtils.calculateRingRatioPercentage(currentStat, lastStat))
                .build();
    }
}
