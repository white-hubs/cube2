package com.guhecloud.cube.base.dictionary.server.controller;

import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryAddParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryPageParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryParam;
import com.guhecloud.cube.base.dictionary.server.param.SysDictionaryaramParam;
import com.guhecloud.cube.base.dictionary.server.service.SysDictionaryService;
import com.guhecloud.cube.base.dictionary.server.vo.SysDictionaryVO;
import com.guhecloud.platform.base.model.Pagination;
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

import jakarta.validation.Valid;

import java.util.List;

/**
 * 系统字典
 *
 * @author : lijie
 * @date : 2023-7-3
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SysDictionaryController {
    private final SysDictionaryService sysDictionaryService;

    /**
     * 根据id查询字典详情
     */
    @GetMapping("/getSysDictionaryById/{id}")
    SysDictionaryVO getSysDictionaryById(@PathVariable("id") Long id) {
        return sysDictionaryService.getSysDictionaryById(id);
    }

    /**
     * 根据id集合批量查询字典详情
     */
    @PostMapping("/getSysDictionaryByIds")
    List<SysDictionaryVO> getSysDictionaryByIds(@RequestBody List<Long> ids) {
        return sysDictionaryService.getSysDictionaryByIds(ids);
    }

    /**
     * 根据字典id（用逗号分割）批量查询字典详情
     */
    @GetMapping("/getSysDictionaryByIdsStr")
    List<SysDictionaryVO> getSysDictionaryByIdsStr(String ids) {
        return sysDictionaryService.getSysDictionaryByIdsStr(ids);
    }

    /**
     * 根据字典编码查询字典详情
     */
    @Deprecated
    @GetMapping("/getSysDictionaryByCode")
    SysDictionaryVO getSysDictionaryByCode(@RequestParam(value = "code") String code) {
        return sysDictionaryService.getSysDictionaryByCode(null, code);
    }

    /**
     * 根据字典编码查询字典详情
     */
    @GetMapping("/getSysDictionaryByCodeAndItemCode")
    SysDictionaryVO getSysDictionaryByCodeAndItemCode(@RequestParam(value = "itemCode") String itemCode,
                                                      @RequestParam(value = "code") String code
                                                     ) {
        return sysDictionaryService.getSysDictionaryByCode(itemCode, code);
    }

    /**
     * 根据字典项编码查询字典值
     */
    @GetMapping("/getSysDictionaryByItem")
    List<SysDictionaryVO> getSysDictionaryByItem(SysDictionaryaramParam param) {
        return sysDictionaryService.getSysDictionaryByItem(param);
    }

    /**
     * 获取系统能力配置，仅用于未登录情况使用
     */
    @GetMapping("public/sys-ability-config")
    List<SysDictionaryVO> getSysAbilityConfig() {
        return sysDictionaryService.getSysAbilityConfig();
    }

    /**
     * 根据字典项编码查询字典值分页
     */
    @GetMapping("/getSysDictionaryByItemPage")
    Pagination<SysDictionaryVO> getSysDictionaryByItemPage(@Valid SysDictionaryPageParam param) {
        return sysDictionaryService.getSysDictionaryByItemPage(param);
    }

    /**
     * 新增字典
     */
    @PostMapping("/addSysDictionary")
    SysDictionaryVO addSysDictionary(@Valid @RequestBody SysDictionaryAddParam param) {
        return sysDictionaryService.addSysDictionary(param);
    }

    /**
     * 修改字典
     */
    @PutMapping("/updateSysDictionary")
    SysDictionaryVO updateSysDictionary(@Valid @RequestBody SysDictionaryParam param) {
        return sysDictionaryService.updateSysDictionary(param);
    }

    /**
     * 删除字典
     */
    @DeleteMapping("/deleteSysDictionary/{id}")
    Boolean deleteSysDictionary(@PathVariable("id") Long id) {
        return sysDictionaryService.deleteSysDictionary(id);
    }
}
