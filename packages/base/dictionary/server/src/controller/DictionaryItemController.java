package com.guhecloud.cube.base.dictionary.server.controller;

import com.guhecloud.cube.base.dictionary.server.param.DictionaryItemParam;
import com.guhecloud.cube.base.dictionary.server.service.DictionaryItemService;
import com.guhecloud.cube.base.dictionary.server.vo.DictionaryItemVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 字典项
 *
 * @author : lijie
 * @date : 2023-8-28
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DictionaryItemController {
    private final DictionaryItemService itemService;

    /**
     * 根据code查询字典项详情
     */
    @GetMapping("/getDictionaryItemByCode/{code}")
    public DictionaryItemVO getDictionaryItemByCode(@PathVariable("code") String code) {
        return itemService.getDictionaryItemByCode(code);
    }

    /**
     * 查询所有字典项
     */
    @GetMapping("/getDictionaryItemAll")
    List<DictionaryItemVO> getDictionaryItemAll() {
        return itemService.getDictionaryItemAll();
    }

    /**
     * 新增字典项
     */
    @PostMapping("/addDictionaryItem")
    DictionaryItemVO addDictionaryItem(@Valid @RequestBody DictionaryItemParam param) {
        return itemService.addDictionaryItem(param);
    }

    /**
     * 刷新字典项缓存
     */
    @PostMapping("/public/refresh-dictionary-item-cache")
    public void refreshDictionaryItemCache(@RequestParam("code") String code) {
        itemService.refreshDictionaryItemCache(code);
    }
}
