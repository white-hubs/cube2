package com.guhecloud.cube.property.management.client.risk.source.client;

import com.guhecloud.cube.property.management.client.property.param.property.RiskSourceDeviceQueryParam;
import com.guhecloud.cube.property.management.client.property.vo.property.RiskSourceDeviceVO;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerAllParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerBatchParam;
import com.guhecloud.cube.property.management.client.risk.source.param.RiskSourceLedgerPageParam;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceIdTypeVO;
import com.guhecloud.cube.property.management.client.risk.source.vo.RiskSourceLedgerVO;
import com.guhecloud.platform.base.model.Pagination;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-30  17:07
 * @Description: 风险源管理
 * @Version: 1.0
 */
@FeignClient(name = "propertyManagement")
public interface RiskSourceLedgerClient {
    /**
     * 查询风险源台账详情
     *
     * @param id 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/risk/source/ledger/queryById/{id}")
    RiskSourceLedgerVO queryById(@PathVariable("id") Long id);

    /**
     * 批量查询风险源台账详情
     *
     * @param idList id集合
     *
     * @return 查询结果
     */
    @GetMapping("/risk/source/ledger/query/batch/get")
    List<RiskSourceLedgerVO> queryRiskSourceLedgerBatchGet(@RequestParam(value = "idList") List<Long> idList);

    /**
     * 批量查询风险源台账详情
     *
     * @param param idList id集合
     *
     * @return 查询结果
     */
    @PostMapping("/risk/source/ledger/query/batch/post")
    List<RiskSourceLedgerVO> queryRiskSourceLedgerBatchPost(@RequestBody RiskSourceLedgerBatchParam param);

    /**
     * 分页查询风险源台账
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    @GetMapping("/risk/source/ledger/page")
    Pagination<RiskSourceLedgerVO> queryByPage(@SpringQueryMap RiskSourceLedgerPageParam param);

    @GetMapping("/post/risk/source/ledger/page")
    Pagination<RiskSourceLedgerVO> postPage(@RequestBody RiskSourceLedgerPageParam param);

    @GetMapping("/risk/source/ledger/queryAll")
    List<RiskSourceLedgerVO> queryAll(@SpringQueryMap RiskSourceLedgerAllParam param);

    /**
     * 根据设备类型获取风险源设备数量
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/risk/source/ledger/device-num")
    List<RiskSourceDeviceVO> getRiskSourceDeviceNum(@SpringQueryMap RiskSourceDeviceQueryParam param);

    /**
     * 查询档位或者现场点关联的风险源
     *
     * @param param 条件
     *
     * @return map(档位id或现场点id为key)
     */
    @PostMapping("/risk/source/ledger/stall-point")
    Map<Long, List<RiskSourceLedgerVO>> postRiskSourceByStallOrPoint(@RequestBody RiskSourceLedgerPageParam param);

    /**
     * 获取风险源ID列表
     *
     * @param param 查询参数
     *
     * @return 风险源ID列表
     */
    @PostMapping("/risk/source/ledger/idTypes")
    List<RiskSourceIdTypeVO> getRiskSourceIdTypes(@RequestBody RiskSourceLedgerPageParam param);
}
