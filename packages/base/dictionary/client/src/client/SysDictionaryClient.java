package com.guhecloud.cube.base.dictionary.client;

import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryParam;
import com.guhecloud.cube.base.dictionary.param.SysDictionaryaramParam;
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
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("dictionary")
public interface SysDictionaryClient {
    /**
     * 根据id查询字典详情
     */
    @GetMapping("/getSysDictionaryById/{id}")
    SysDictionaryDTO getSysDictionaryById(@PathVariable("id") Long id);

    /**
     * 根据id集合批量查询字典详情
     */
    @PostMapping("/getSysDictionaryByIds")
    List<SysDictionaryDTO> getSysDictionaryByIds(@RequestBody List<Long> ids);

    /**
     * 根据字典id（用逗号分割）批量查询字典详情
     */
    @GetMapping("/getSysDictionaryByIdsStr")
    List<SysDictionaryDTO> getSysDictionaryByIdsStr(@RequestParam("ids") String ids);

    /**
     * 根据字典编码查询字典详情
     */
    @Deprecated
    @GetMapping("/getSysDictionaryByCode")
    SysDictionaryDTO getSysDictionaryByCode(@RequestParam(value = "code") String code);

    @GetMapping("/getSysDictionaryByCodeAndItemCode")
    SysDictionaryDTO getSysDictionaryByCodeAndItemCode(@RequestParam(value = "itemCode") String itemCode,
                                                       @RequestParam(value = "code") String code
                                                      );

    /**
     * 根据字典项编码查询字典值
     */
    @GetMapping("/getSysDictionaryByItem")
    List<SysDictionaryDTO> getSysDictionaryByItem(@SpringQueryMap @Valid SysDictionaryaramParam param);

    /**
     * 根据字典项编码查询字典值分页
     */
    @GetMapping("/getSysDictionaryByItemPage")
    Pagination<SysDictionaryDTO> getSysDictionaryByItemPage(@SpringQueryMap @Valid SysDictionaryPageParam param);

    /**
     * 新增字典
     */
    @PostMapping("/addSysDictionary")
    SysDictionaryDTO addSysDictionary(@Valid @RequestBody SysDictionaryAddParam param);

    /**
     * 修改字典
     */
    @PutMapping("/updateSysDictionary")
    SysDictionaryDTO updateSysDictionary(@Valid @RequestBody SysDictionaryParam param);

    /**
     * 删除字典
     */
    @DeleteMapping("/deleteSysDictionary/{id}")
    Boolean deleteSysDictionary(@PathVariable("id") Long id);

}
