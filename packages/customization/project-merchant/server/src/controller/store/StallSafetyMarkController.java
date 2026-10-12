package com.guhecloud.cube.deliver.project.merchant.controller.store;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.deliver.project.merchant.commons.dto.BaseQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageRequest;
import com.guhecloud.cube.deliver.project.merchant.commons.web.PageResponse;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.Space;
import com.guhecloud.cube.deliver.project.merchant.service.SpaceService;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallSafetyMarkService;

import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 档位总分值 前端控制器
 * </p>
 *
 * @author bq
 * @since 2022-02-28
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallSafetyMarkController {

    private final SpaceService spaceService;
    private final StallSafetyMarkService stallSafetyMarkService;

    /**
     * 单个档位总分
     *
     * @return
     */
    @GetMapping("/stall-score/{stallId}")
    public StallScoreDTO getStallScore(@PathVariable("stallId") String stallId) {
        StallScoreDTO dto = new StallScoreDTO();
        // 查询档位
//        Space space = spaceService.getById(stallId);
//        StallScoreDTO stallScore = stallSafetyMarkService.getStallScore(space);
        StallScoreQueryDTO queryDTO = new StallScoreQueryDTO();
        queryDTO.setStallId(stallId);
        // 安全生产分值，查询当年的
        queryDTO.setYear(DateUtils.now().getYear());
        List<StallScoreDTO> stallScoreAndEvaluation = stallSafetyMarkService.getStallScoreAndEvaluation(queryDTO);
        if (!stallScoreAndEvaluation.isEmpty()) dto = stallScoreAndEvaluation.getFirst();
        return dto;
    }

    /**
     * 档位总分
     *
     * @param queryDTO
     * @param pageRequest
     * @return
     */
    @GetMapping("/stall-score/total")
    public PageResponse<StallScoreDTO> getPage(StallScoreQueryDTO queryDTO, PageRequest pageRequest) {
//        List<StallScoreDTO> dtos = new ArrayList<>();
        // 查询档位
//        Page<Space> spacePage = this.querySpaceList(queryDTO, pageRequest);
//        // 查询档位对应的信息
//        spacePage.getRecords().stream().forEach(space -> {
//            dtos.add(stallSafetyMarkService.getStallScore(space));
//        });
//        档位782、A001档位、档位783、专A002
//        dtos.sort(Comparator.comparing(StallScoreDTO::getInspTotal, Comparator.reverseOrder()).thenComparing(StallScoreDTO::getTotal, Comparator.reverseOrder()));
//        return new PageResponse<>(dtos, spacePage.getTotal(), spacePage.getPages());
        // 安全生产分值，查询当年的
        queryDTO.setYear(DateUtils.now().getYear());
        List<StallScoreDTO> stallScoreAndEvaluation = stallSafetyMarkService.getStallScoreAndEvaluation(queryDTO);
        stallScoreAndEvaluation.sort(Comparator.comparing(StallScoreDTO::getInspTotal, Comparator.reverseOrder()).thenComparing(StallScoreDTO::getTotal, Comparator.reverseOrder()));
        return PageResponse.doPage(stallScoreAndEvaluation, pageRequest);
    }

    private Page<Space> querySpaceList(BaseQueryDTO queryDTO, PageRequest pageRequest) {
        Page<Space> page = spaceService.lambdaQuery().eq(Space::getProjectId, queryDTO.getProjectId())
                .and(StringUtils.isNotEmpty(queryDTO.getSearch()), q -> q
                        .like(Space::getName, queryDTO.getSearch())
                        .or()
                        .like(Space::getCompanyName, queryDTO.getSearch())
                )
                .ne(Space::getCompanyId, "")
//                .eq(Space::getAttribute, SpaceService.ATTR_BOOTH)
                .page(pageRequest.page());
        return page;
    }

}

