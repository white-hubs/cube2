package com.guhecloud.cube.deliver.project.merchant.controller.store;

import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallSafetyInspDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyInsp;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyInspService;

import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
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
import java.util.stream.Collectors;

/**
 * <p>
 * 档位安全生产巡检扣分记录 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2021-12-23
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSafetyInspController {

    private final StallSafetyInspService service;

    /**
     * 根据ID查看巡检记录详情
     */
    @GetMapping("/stall-safety-insp/{id}")
    public StallSafetyInspDTO getById(@PathVariable("id") String id) {
        return new StallSafetyInspDTO(service.lambdaQuery().eq(StallSafetyInsp::getId, id).one());
    }

    /**
     * 根据时间，档位进行分页查询
     */
    @GetMapping("/stall-safety-insp/page")
    public PageResponse<StallSafetyInspDTO> getPage(BaseQueryDTO queryDTO, String stallId, PageRequest pageRequest) {
        //根据档位ID和时间段来查询对应的巡检扣分记录
        List<StallSafetyInsp> list = service.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryDTO.getSearch()), q -> q
                        .like(StallSafetyInsp::getTaskName, queryDTO.getSearch())
                        .or()
                        .like(StallSafetyInsp::getStandardName, queryDTO.getSearch())
                        .or()
                        .like(StallSafetyInsp::getNickname, queryDTO.getSearch())
                    )
                .eq(StringUtils.isNotEmpty(stallId), StallSafetyInsp::getStallId, stallId)
                .between(queryDTO.getBeginTime() != null &&
                         queryDTO.getEndTime() != null, StallSafetyInsp::getTime, queryDTO.getBeginTime(), queryDTO.getEndTime())
                .list();
        return PageResponse.doPage(new StallSafetyInspDTO(list).list(), pageRequest);
    }

    /**
     * 档位新增"巡检扣分记录"，
     * 并且同时将对应的分扣掉（store_safety_mark.score）
     */
    @PostMapping("/stall-safety-insp/batch")
    public void saveBatch(@RequestBody List<StallSafetyInspDTO> dtos) {
        service.save(dtos);
    }

    /**
     * @return 修改后的ID
     */
    @PutMapping("/stall-safety-insp/{recordId}/non-exist")
    public List<String> updateRecord(@PathVariable("recordId") String recordId, Boolean exist) {
        //返回被修改的IDS
        List<String> ids = service.lambdaQuery()
                .eq(StallSafetyInsp::getRecordId, recordId)
                .eq(StallSafetyInsp::getRecordExist, !exist)
                .list().stream().map(StallSafetyInsp::getRecordId).collect(Collectors.toList());
        //修改存在标识
        service.lambdaUpdate()
                .eq(StallSafetyInsp::getRecordId, recordId)
                .eq(StallSafetyInsp::getRecordExist, !exist)
                .set(StallSafetyInsp::getRecordExist, exist)
                .update();
        return ids;
    }

    /**
     * 根据ID删除巡检扣分记录，同时还应该将该记录扣的分加回去
     */
    @DeleteMapping("/stall-safety-insp/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }

    /**
     * 批量删除巡检扣分记录，并退还对应的分数
     */
    @DeleteMapping("/stall-safety-insp")
    public void remove(@RequestParam List<String> ids) {
        if (ids != null) {
            ids.forEach(service::remove);
        }
    }
}

