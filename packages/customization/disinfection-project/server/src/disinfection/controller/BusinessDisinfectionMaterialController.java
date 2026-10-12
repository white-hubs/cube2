package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.MaterialInsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.MaterialPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionMaterialService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.ProjectMaterialInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.MaterialPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消杀资料接口
 */
@RestController
@RequestMapping("/disinfection/materials")
@RequiredArgsConstructor
@Slf4j
public class BusinessDisinfectionMaterialController {
    private final BusinessDisinfectionMaterialService businessMaterialService;

    /**
     * 根据条件获取全部列表
     */
    @GetMapping("/list-all")
    public List<ProjectMaterialInfoVO> listAll(@RequestParam Long projectId, @RequestParam String keyword) {
        return businessMaterialService.listAll(projectId, keyword);
    }

    /**
     * 新增项目资料
     */
    @PostMapping
    public Boolean insertMaterial(@Valid @RequestBody MaterialInsertParam param) {
        return businessMaterialService.insertMaterial(param);
    }

    /**
     * 批量删除，多个id用逗号分隔
     *
     * @param ids id
     *
     * @return 删除结果
     */
    @DeleteMapping
    public Boolean deleteMaterial(@RequestParam String ids) {
        return businessMaterialService.deleteMaterial(ids);
    }

    /**
     * 分页
     */
    @GetMapping
    public Pagination<MaterialPageVO> listMaterials(MaterialPageParam param) {
        return businessMaterialService.pageMaterials(param);
    }
}
