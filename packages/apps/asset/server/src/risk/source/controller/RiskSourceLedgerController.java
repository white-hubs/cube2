package com.guhecloud.cube.srv.gen.property.management.server.risk.source.controller;

import com.guhecloud.cube.base.dictionary.client.CascadeDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.CascadeDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryParam;
import com.guhecloud.cube.property.management.client.property.param.property.RiskSourceDeviceQueryParam;
import com.guhecloud.cube.property.management.client.property.vo.SafetyManageCollectPropertyVO;
import com.guhecloud.cube.property.management.client.property.vo.property.RiskSourceDeviceVO;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerAllParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerBatchParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceIdTypeVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.cube.srv.gen.property.management.server.risk.source.service.RiskSourceLedgerService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 风险源台账
 *
 * @Author: lijie
 * @CreateTime: 2024-01-30  16:45
 * @Description:
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskSourceLedgerController {
    private final RiskSourceLedgerService riskSourceLedgerService;
    private final CascadeDictionaryClient cascadeDictionaryClient;

    /**
     * 查询风险源台账详情
     *
     * @param id 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/risk/source/ledger/queryById/{id}")
    public RiskSourceLedgerVO queryById(@PathVariable Long id) {
        return riskSourceLedgerService.queryById(id);
    }

    /**
     * 批量查询风险源台账详情
     *
     * @param idList 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/risk/source/ledger/query/batch/get")
    public List<RiskSourceLedgerVO> queryRiskSourceLedgerBatchGet(@RequestParam(value = "idList") List<Long> idList) {
        return riskSourceLedgerService.queryRiskSourceLedgerBatch(idList);
    }

    /**
     * 批量查询风险源台账详情
     *
     * @param param idList id集合
     *
     * @return 查询结果
     */
    @PostMapping("/risk/source/ledger/query/batch/post")
    public List<RiskSourceLedgerVO> queryRiskSourceLedgerBatchPost(@RequestBody RiskSourceLedgerBatchParam param) {
        return riskSourceLedgerService.queryRiskSourceLedgerBatch(param.getIdList());
    }

    /**
     * 查询风险源台账详情
     *
     * @param bimCode 模型编码
     *
     * @return 查询结果
     */
    @GetMapping("/risk/source/ledger/queryByBimCode/{bimCode}")
    public RiskSourceLedgerVO queryByBimCode(@PathVariable String bimCode) {
        return riskSourceLedgerService.queryByBimCode(bimCode);
    }

    /**
     * 分页查询风险源台账
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/risk/source/ledger/page")
    @DataPermissionSwitch(dataModel = "riskSource:detailed")
    public Pagination<RiskSourceLedgerVO> queryByPage(RiskSourceLedgerPageParam param) {
        return riskSourceLedgerService.queryByPage(param);
    }

    @PostMapping("/post/risk/source/ledger/page")
    public Pagination<RiskSourceLedgerVO> postPage(@RequestBody @Valid RiskSourceLedgerPageParam param) {
        return queryByPage(param);
    }

    /**
     * 根据编码查询执行数量风险源台账
     *
     * @param typeCodes
     * @param size
     *
     * @return
     */
    @GetMapping("/risk/source/ledger/with/location")
    public List<RiskSourceLedgerVO> queryRiskSourceLedgerWithLocation(@RequestParam("typeCodes") String typeCodes,
                                                                      @RequestParam("size") Integer size
                                                                     ) {
        return riskSourceLedgerService.queryRiskSourceLedgerWithLocation(typeCodes, size);
    }

    /**
     * 风险源台账数据采集
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @PreAuthorize("hasScope('risk_source')")
    @GetMapping("/risk/source/ledger/queryAll")
    public List<SafetyManageCollectPropertyVO> queryAll(RiskSourceLedgerAllParam param) {
        return riskSourceLedgerService.queryAll(param);
    }

    /**
     * 新增风险源台账
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/risk/source/ledger/insert")
    public Boolean insert(@RequestBody RiskSourceLedgerParam param) {
        return riskSourceLedgerService.insert(param);
    }

    /**
     * 更新风险源台账
     *
     * @param param 实例对象
     *
     * @return 实例对象
     */
    @PutMapping("/risk/source/ledger/update")
    public Boolean update(@RequestBody RiskSourceLedgerParam param) {
        return riskSourceLedgerService.update(param);
    }

    /**
     * 删除风险源台账
     *
     * @param ids 主键
     *
     * @return 是否成功
     */
    @PutMapping("/risk/source/ledger/delete")
    public void deleteById(@RequestBody Set<Long> ids) {
        riskSourceLedgerService.deleteByIds(ids);
    }

    /**
     * 下载风险源导入模板
     *
     * @author: lijie
     * @date: 2024/2/1 11:31
     * @param: void
     * @return: 模板
     **/
    @GetMapping("/risk/source/ledger/getTemplateExcel")
    public void getTemplateExcel(HttpServletRequest request, HttpServletResponse response) {
        try {
            CascadeDictionaryParam dictionaryParam = new CascadeDictionaryParam();
            dictionaryParam.setItemCode("risk-source-type");
            List<CascadeDictionaryDTO> allCascadeDictionaryItem = cascadeDictionaryClient.getAllCascadeDictionaryItem(dictionaryParam);
            riskSourceLedgerService.getTemplateExcel(allCascadeDictionaryItem, request, response);
        } catch (Exception e) {
            log.error("风险源导入模板下载失败", e);
            throw new ServiceException("风险源导入模板下载失败，请联系管理员");
        }
    }

    /**
     * 风险源导入
     *
     * @author: lijie
     * @date: 2024/2/1 11:31
     * @param: 文件
     * @return: void
     **/
    @PostMapping("/risk/source/ledger/import/template")
    public void importTemplate(@RequestParam("file") MultipartFile file) {
        riskSourceLedgerService.importTemplate(file);
    }

    @GetMapping("/risk/source/ledger/device-num")
    public List<RiskSourceDeviceVO> getRiskSourceDeviceNum(RiskSourceDeviceQueryParam param) {
        return riskSourceLedgerService.getRiskSourceDeviceNum(param);
    }

    /**
     * 查询档位或者现场点关联的风险源
     *
     * @param param 条件
     *
     * @return map(档位id或现场点id为key)
     */
    @PostMapping("/risk/source/ledger/stall-point")
    public Map<Long, List<RiskSourceLedgerVO>> postRiskSourceByStallOrPoint(@RequestBody RiskSourceLedgerPageParam param) {
        param.checkStallOrPrintList();
        List<RiskSourceLedgerVO> vos = riskSourceLedgerService.riskSourceLedgerVOList(param);
        if (CollectionUtils.isNotEmpty(param.getPrintIdList())) {
            return vos.stream().collect(Collectors.groupingBy(RiskSourceLedgerVO::getSitePointId));
        }
        return vos.stream().collect(Collectors.groupingBy(RiskSourceLedgerVO::getStallId));
    }

    /**
     * 获取风险源ID列表
     *
     * @param param 查询参数
     *
     * @return 风险源ID列表
     */
    @PostMapping("/risk/source/ledger/idTypes")
    public List<RiskSourceIdTypeVO> getRiskSourceIdTypes(@RequestBody RiskSourceLedgerPageParam param) {
        return riskSourceLedgerService.getRiskSourceIdTypes(param);
    }
}
