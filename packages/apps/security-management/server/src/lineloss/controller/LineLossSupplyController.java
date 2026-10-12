package com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossEditParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.param.LineLossTrendParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.service.LineLossSupplyService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.ElectricitySupplyChangeVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossSupplyDetailVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lineloss.vo.LineLossTrendVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 线损关系控制器
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LineLossSupplyController {
    private final LineLossSupplyService lineLossSupplyService;

    /**
     * 获取供电关系变更内容
     *
     * @return 变更数据
     */
    @GetMapping("/lineLoss/supply/change")
    public ElectricitySupplyChangeVO getElectricitySupplyChange() {
        return lineLossSupplyService.getElectricitySupplyChange();
    }

    /**
     * 同步供电关系到线损关系
     */
    @PostMapping("/lineLoss/supply/sync")
    public Boolean syncFromElectricitySupply() {
        return lineLossSupplyService.syncFromElectricitySupply();
    }

    /**
     * 获取详情
     *
     * @return 详情
     */
    @GetMapping("/lineLoss/supply/detail")
    public LineLossSupplyDetailVO detail() {
        return lineLossSupplyService.detail();
    }

    /**
     * 修改线损关系中的设备
     *
     * @param param 参数
     *
     * @return 是否成功
     */
    @PostMapping("/lineLoss/supply/edit")
    public Boolean editLossLineDevice(@RequestBody LineLossEditParam param) {
        return lineLossSupplyService.editLossLineDevice(param);
    }

    /**
     * 根据线路 ID 查询线损趋势
     *
     * @param param 查询参数
     *
     * @return 线损趋势数据列表
     */
    @GetMapping("/lineLoss/supply/trend")
    public List<LineLossTrendVO> getLineLossTrend(LineLossTrendParam param) {
        return lineLossSupplyService.getLineLossTrend(param);
    }
}
