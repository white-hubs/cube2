package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.DataTypePageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.DataTypeUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionDataTypeService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.DataTypeVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.DataTypePageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消杀分类业务配置相关接口
 */
@RestController
@RequestMapping("/disinfection/data-types")
@Slf4j
@RequiredArgsConstructor
public class BusinessDisinfectionDataTypeController {
    private final BusinessDisinfectionDataTypeService configDataTypeService;

    /**
     * 新增
     */
    @PostMapping
    public Boolean insert(@RequestBody @Valid DataTypeUpsertParam dataTypeUpsertParam) {
        return configDataTypeService.insertDataType(dataTypeUpsertParam);
    }

    /**
     * 删除
     */
    @DeleteMapping("/{id}")
    public Boolean delete(@PathVariable Long id) {
        return configDataTypeService.deleteDataType(id);
    }

    /**
     * 修改
     */
    @PutMapping("/{id}")
    public Boolean update(@PathVariable Long id, @RequestBody @Valid DataTypeUpsertParam dataTypeUpsertParam) {
        return configDataTypeService.updateDataType(id, dataTypeUpsertParam);
    }

    /**
     * 分页
     */
    @GetMapping
    public Pagination<DataTypePageVO> list(DataTypePageParam dataTypePageParam) {
        return configDataTypeService.list(dataTypePageParam);
    }

    /**
     * 根据条件获取获取各个分类个数列表
     */
    @GetMapping("/list-all")
    public List<DataTypeVO> listAll(@RequestParam DisinfectionDataTypeEnum dataType, Long projectId) {
        return configDataTypeService.listAll(dataType, projectId);
    }

}
