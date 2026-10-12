package com.guhecloud.cube.base.iot.product.model.v3.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemModifyParam;
import com.guhecloud.cube.base.iot.product.model.v3.param.DictItemPageParam;
import com.guhecloud.cube.base.iot.product.model.v3.service.V3DictItemService;
import com.guhecloud.cube.base.iot.product.model.v3.util.PageAdapter;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemTreeVO;
import com.guhecloud.cube.base.iot.product.model.v3.vo.DictItemVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典项管理Controller
 * 提供通用的字典项CRUD接口
 *
 * @author AI Assistant
 */
@Slf4j
@RestController
@RequestMapping("/v3/dict-item")
@RequiredArgsConstructor
public class V3DictItemController {

    private final V3DictItemService dictItemService;

    /**
     * 分页查询字典项
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @GetMapping("/page")
    public Pagination<DictItemVO> page(DictItemPageParam param) {
        log.info("分页查询字典项, param={}", param);
        return PageAdapter.toStandard(dictItemService.page(param));
    }

    /**
     * 查询字典项详情
     *
     * @param id 字典项ID
     * @return 字典项详情
     */
    @GetMapping("/{id}")
    public DictItemVO getById(@PathVariable Long id) {
        log.info("查询字典项详情, id={}", id);
        return dictItemService.getById(id);
    }

    /**
     * 批量查询字典项(用于ID转Name)
     *
     * @param ids 字典项ID列表
     * @return 字典项列表
     */
    @GetMapping("/batch")
    public List<DictItemVO> listByIds(@RequestParam List<Long> ids) {
        log.info("批量查询字典项, ids={}", ids);
        return dictItemService.listByIds(ids);
    }

    /**
     * 查询树形字典项
     *
     * @param dictId 字典ID
     * @param dictItemId 指定节点ID(可选)
     * @return 树形字典项列表
     */
    @GetMapping("/tree")
    public List<DictItemTreeVO> listTree(
            @RequestParam Long dictId,
            @RequestParam(required = false) Long dictItemId) {
        log.info("查询树形字典项, dictId={}, dictItemId={}", dictId, dictItemId);
        return dictItemService.listTree(dictId, dictItemId);
    }

    /**
     * 新增字典项
     *
     * @param param 新增参数
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long save(@Valid @RequestBody DictItemModifyParam param) {
        log.info("新增字典项, param={}", param);
        return dictItemService.save(param);
    }

    /**
     * 修改字典项
     *
     * @param id 字典项ID
     * @param param 修改参数
     */
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody DictItemModifyParam param) {
        log.info("修改字典项, id={}, param={}", id, param);
        dictItemService.update(id, param);
    }

    /**
     * 删除字典项
     *
     * @param id 字典项ID
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        log.info("删除字典项, id={}", id);
        dictItemService.delete(id);
    }
}
