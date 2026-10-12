package com.guhecloud.cube.base.dictionary.server.controller;

import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryDetailParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.CascadeDictionaryUpdateParam;
import com.guhecloud.cube.base.dictionary.server.service.CascadeDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.CascadeDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 树型字典
 *
 * @author : lijie
 * @date : 2023-6-27
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CascadeDictionaryController {
    private final CascadeDictionaryService cascadeDictionaryService;

    /**
     * 根据id查询字典详情
     */
    @GetMapping("/getCascadeDictionaryById/{id}")
    CascadeDictionaryVO getCascadeDictionaryById(@PathVariable("id") Long id) {
        return cascadeDictionaryService.getCascadeDictionaryById(id);
    }

    /**
     * 查询字典详情
     */
    @GetMapping("/getCascadeDictionary")
    CascadeDictionaryVO getCascadeDictionary(@SpringQueryMap @Valid CascadeDictionaryDetailParam param) {
        return cascadeDictionaryService.getCascadeDictionary(param);
    }

    /**
     * 根据字典项编码查询字典树
     */
    @GetMapping("/getCascadeDictionaryItem")
    List<CascadeDictionaryVO> getCascadeDictionaryItem(CascadeDictionaryParam param) {
        return cascadeDictionaryService.getCascadeDictionaryItem(param);
    }

    /**
     * 根据字典项编码查询字典树分页
     */
    @GetMapping("/getCascadeDictionaryTreeByPage")
    Pagination<CascadeDictionaryVO> getCascadeDictionaryTreeByPage(CascadeDictionaryPageParam param) {
        return cascadeDictionaryService.getCascadeDictionaryTreeByPage(param);
    }

    /**
     * 查询所有字典
     */
    @GetMapping("/getAllCascadeDictionaryItem")
    List<CascadeDictionaryVO> getAllCascadeDictionaryItem(CascadeDictionaryParam param) {
        return cascadeDictionaryService.getAllCascadeDictionaryItem(param);
    }

    /**
     * 新增字典
     */
    @PostMapping("/addCascadeDictionary")
    CascadeDictionaryVO addCascadeDictionary(@Valid @RequestBody CascadeDictionaryAddParam param) {
        return cascadeDictionaryService.addCascadeDictionary(param);
    }

    /**
     * 修改字典
     */
    @PutMapping("/updateCascadeDictionary")
    CascadeDictionaryVO updateCascadeDictionary(@Valid @RequestBody CascadeDictionaryUpdateParam param) {
        return cascadeDictionaryService.updateCascadeDictionary(param);
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/deleteCascadeDictionary/{id}")
    Boolean deleteCascadeDictionary(@PathVariable("id") Long id) {
        return cascadeDictionaryService.deleteCascadeDictionary(id);
    }

}
