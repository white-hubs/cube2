package com.guhecloud.cube.biz.mdata.server.stall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.stall.dto.StallCategoryDTO;
import com.guhecloud.cube.biz.mdata.server.stall.entity.StallCategory;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryCreateBatchParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryCreateParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryQueryParam;
import com.guhecloud.cube.biz.mdata.server.stall.param.StallCategoryUpdateParam;
import com.guhecloud.cube.biz.mdata.server.stall.service.StallCategoryService;
import com.guhecloud.cube.biz.mdata.server.stall.vo.StallCategoryPageVO;
import com.guhecloud.cube.biz.mdata.server.stall.vo.StallCategoryVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
import java.util.Objects;

/**
 * 档位分类接口
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallCategoryController {

    private final StallCategoryService stallCategoryService;

    private final FileClient fileClient;

    /**
     * 档位分类新增
     *
     * @param param 档位分类新增参数
     */
    @PostMapping("/stall-category")
    public void save(@Valid @RequestBody StallCategoryCreateParam param) {
        this.stallCategoryService.save(param);
    }

    /**
     * 档位分类更新
     *
     * @param param 档位分类更新参数
     */
    @PutMapping("/stall-category")
    public void update(@Valid @RequestBody StallCategoryUpdateParam param) {
        this.stallCategoryService.update(param);
    }

    /**
     * 档位分类ID查询
     *
     * @param id 档位分类ID
     *
     * @return 档位分类
     */
    @GetMapping("/stall-category/{id}")
    public StallCategoryVO getById(@NotNull(message = "档位分类主键不能小于1") @Min(value = 1, message = "档位分类不能小于1")
                                   @PathVariable("id") Long id
                                  ) {
        StallCategory stallCategory = this.stallCategoryService.getById(id);
        StallCategoryVO stallCategoryVO = ObjectUtils.copying(stallCategory, StallCategoryVO.class);
        if (Objects.nonNull(stallCategoryVO)) {
            List<FileVO> fileVOS = this.fileClient.getByBusinessKey(stallCategory.getId());
            stallCategoryVO.setFileVO(!CollectionUtils.isEmpty(fileVOS) ? fileVOS.get(0) : null);
        }
        return stallCategoryVO;
    }

    /**
     * 档位分类分页查询
     *
     * @param queryParam 档位分类查询参数
     *
     * @return 档位分类分页集合
     */
    @GetMapping("/stall-category/page")
    public Pagination<StallCategoryPageVO> page(StallCategoryQueryParam queryParam) {
        Page<StallCategory> page = this.stallCategoryService.page(queryParam);
        List<StallCategoryPageVO> pageVOS = ObjectUtils.copying(page.getRecords(), StallCategoryPageVO.class);
        if (CollectionUtils.isEmpty(pageVOS)) {
            return Pagination.empty();
        }
        pageVOS.stream().forEach(stallCategoryPageVO -> {
            List<FileVO> fileVOS = this.fileClient.getByBusinessKey(stallCategoryPageVO.getId());
            if (!CollectionUtils.isEmpty(fileVOS)) {
                stallCategoryPageVO.setFileVO(fileVOS.get(0));
            }
        });
        return PagingUtils.convert(page, pageVOS);
    }

    /**
     * 档位分类列表查询
     *
     * @return 档位分类列表集合
     */
    @GetMapping("/stall-category/list")
    public List<StallCategoryPageVO> list(StallCategoryQueryParam queryParam) {
        List<StallCategory> list = this.stallCategoryService.list(queryParam);
        return ObjectUtils.copying(list, StallCategoryPageVO.class);
    }

    /**
     * 档位类型ID删除
     *
     * @param id 档位类型主键
     */
    @DeleteMapping("/stall-category/{id}")
    public void remove(@NotNull(message = "档位分类主键不能小于1") @Min(value = 1, message = "档位分类不能小于1")
                       @PathVariable("id") Long id
                      ) {
        this.stallCategoryService.remove(id);
    }

    /**
     * 档位类型查询
     *
     * @param ids 档位类型主键集合
     *
     * @return 档位类型信息
     */
    @GetMapping("/stall-category/ids")
    public List<StallCategoryDTO> listStallCategory(@RequestParam("ids") List<Long> ids) {
        List<StallCategory> stallCategories = this.stallCategoryService.getByIds(ids);
        return ObjectUtils.copying(stallCategories, StallCategoryDTO.class);
    }

    /**
     * 大白菜挡位类型批量新增
     * 如果存在相同名称挡位跳过
     * 注意：不会新增文件信息
     *
     * @param param 挡位集合入参
     */
    @PostMapping("/stall-category/saveBatchOnlyEntity")
    public void saveBathOnlyEntity(@RequestBody StallCategoryCreateBatchParam param) {
        this.stallCategoryService.saveBatchOnlyEntity(param);
    }

}
