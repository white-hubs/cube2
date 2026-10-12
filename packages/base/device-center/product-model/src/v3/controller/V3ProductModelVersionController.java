package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.guhecloud.cube.base.iot.product.model.v3.service.V3ProductModelVersionService;
import com.guhecloud.cube.base.iot.product.model.v3.vo.ProductModelVersionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 物模型版本管理Controller v3版本(新增功能)
 * 路径前缀: /v3/things/product-version
 */
@Slf4j
@RestController
@RequestMapping("/v3/things/product-version")
@RequiredArgsConstructor
public class V3ProductModelVersionController {

    private final V3ProductModelVersionService versionService;

    /**
     * 查询产品的版本列表
     *
     * @param prodId 产品ID
     * @return 版本列表
     */
    @GetMapping("/list")
    public List<ProductModelVersionVO> listVersions(@RequestParam Long prodId) {
        log.info("查询版本列表接口调用, prodId={}", prodId);
        return versionService.listVersions(prodId);
    }

    /**
     * 查询版本详情
     *
     * @param id 版本ID
     * @return 版本详情(包含完整快照)
     */
    @GetMapping("/{id}")
    public ProductModelVersionVO getVersionDetail(@PathVariable Long id) {
        log.info("查询版本详情接口调用, versionId={}", id);
        return versionService.getVersionDetail(id);
    }

    /**
     * 对比两个版本
     *
     * @param versionId1 版本1 ID
     * @param versionId2 版本2 ID
     * @return 对比结果
     */
    @GetMapping("/compare")
    public Map<String, Object> compareVersions(
            @RequestParam Long versionId1,
            @RequestParam Long versionId2) {
        log.info("对比版本接口调用, versionId1={}, versionId2={}", versionId1, versionId2);
        return versionService.compareVersions(versionId1, versionId2);
    }

    /**
     * 回滚到指定版本
     *
     * @param param 包含prodId和versionId
     */
    @PostMapping("/rollback")
    public void rollbackToVersion(@RequestBody Map<String, Long> param) {
        Long prodId = param.get("prodId");
        Long versionId = param.get("versionId");
        log.info("版本回滚接口调用, prodId={}, versionId={}", prodId, versionId);
        versionService.rollbackToVersion(prodId, versionId);
    }

    /**
     * 手动创建版本快照
     *
     * @param param 包含prodId和changeDesc
     * @return 新版本ID
     */
    @PostMapping("/snapshot")
    public Long createSnapshot(@RequestBody Map<String, Object> param) {
        Long prodId = ((Number) param.get("prodId")).longValue();
        String changeDesc = (String) param.getOrDefault("changeDesc", "手动创建快照");
        log.info("创建版本快照接口调用, prodId={}, changeDesc={}", prodId, changeDesc);
        return versionService.createSnapshot(prodId, "UPDATE", changeDesc);
    }
}
