package com.guhecloud.cube.biz.emergency.server.controller;

import com.guhecloud.cube.biz.emergency.server.param.EmergencyKnowledgeParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyKnowledgeQueryParam;
import com.guhecloud.cube.biz.emergency.server.service.EmergencyKnowledgeService;
import com.guhecloud.cube.biz.emergency.server.vo.EmergencyKnowledgeVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 应急知识（技术）管理
 * @date 2024-01-26 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmergencyKnowledgeController {
    private final EmergencyKnowledgeService emergencyKnowledgeService;

    /**
     * 功能描述： 应急知识（技术）管理添加
     */
    @PostMapping(value = "/emergency-knowledge")
    public Long add(@Valid @RequestBody EmergencyKnowledgeParam param) {
        return emergencyKnowledgeService.add(param);
    }

    /**
     * 功能描述： 应急知识（技术）管理修改
     */
    @PutMapping(value = "/emergency-knowledge/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody EmergencyKnowledgeParam param) {
        emergencyKnowledgeService.update(id, param);
    }

    /**
     * 功能描述： 应急知识（技术）管理删除
     */
    @DeleteMapping(value = "/emergency-knowledge/{id}")
    public void delete(@PathVariable Long id) {
        emergencyKnowledgeService.delete(id);
    }

    /**
     * 功能描述： 应急知识（技术）管理批量删除
     */
    @DeleteMapping(value = "/emergency-knowledge/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        emergencyKnowledgeService.batchDelete(ids);
    }

    /**
     * 功能描述： 应急知识（技术）管理分页
     */
    @GetMapping(value = "/emergency-knowledge/page")
    public Pagination<EmergencyKnowledgeVO> getPage(EmergencyKnowledgeQueryParam param) {
        return emergencyKnowledgeService.getPage(param);
    }

    /**
     * 功能描述： 应急知识（技术）管理详情
     */
    @GetMapping(value = "/emergency-knowledge/{id}")
    public EmergencyKnowledgeVO getById(@PathVariable Long id) {
        return emergencyKnowledgeService.getById(id);
    }

}