package com.guhecloud.cube.deliver.project.merchant.controller.store;

import cn.hutool.core.date.DateUtil;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallSafetyMarkLogsDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyMarkLogs;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkLogsService;

import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import java.util.List;

/**
 * <p>
 * 档位安全生产分值修改记录 前端控制器
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-27
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSafetyMarkLogsController {

    private final StallSafetyMarkLogsService service;

    /**
     * 根据档位ID,时间段来查询对应的修改记录
     */
    @GetMapping("/stall-safety-mark-logs/store/page")
    public PageResponse<StallSafetyMarkLogsDTO> getLogsByStore(BaseQueryDTO queryDTO, PageRequest pageRequest) {
        List<StallSafetyMarkLogs> list = service.lambdaQuery()
                .eq(StallSafetyMarkLogs::getStallId, queryDTO.getSearch())
                .and(queryDTO.getBeginTime() != null && queryDTO.getEndTime() != null,
                     q -> q.between(StallSafetyMarkLogs::getCreateTime,
                                    DateUtils.beginOfDay(queryDTO.getBeginTime()), DateUtils.endOfDay(queryDTO.getEndTime())))
                .list();
        return PageResponse.doPage(new StallSafetyMarkLogsDTO(list).list(), pageRequest);
    }

    /**
     * 增加的同时 ，还要加减分
     */
    @PostMapping("/stall-safety-mark-logs")
    public String save(@Valid @RequestBody StallSafetyMarkLogsDTO dto) {
        return service.save(dto);
    }

    /**
     * 删除的时候，也要加减分
     */
    @DeleteMapping("/stall-safety-mark-logs/{id}")
    public void remove(@PathVariable("id") String id) {
        service.remove(id);
    }
}

