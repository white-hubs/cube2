package com.guhecloud.cube.srv.gen.official.management.server.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.FocusWorkCategoryCommandService;
import com.guhecloud.cube.srv.gen.official.management.server.commandgateway.command.FocusWorkCategoryCommand;
import com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo.FocusWorkCategoryVO;
import com.guhecloud.cube.srv.gen.official.management.server.po.FocusWorkCategoryPO;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.FocusWorkCategoryQueryService;
import com.guhecloud.cube.srv.gen.official.management.server.querygateway.query.FocusWorkCategoryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vo.FormFieldVO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 事项类型管理
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FocusWorkCategoryController {
    private final FocusWorkCategoryQueryService focusWorkCategoryService;
    private final FocusWorkCategoryCommandService focusWorkCategoryCommandService;

    /**
     * 新增事项类型
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/focusWork/category")
    public Long saveFocusWorkCategory(@Valid @RequestBody FocusWorkCategoryCommand param) {
        return focusWorkCategoryCommandService.addFocusWorkCategory(param);
    }

    /**
     * 修改事项类型
     *
     * @param id
     * @param param
     */
    @PutMapping("/focusWork/category/{id}")
    public void updateFocusWorkCategory(@PathVariable Long id, @RequestBody FocusWorkCategoryCommand param) {
        focusWorkCategoryCommandService.updateFocusWorkCategory(id, param);
    }

    /**
     * 删除事项类型
     *
     * @param id
     */
    @DeleteMapping("/focusWork/category/{id}")
    public void deleteFocusWorkCategory(@PathVariable Long id) {
        focusWorkCategoryCommandService.deleteFocusWorkCategory(id);
    }

    /**
     * 事项类型分页查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/focusWork/category")
    public Pagination<FocusWorkCategoryVO> pageFocusWorkCategory(FocusWorkCategoryParam param) {
        Page<FocusWorkCategoryPO> page = focusWorkCategoryService.pageFocusWorkCategory(param);
        return PagingUtils.convert(page, FocusWorkCategoryVO.class);
    }

    @GetMapping("/focusWork/category/weight/list")
    public List<FormFieldVO> listFocusWorkCategoryForWeight(FocusWorkCategoryParam param) {
        List<FocusWorkCategoryPO> list = focusWorkCategoryService.listFocusWorkCategory(param);
        if (!CollectionUtils.isEmpty(list)) {
            return list.stream().map(d -> FormFieldVO.builder().id(d.getId().toString()).name(d.getName()).build())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
