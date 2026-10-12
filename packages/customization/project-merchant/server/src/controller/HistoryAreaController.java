package com.guhecloud.cube.deliver.project.merchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.HistoryAreaDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.HistoryArea;
import com.guhecloud.cube.deliver.project.merchant.service.HistoryAreaService;
import com.guhecloud.cube.deliver.project.merchant.service.MerchantAreaService;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.utils.PageRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 空间、区域历史面积 前端控制器
 * </p>
 *
 * @author bq
 * @since 2021-07-14
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HistoryAreaController {

    private final HistoryAreaService historyAreaService;
    private final MerchantAreaService areaService;
    private final SpaceService spaceService;

    @GetMapping("/history-areas")
    public PageResponse<HistoryAreaDTO> getPage(
            @RequestParam("objectType") String objectType,
            @RequestParam("objectId") String objectId,
            BaseQueryDTO baseQueryDTO,
            PageRequest pageRequest
    ) {
        Page<HistoryArea> page = historyAreaService.lambdaQuery()
                .eq(HistoryArea::getProjectId, baseQueryDTO.getProjectId())
                .eq(HistoryArea::getObjectType, objectType)
                .eq(HistoryArea::getObjectId, objectId)
                .between(null != baseQueryDTO.getBeginTime() && null != baseQueryDTO.getEndTime(), HistoryArea::getCreateTime, baseQueryDTO.getBeginTime(), baseQueryDTO.getEndTime())
                .orderByAsc(HistoryArea::getCreateTime)
                .page(pageRequest.page());
        List<HistoryAreaDTO> dtos = new HistoryAreaDTO(page.getRecords()).list();
        this.extend(dtos, objectId, objectType);
        return new PageResponse<>(dtos, page.getTotal(), page.getPages());
    }

    private void extend(List<HistoryAreaDTO> dtos, String objectId, String objectType) {
        Object object = new Object();
        if (MerchantAreaService.BUSINESS_TYPE.equals(objectType)) {
            object = areaService.getById(objectId);
        } else if (SpaceService.BUSINESS_TYPE.equals(objectType)) {
            object = spaceService.getById(objectId);
        }
        Object finalObject = object;
        dtos.parallelStream().forEach(dto -> dto.setObject(finalObject));
    }

}

