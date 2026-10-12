package com.guhecloud.cube.base.dictionary.client;

import com.guhecloud.cube.base.dictionary.dto.CascadeDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryDetailParam;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.CascadeDictionaryUpdateParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("dictionary")
public interface CascadeDictionaryClient {
    /**
     * 根据id查询字典详情
     */
    @GetMapping("/getCascadeDictionaryById/{id}")
    @Deprecated
    CascadeDictionaryDTO getCascadeDictionaryById(@PathVariable("id") Long id);

    /**
     * 查询字典详情
     */
    @GetMapping("/getCascadeDictionary")
    CascadeDictionaryDTO getCascadeDictionary(@SpringQueryMap @Valid CascadeDictionaryDetailParam param);

    /**
     * 根据字典项编码查询字典树
     */
    @GetMapping("/getCascadeDictionaryItem")
    List<CascadeDictionaryDTO> getCascadeDictionaryItem(@SpringQueryMap @Valid CascadeDictionaryParam param);

    /**
     * 根据字典项编码查询字典树分页
     */
    @GetMapping("/getCascadeDictionaryTreeByPage")
    Pagination<CascadeDictionaryDTO> getCascadeDictionaryTreeByPage(@SpringQueryMap @Valid CascadeDictionaryPageParam param);

    /**
     * 查询所有字典
     */
    @GetMapping("/getAllCascadeDictionaryItem")
    List<CascadeDictionaryDTO> getAllCascadeDictionaryItem(@SpringQueryMap CascadeDictionaryParam param);

    /**
     * 新增字典
     */
    @PostMapping("/addCascadeDictionary")
    CascadeDictionaryDTO addCascadeDictionary(@Valid @RequestBody CascadeDictionaryAddParam param);

    /**
     * 修改字典
     */
    @PutMapping("/updateCascadeDictionary")
    CascadeDictionaryDTO updateCascadeDictionary(@Valid @RequestBody CascadeDictionaryUpdateParam param);

    /**
     * 删除字典
     */
    @DeleteMapping("/deleteCascadeDictionary/{id}")
    Boolean deleteCascadeDictionary(@PathVariable("id") Long id);
}
