package com.guhecloud.kaihe.scada.server.controller;

import com.guhecloud.kaihe.scada.server.dto.CategoryDTO;
import com.guhecloud.kaihe.scada.server.dto.CategoryQueryDTO;
import com.guhecloud.kaihe.scada.server.dto.CategoryTreeDTO;
import com.guhecloud.kaihe.scada.server.entity.Category;
import com.guhecloud.kaihe.scada.server.infrastructure.web.response.UnifiedResponseBody;
import com.guhecloud.kaihe.scada.server.service.CategoryService;
import com.guhecloud.kaihe.scada.server.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 图形目录分类
 */
@RequestMapping("/Home")
@RestController
@UnifiedResponseBody
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/CategoryDetail/{id}")
    public void saveOrUpdate(
            @Valid CategoryDTO categoryDTO,
            @PathVariable("id") Integer id
                            ) {
        if (id == -1) {
            categoryService.save(categoryDTO);
        } else {
            categoryService.update(categoryDTO);
        }
    }

    @PostMapping("/LoadCategory")
    public List<CategoryTreeDTO> getTree(
            @RequestParam(name = "projectId", defaultValue = ProjectService.DEFAULT_PROJECT_ID) Integer projectId
                                        ) {
        //        projectId = SessionContextHolder.getRequester().getTenant();
        List<Category> rootCategorys = categoryService.lambdaQuery()
                .eq(Category::getParentID, -1)
                .eq(Category::getProjectId, projectId)
                .list();
        return this.getCategoryTreeDTO(rootCategorys);
    }

    @PostMapping("/GetAllCategory")
    public List<CategoryQueryDTO> getAll(
            @RequestParam(name = "projectId", defaultValue = ProjectService.DEFAULT_PROJECT_ID) Integer projectId
                                        ) {
        //        projectId = SessionContextHolder.getRequester().getTenant();
        List<Category> categories = categoryService.lambdaQuery().eq(Category::getProjectId, projectId).list();
        List<CategoryQueryDTO> categoryQueryDTOS = categories.stream().map(category -> {
            CategoryQueryDTO categoryQueryDTO = new CategoryQueryDTO()
                    .setId(category.getID())
                    .setPId(category.getParentID())
                    .setName(category.getName());
            return categoryQueryDTO;
        }).collect(Collectors.toList());
        return categoryQueryDTOS;
    }

    @PostMapping("/DeleteCategory")
    public void remove(String data) {
        categoryService.remove(data);
    }

    private List<CategoryTreeDTO> getChildCategory(CategoryDTO rootCategoryDTO) {
        List<Category> childCategorys = categoryService.lambdaQuery()
                .eq(Category::getParentID, rootCategoryDTO.getID())
                .list();
        return this.getCategoryTreeDTO(childCategorys);
    }

    private List<CategoryTreeDTO> getCategoryTreeDTO(List<Category> categories) {
        List<CategoryDTO> childCategoryDTOS = new CategoryDTO(categories).list();
        List<CategoryTreeDTO> categoryTreeDTOS = childCategoryDTOS.stream().map(categoryDTO -> {
            CategoryTreeDTO categoryTreeDTO = new CategoryTreeDTO()
                    .setText(categoryDTO.getName())
                    .setHref("#" + categoryDTO.getID())
                    .setKey(categoryDTO.getID())
                    .setPkey(categoryDTO.getParentID())
                    .setNodes(this.getChildCategory(categoryDTO));
            return categoryTreeDTO;
        }).collect(Collectors.toList());
        return categoryTreeDTOS;
    }

}
