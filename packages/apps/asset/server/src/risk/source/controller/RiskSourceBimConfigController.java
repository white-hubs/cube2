package com.guhecloud.cube.srv.gen.property.management.server.risk.source.controller;

import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigDeleteParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigPageParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceBimConfigParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceBimConfigVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceBimConfigService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: lijie
 * @CreateTime: 2024-04-07  13:46
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceBimConfigController {
    private final RiskSourceBimConfigService riskSourceBimConfigService;

    /**
     * 分页查询未关联可视化菜单的风险源
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/risk/source/ledgerByBimSceneType/page")
    public Pagination<RiskSourceLedgerVO> queryledgerByBimSceneTypePage(RiskSourceLedgerPageParam param) {
        return riskSourceBimConfigService.queryledgerByBimSceneTypePage(param);
    }

    /**
     * 分页查询
     *
     * @param param: 筛选条件
     *
     * @return 查询结果
     *
     * @author : lijie
     */
    @GetMapping("/risk/source/bim/config/page")
    public Pagination<RiskSourceBimConfigVO> queryByPage(RiskSourceBimConfigPageParam param) {
        return riskSourceBimConfigService.queryByPage(param);
    }

    /**
     * 新增数据
     *
     * @param param: 新增数据
     *
     * @return 新增结果
     *
     * @author : lijie
     */
    @PostMapping("/risk/source/bim/config/insert")
    public Boolean insert(@RequestBody RiskSourceBimConfigParam param) {
        return riskSourceBimConfigService.insert(param);
    }

    /**
     * 通过主键删除数据
     *
     * @return 删除结果
     *
     * @author : lijie
     */
    @DeleteMapping("/risk/source/bim/config/delete")
    public Boolean deleteById(@RequestBody RiskSourceBimConfigDeleteParam ids) {
        return riskSourceBimConfigService.deleteById(ids);
    }
}
